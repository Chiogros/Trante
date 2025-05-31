package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionSftpRepository
import chiogros.etomer.data.storage.ConnectionSftp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
data class ConnectionListUiState(
    val sftpConnections: List<ConnectionSftp> = emptyList()
)
*/

class ConnectionListViewModel(private val repository: ConnectionSftpRepository) : ViewModel() {
    /*
    private val _uiState = MutableStateFlow(ConnectionListUiState())
    val uiState: StateFlow<ConnectionListUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val sftpConnections = repository.getAll()
            _uiState.update {
                it.copy(
                    sftpConnections = repository.getAll()
                )
            }
        }
    }
    */

    val connections: Flow<List<ConnectionSftp>> = repository.getAll().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptyList()
    )

    fun update(connection: ConnectionSftp) {
        viewModelScope.launch {
            repository.update(connection)
        }
    }
}
