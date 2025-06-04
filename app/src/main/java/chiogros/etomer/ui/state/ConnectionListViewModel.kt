package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionSftpRepository
import chiogros.etomer.data.storage.ConnectionSftp
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConnectionListViewModel(private val repository: ConnectionSftpRepository) : ViewModel() {
    val uiState: StateFlow<List<ConnectionSftp>> = repository.getAll().stateIn(
            viewModelScope,
            WhileSubscribed(5000),
            emptyList()
        )

    fun delete(connection: ConnectionSftp) {
        viewModelScope.launch {
            repository.delete(connection)
        }
    }

    fun insert(connection: ConnectionSftp) {
        viewModelScope.launch {
            repository.insert(connection)
        }
    }

    fun update(from: ConnectionSftp, to: ConnectionSftp) {
        delete(from)
        insert(to)
    }

    fun toggle(connection: ConnectionSftp) {
        val newConnection = connection.copy()
        newConnection.enabled = !connection.enabled
        update(connection, newConnection)
    }
}
