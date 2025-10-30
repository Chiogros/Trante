package chiogros.trante.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.domain.AddConnectionUseCase
import chiogros.trante.domain.DeleteConnectionUseCase
import chiogros.trante.domain.GetConnectionUseCase
import chiogros.trante.domain.UpdateConnectionUseCase
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.ProtocolFactoryManager
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


open class ConnectionEditFormState(
    open val id: String = String()
)

data class ConnectionEditUiState(
    val formState: ConnectionEditFormState = ConnectionEditFormState(),
    // Holds initial form data, useful to check for changes
    val originalFormState: ConnectionEditFormState = ConnectionEditFormState(),
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,
    var deletedConnection: Connection = SftpRoom(),
    val showPassword: Boolean = false,
    val protocol: Protocol = Protocol.SFTP
) {
    val isEdited: Boolean
        get() = formState != originalFormState
}

class ConnectionEditViewModel(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val deleteConnectionUseCase: DeleteConnectionUseCase,
    private val addConnectionUseCase: AddConnectionUseCase,
    private val getConnectionUseCase: GetConnectionUseCase,
    private val updateConnectionUseCase: UpdateConnectionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun delete() {
        backup()

        viewModelScope.launch {
            deleteConnectionUseCase(uiState.value.formState.id)
        }
    }

    fun initFrom(id: String) {
        viewModelScope.launch {
            refresh()

            val con: Connection = getConnectionUseCase(id).first()
            val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
            val formState = factory.formStateRoomAdapter.convert(con)

            _uiState.update {
                it.copy(
                    formState = formState, originalFormState = formState, isEditing = true
                )
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
            val con = factory.formStateRoomAdapter.convert(uiState.value.formState)
            addConnectionUseCase(con)
        }
    }

    // Initialize states
    fun refresh() {
        _uiState.update { ConnectionEditUiState() }
    }

    // Restore the last deleted connection
    fun restore() {
        val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
        val formState = factory.formStateRoomAdapter.convert(uiState.value.deletedConnection)

        _uiState.update {
            it.copy(formState = formState)
        }

        insert()
    }

    // Backup the deleted connection, useful in case of restore()
    fun backup() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    deletedConnection = getConnectionUseCase(uiState.value.formState.id).first()
                )
            }
        }
    }

    fun setIsDialogShown(state: Boolean) {
        _uiState.update { it.copy(isDialogShown = state) }
    }

    fun setHost(host: String) {
        _uiState.update { it.formState.= host)) }
    }

    fun setName(name: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(name = name)) }
    }

    fun setPassword(password: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(password = password)) }
    }

    fun setType(type: Protocol) {
        _uiState.update { it.copy(protocol = type) }
    }

    fun setUser(user: String) {
        _uiState.update { it.copy(formState = uiState.value.formState.copy(user = user)) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !uiState.value.showPassword) }
    }

    fun update() {
        viewModelScope.launch {
            val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
            val con = factory.formStateRoomAdapter.convert(uiState.value.formState)

            con.state = ConnectionState.NEVER_USED
            con.enabled = false

            updateConnectionUseCase(con)
        }
    }
}
