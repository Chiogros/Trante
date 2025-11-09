package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConnectionEditViewModelSftp : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditFormStateSftp())
    val uiState: StateFlow<ConnectionEditFormStateSftp> = _uiState.asStateFlow()

    fun setHost(host: String) {
        _uiState.update {
            it.host = host
            it
        }
    }

    fun setPassword(password: String) {
        _uiState.value.password = password
    }

    fun setUser(user: String) {
        _uiState.value.user = user
    }

    fun togglePasswordVisibility() {
        _uiState.value.showPassword = !uiState.value.showPassword
    }
}
