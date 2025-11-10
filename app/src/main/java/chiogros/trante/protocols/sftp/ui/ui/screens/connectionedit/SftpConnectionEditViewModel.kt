package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SftpConnectionEditViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SftpConnectionEditFormState())
    val uiState: StateFlow<SftpConnectionEditFormState> = _uiState.asStateFlow()

    fun setHost(host: String) {
        viewModelScope.launch {
            _uiState.emit(uiState.value.copy(host = host))
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _uiState.emit(uiState.value.copy(password = password))
        }
    }

    fun setUser(user: String) {
        viewModelScope.launch {
            _uiState.emit(uiState.value.copy(user = user))
        }
    }

    fun togglePasswordVisibility() {
        _uiState.value.showPassword = !uiState.value.showPassword
    }
}
