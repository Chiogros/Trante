package chiogros.trante.ui.ui.screens.connectionslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.data.room.Connection
import chiogros.trante.domain.DisableConnectionUseCase
import chiogros.trante.domain.EnableConnectionUseCase
import chiogros.trante.domain.GetConnectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConnectionsListUiState(
    val connections: StateFlow<List<Connection>>, val isConnectionDeleted: Boolean = false
)

class ConnectionsListViewModel(
    private val enableConnectionUseCase: EnableConnectionUseCase,
    private val disableConnectionUseCase: DisableConnectionUseCase,
    private val getConnectionsUseCase: GetConnectionsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ConnectionsListUiState(connections = MutableStateFlow(emptyList()))
    )
    val uiState: StateFlow<ConnectionsListUiState> = _uiState.asStateFlow()

    init {
        loadConnections()
    }

    fun loadConnections() {
        _uiState.update {
            it.copy(
                connections = getConnectionsUseCase().stateIn(
                    viewModelScope, WhileSubscribed(5000), emptyList()
                )
            )
        }
    }

    fun toggle(con: Connection) {
        viewModelScope.launch {
            if (con.enabled) {
                disableConnectionUseCase(con)
            } else {
                enableConnectionUseCase(con)
            }
        }
    }
}
