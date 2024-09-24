package template.feature.nba.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import template.core.common.error.Error
import template.core.common.result.fold
import template.core.data.repository.NbaRepository
import template.core.model.Team
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val nbaRepository: NbaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsUiState(isLoading = true))
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    init {
        getTeams()
    }

    fun onEvent(event: TeamsUiEvent) {
        when (event) {
            is TeamsUiEvent.OpenDialog -> onDialogClicked()
            is TeamsUiEvent.CloseDialog -> onDialogClosed()
            is TeamsUiEvent.Sort -> onSortingSelected(event.type)
            is TeamsUiEvent.ErrorConsumed -> onErrorConsumed()
        }
    }

    private fun getTeams() {
        viewModelScope.launch {
            nbaRepository.getTeams()
                .collect { result ->
                    _uiState.update { it.copy(isLoading = false) }

                    result.fold(
                        onSuccess = { teams ->
                            _uiState.update { it.copy(teams = teams) }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(error = error) }
                        }
                    )
                }
        }
    }

    private fun onDialogClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDialogVisible = true) }
        }
    }

    private fun onDialogClosed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDialogVisible = false) }
        }
    }


    private fun onSortingSelected(sortType: SortType) {
        val sortedList = when (sortType) {
            SortType.Name -> {
                uiState.value.teams.sortedWith(compareBy<Team> { it.fullName.isBlank() }.thenBy { it.fullName })
            }

            is SortType.City -> {
                uiState.value.teams.sortedWith(compareBy<Team> { it.city.isBlank() }.thenBy { it.city })
            }

            is SortType.Conference -> {
                uiState.value.teams.sortedWith(compareBy<Team> { it.conference.isBlank() }.thenBy { it.conference })
            }
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teams = sortedList,
                    isDialogVisible = false,
                    buttonText = sortType
                )
            }
        }
    }

    private fun onErrorConsumed() {
        _uiState.update { it.copy(error = null) }
    }
}

sealed interface TeamsUiEvent {
    data object OpenDialog : TeamsUiEvent
    data object CloseDialog : TeamsUiEvent
    data object ErrorConsumed : TeamsUiEvent
    data class Sort(val type: SortType) : TeamsUiEvent
}

data class TeamsUiState(
    val teams: List<Team> = emptyList(),
    val isDialogVisible: Boolean = false,
    val buttonText: SortType = SortType.Name,
    val isLoading: Boolean = false,
    val error: Error? = null
)

sealed interface SortType {
    data object Name : SortType
    data object City : SortType
    data object Conference : SortType
}
