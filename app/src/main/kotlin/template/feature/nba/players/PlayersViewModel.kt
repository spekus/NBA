package template.feature.nba.players

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
import template.core.model.Player
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val nbaRepository: NbaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayersUiState(isLoading = true))
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()

    init {
        getPlayers()
    }

    fun onEvent(event: PlayersUiEvent) {
        when (event) {
            is PlayersUiEvent.Search -> getPlayers(event.name)
            is PlayersUiEvent.LoadMore -> getPlayers(uiState.value.search)
            is PlayersUiEvent.ErrorConsumed -> onErrorConsumed()
        }
    }

    private fun getPlayers(search: String = "") {
        viewModelScope.launch {
            nbaRepository.getPlayers(search).collect { result ->
                _uiState.update { it.copy(isLoading = false) }

                result.fold(onSuccess = { players ->
                    _uiState.update {
                        it.copy(
                            players = if (search == uiState.value.search) (players + uiState.value.players).distinct() else players,
                            search = search
                        )
                    }
                }, onFailure = { error ->
                    _uiState.update { it.copy(error = error) }
                })
            }
        }
    }

    private fun onErrorConsumed() {
        _uiState.update { it.copy(error = null) }
    }
}

sealed interface PlayersUiEvent {
    data class Search(val name: String) : PlayersUiEvent
    data object ErrorConsumed : PlayersUiEvent
    data object LoadMore : PlayersUiEvent
}

data class PlayersUiState(
    val players: List<Player> = emptyList(),
    val search: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null
)