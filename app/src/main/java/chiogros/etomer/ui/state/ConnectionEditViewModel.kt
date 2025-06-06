package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionSftpRepository
import chiogros.etomer.data.storage.ConnectionSftp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConnectionEditUiState(
    val id: Long = 0,
    val host: String = "",
    val name: String = "",
    val type: String = "SFTP",
    val user: String = "",
    val isEdited: Boolean = false,
    val isEditing: Boolean = false
    )

class ConnectionEditViewModel(private val repository: ConnectionSftpRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun delete() {
        viewModelScope.launch {
            repository.delete(ConnectionSftp(id = uiState.value.id))
        }
    }

    fun init(id: Long) {
        viewModelScope.launch {
            val con: ConnectionSftp = repository.get(id).first()
            _uiState.update {
                it.copy(
                    id = con.id,
                    host = con.host,
                    type = "SFTP",
                    user = con.user,
                    isEditing = true
                )
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            repository.insert(ConnectionSftp(
                host = uiState.value.host,
                name = uiState.value.name,
                user = uiState.value.user
            ))
        }
    }

    fun refresh() {
        _uiState.update {
            it.copy(
                id = 0,
                host = "",
                name = "",
                type = "SFTP",
                user = "",
                isEdited = false,
                isEditing = false
            )
        }
    }

    fun setHost(host: String) {
        _uiState.update {
            it.copy(
                host = host,
                isEdited = true
            )
        }
    }

    fun setType(type: String) {
        _uiState.update {
            it.copy(
                type = type,
                isEdited = true
            )
        }
    }

    fun setUser(user: String) {
        _uiState.update {
            it.copy(
                user = user,
                isEdited = true
            )
        }
    }

    fun update() {
        viewModelScope.launch {
            var con: ConnectionSftp = repository.get(uiState.value.id).first()
            con.host = uiState.value.host
            con.user = uiState.value.user
            con.name = uiState.value.name
            repository.update(con)
        }
    }
}
