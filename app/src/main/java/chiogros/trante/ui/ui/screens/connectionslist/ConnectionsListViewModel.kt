package chiogros.trante.ui.ui.screens.connectionslist

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.data.room.Connection
import chiogros.trante.domain.DisableConnectionUseCase
import chiogros.trante.domain.EnableConnectionUseCase
import chiogros.trante.domain.GetConnectionsUseCase
import chiogros.trante.domain.GetProtocolFromIdUseCase
import chiogros.trante.protocols.Protocol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConnectionsListUiState(
    val connections: SnapshotStateMap<Protocol, List<Connection>> = mutableStateMapOf(),
    val isLoadingConnections: Boolean = true,
    val isConnectionDeleted: Boolean = false
)

class ConnectionsListViewModel(
    private val enableConnectionUseCase: EnableConnectionUseCase,
    private val disableConnectionUseCase: DisableConnectionUseCase,
    private val getConnectionsUseCase: GetConnectionsUseCase,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionsListUiState())

    val uiState: StateFlow<ConnectionsListUiState> = _uiState.asStateFlow()

    init {
        loadConnections()
    }

    fun loadConnections() {
        viewModelScope.launch {
            getConnectionsUseCase().collect { pair ->
                uiState.value.connections[pair.first] = pair.second
            }
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
