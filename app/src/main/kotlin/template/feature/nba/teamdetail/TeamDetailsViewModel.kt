package template.feature.nba.teamdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import template.core.common.error.Error
import template.core.common.result.fold
import template.core.data.repository.NbaRepository
import template.core.model.Game
import template.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val nbaRepository: NbaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teamDetailsRoute: Screen.TeamDetails = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(TeamDetailsUiState(isLoading = true))
    val uiState: StateFlow<TeamDetailsUiState> = _uiState.asStateFlow()

    val teamId: StateFlow<Int> = savedStateHandle.getStateFlow(
        key = "TEAM_ID_ARG",
        initialValue = teamDetailsRoute.teamId
    )

    init {
        getTeams()
    }

    fun onEvent(event: TeamDetailsUiEvent) {
        when (event) {
            is TeamDetailsUiEvent.LoadMore -> getTeams()
            is TeamDetailsUiEvent.ErrorConsumed -> onErrorConsumed()
        }
    }

    private fun getTeams() {
        viewModelScope.launch {
            nbaRepository.getGames(teamId.value)
                .collect { result ->
                    _uiState.update { it.copy(isLoading = false) }

                    result.fold(
                        onSuccess = { games ->
                            _uiState.update { it.copy(
                                games = uiState.value.games + games,
                                name = teamId.value.toString()
                            ) }
                        },
                        onFailure = { error ->
                            _uiState.update { it.copy(error = error) }
                        }
                    )
                }
        }
    }

    private fun onErrorConsumed() {
        _uiState.update { it.copy(error = null) }
    }
}

data class TeamDetailsUiState(
    val games: List<Game> = emptyList(),
    val name: String = "",
    val error: Error? = null,
    val isLoading: Boolean = false
)

sealed interface TeamDetailsUiEvent {
    data object ErrorConsumed : TeamDetailsUiEvent
    data object LoadMore : TeamDetailsUiEvent
}
