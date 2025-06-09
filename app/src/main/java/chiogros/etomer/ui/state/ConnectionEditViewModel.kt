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

data class ConnectionEditFormState(
    val id: Long = 0,
    val host: String = "",
    val name: String = "",
    val type: String = "SFTP",
    val user: String = ""
)

data class ConnectionEditUiState(
    val formState: ConnectionEditFormState = ConnectionEditFormState(),
    // Holds initial form data, useful to check for changes
    val originalFormState: ConnectionEditFormState = ConnectionEditFormState(),
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false
) {
    val isEdited: Boolean
        get() = formState != originalFormState
}

class ConnectionEditViewModel(private val repository: ConnectionSftpRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun delete() {
        viewModelScope.launch {
            repository.delete(ConnectionSftp(id = uiState.value.formState.id))
        }
    }

    fun init(id: Long) {
        viewModelScope.launch {
            // Fill out the form with connection data
            val con: ConnectionSftp = repository.get(id).first()
            val conForm = ConnectionEditFormState(
                id = con.id,
                host = con.host,
                name = con.name,
                type = "SFTP",
                user = con.user
            )

            refresh()
            _uiState.update {
                it.copy(
                    formState = conForm,
                    originalFormState = conForm,
                    isEditing = true
                )
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            repository.insert(ConnectionSftp(
                host = uiState.value.formState.host,
                name = uiState.value.formState.name,
                user = uiState.value.formState.user
            ))
        }
    }

    // Initialize states
    fun refresh() {
        _uiState.update {
            it.copy(
                formState = ConnectionEditFormState(),
                originalFormState = ConnectionEditFormState(),
                isEditing = false,
                isDialogShown = false
            )
        }
    }

    fun setIsDialogShown(state: Boolean) {
        _uiState.update {
            it.copy(
                isDialogShown = state
            )
        }
    }

    fun setHost(host: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(host = host)
            )
        }
    }

    fun setName(name: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(name = name)
            )
        }
    }

    fun setType(type: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(type = type)
            )
        }
    }

    fun setUser(user: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(user = user)
            )
        }
    }

    fun update() {
        viewModelScope.launch {
            var con: ConnectionSftp = repository.get(uiState.value.formState.id).first()
            con.host = uiState.value.formState.host
            con.name = uiState.value.formState.name
            con.user = uiState.value.formState.user
            repository.update(con)
        }
    }
}
