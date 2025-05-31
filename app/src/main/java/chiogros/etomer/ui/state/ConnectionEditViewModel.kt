package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionSftpRepository
import chiogros.etomer.data.storage.ConnectionSftp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConnectionEditUiState(
    var host: String,
    var user: String
)

class ConnectionEditViewModel(private val repository: ConnectionSftpRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditUiState(
        host = "",
        user = ""
    ))
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun insert(connection: ConnectionSftp) {
        viewModelScope.launch {
            repository.insert(connection)
        }
    }
}
