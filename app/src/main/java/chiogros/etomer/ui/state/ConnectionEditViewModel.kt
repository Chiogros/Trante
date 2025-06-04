package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionSftpRepository
import chiogros.etomer.data.storage.ConnectionSftp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConnectionEditUiState(
    val host: String = "",
    val type: String = "SFTP",
    val user: String = ""
    )

class ConnectionEditViewModel(private val repository: ConnectionSftpRepository) : ViewModel() {
    private val _uiState =  MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun refreshAll() {
        setHost("")
        setType("SFTP")
        setUser("")
    }

    fun insert(connection: ConnectionSftp) {
        viewModelScope.launch {
            repository.insert(connection)
        }
    }

    fun setHost(host: String) {
        _uiState.update {
            it.copy(
                host = host
            )
        }
    }

    fun setType(type: String) {
        _uiState.update {
            it.copy(
                type = type
            )
        }
    }

    fun setUser(user: String) {
        _uiState.update {
            it.copy(
                user = user
            )
        }
    }
}
