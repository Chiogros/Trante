package chiogros.etomer.ui.ui.screens.connectionslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.room.Connection
import chiogros.etomer.domain.DeleteConnectionUseCase
import chiogros.etomer.domain.DisableConnectionUseCase
import chiogros.etomer.domain.EnableConnectionUseCase
import chiogros.etomer.domain.GetConnectionsUseCase
import chiogros.etomer.domain.InsertConnectionUseCase
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
    private val deleteConnectionUseCase: DeleteConnectionUseCase,
    private val insertConnectionUseCase: InsertConnectionUseCase,
    private val getConnectionsUseCase: GetConnectionsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ConnectionsListUiState(connections = MutableStateFlow(emptyList()))
    )
    val uiState: StateFlow<ConnectionsListUiState> = _uiState.asStateFlow()

    init {
        loadConnections()
    }

    fun delete(con: Connection) {
        viewModelScope.launch {
            deleteConnectionUseCase(con.id)
        }
    }

    fun insert(con: Connection) {
        viewModelScope.launch {
            insertConnectionUseCase(con)
        }
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
                disableConnectionUseCase(con.id)
            } else {
                enableConnectionUseCase(con.id)
            }
        }
    }
}
