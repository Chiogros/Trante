package chiogros.trante.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.data.room.repository.RoomManager
import chiogros.trante.data.room.sftp.SftpRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ConnectionEditFormState(
    val id: String = String(),
    val host: String = String(),
    val name: String = String(),
    val type: String = SftpRoom.toString(),
    val user: String = String(),
    val password: String = String()
)

data class ConnectionEditUiState(
    val formState: ConnectionEditFormState = ConnectionEditFormState(),
    // Holds initial form data, useful to check for changes
    val originalFormState: ConnectionEditFormState = ConnectionEditFormState(),
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,
    var deletedConnection: Connection = SftpRoom(),
    val showPassword: Boolean = false
) {
    val isEdited: Boolean
        get() = formState != originalFormState
}

class ConnectionEditViewModel(private val repository: RoomManager) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun delete() {
        backup()

        val con = when (uiState.value.formState.type) {
            SftpRoom.toString() -> {
                SftpRoom(id = uiState.value.formState.id)
            }

            else                -> error("Type ${uiState.value.formState.type} unknown!")
        }

        viewModelScope.launch {
            repository.delete(con)
        }
    }

    fun initFrom(id: String) {
        viewModelScope.launch {
            refresh()

            // Fill out the form with connection data
            val con: Connection = repository.get(id).first()
            val conForm = ConnectionEditFormState(
                id = con.id,
                host = con.host,
                name = con.name,
                type = con.toString(),
                user = con.user,
                password = String(CryptoUtils().decrypt(con.password))
            )
            _uiState.update {
                it.copy(
                    formState = conForm, originalFormState = conForm, isEditing = true
                )
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            repository.insert(
                when (uiState.value.formState.type) {
                    SftpRoom.toString() -> {
                        SftpRoom(
                            host = uiState.value.formState.host,
                            name = uiState.value.formState.name,
                            user = uiState.value.formState.user,
                            password = CryptoUtils().encrypt(uiState.value.formState.password.toByteArray())
                        )
                    }

                    else                -> error("Type ${uiState.value.formState.type} unknown!")
                }
            )
        }
    }

    // Initialize states
    fun refresh() {
        _uiState.update {
            it.copy(
                formState = ConnectionEditFormState(),
                originalFormState = ConnectionEditFormState(),
                isEditing = false,
                isDialogShown = false,
                deletedConnection = SftpRoom(),
                showPassword = false
            )
        }
    }

    // Restore the last deleted connection
    fun restore() {
        _uiState.update {
            it.copy(
                formState = ConnectionEditFormState(
                    id = uiState.value.deletedConnection.id,
                    host = uiState.value.deletedConnection.host,
                    name = uiState.value.deletedConnection.name,
                    user = uiState.value.deletedConnection.user,
                    password = String(CryptoUtils().decrypt(uiState.value.deletedConnection.password))
                )
            )
        }
        insert()
    }

    // Backup the deleted connection, useful in case of restore()
    fun backup() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    deletedConnection = repository.get(uiState.value.formState.id).first()
                )
            }
        }
    }

    fun setIsDialogShown(state: Boolean) {
        _uiState.update { it.copy(isDialogShown = state) }
    }

    fun setHost(host: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(host = host)) }
    }

    fun setName(name: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(name = name)) }
    }

    fun setPassword(password: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(password = password)) }
    }

    fun setType(type: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(type = type)) }
    }

    fun setUser(user: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(user = user)) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !uiState.value.showPassword) }
    }

    fun update() {
        viewModelScope.launch {
            val con: Connection = repository.get(uiState.value.formState.id).first()
            con.host = uiState.value.formState.host
            con.name = uiState.value.formState.name
            con.user = uiState.value.formState.user
            con.password = CryptoUtils().encrypt(uiState.value.formState.password.toByteArray())
            con.state = ConnectionState.NEVER_USED
            con.enabled = false
            repository.update(con)
        }
    }
}
