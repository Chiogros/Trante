package chiogros.etomer.ui.ui.screens.connectionslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.room.ConnectionManager
import chiogros.etomer.data.room.Connection
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

class ConnectionsListViewModel(private val repository: ConnectionManager) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ConnectionsListUiState(connections = MutableStateFlow(emptyList()))
    )
    val uiState: StateFlow<ConnectionsListUiState> = _uiState.asStateFlow()

    init {
        loadConnections()
    }

    fun delete(connection: Connection) {
        viewModelScope.launch {
            repository.delete(connection)
        }
    }

    fun insert(connection: Connection) {
        viewModelScope.launch {
            repository.insert(connection)
        }
    }

    fun loadConnections() {
        _uiState.update {
            it.copy(
                connections = repository.getAll().stateIn(
                    viewModelScope, WhileSubscribed(5000), emptyList()
                )
            )
        }
    }

    fun toggle(connection: Connection) {
        connection.enabled = !connection.enabled
        update(connection)
    }

    fun update(connection: Connection) {
        viewModelScope.launch {
            repository.update(connection)
            loadConnections()
        }
    }
}
