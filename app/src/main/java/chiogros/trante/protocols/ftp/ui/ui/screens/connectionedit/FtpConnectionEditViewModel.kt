package chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.protocols.common.CommonConnectionEditViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FtpConnectionEditViewModel(private val _uiState: MutableStateFlow<FtpConnectionEditFormState>) :
    CommonConnectionEditViewModel, ViewModel() {
    val uiState: StateFlow<FtpConnectionEditFormState> = _uiState.asStateFlow()
    val showPassword = MutableStateFlow(false)

    fun setHost(host: String) {
        _uiState.update {
            it.copy(host = host)
        }
    }

    override fun setName(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun setPassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun setUser(user: String) {
        _uiState.update {
            it.copy(user = user)
        }
    }

    fun togglePasswordVisibility() {
        viewModelScope.launch {
            showPassword.emit(!showPassword.value)
        }
    }

    override fun verify(): Boolean {
        return (uiState.value.host.isNotEmpty()
                && uiState.value.user.isNotEmpty()
                && uiState.value.password.isNotEmpty())
    }
}
