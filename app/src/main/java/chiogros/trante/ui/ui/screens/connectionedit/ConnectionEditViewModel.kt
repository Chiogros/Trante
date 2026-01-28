package chiogros.trante.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.domain.AddConnectionUseCase
import chiogros.trante.domain.DeleteConnectionUseCase
import chiogros.trante.domain.GetProtocolFromIdUseCase
import chiogros.trante.domain.UpdateConnectionUseCase
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.ProtocolFactory
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectionEditViewModel(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val deleteConnectionUseCase: DeleteConnectionUseCase,
    private val addConnectionUseCase: AddConnectionUseCase,
    private val updateConnectionUseCase: UpdateConnectionUseCase,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) : ViewModel() {
    val defaultProtocol = Protocol.SFTP
    var factory: ProtocolFactory = protocolFactoryManager.getFactory(defaultProtocol)

    private val _uiState =
        MutableStateFlow(
            ConnectionEditUiState(
                deletedConnection = factory.formStateRoomAdapter.convert(
                    factory.screensConnectionEditFormState.value
                ),
                protocol = defaultProtocol,
                unmodifiedFormHash = factory.screensConnectionEditFormState.value.hashCode()
            )
        )

    val uiState: StateFlow<ConnectionEditUiState> = combine(
        _uiState,
        factory.screensConnectionEditFormState
    ) { uiState, formState ->
        uiState.copy(isModified = uiState.unmodifiedFormHash != formState.hashCode())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConnectionEditUiState(
            deletedConnection = factory.formStateRoomAdapter.convert(
                factory.screensConnectionEditFormState.value
            ),
            protocol = defaultProtocol,
            unmodifiedFormHash = factory.screensConnectionEditFormState.value.hashCode()
        )
    )

    // Backup the deleted connection, useful in case of restore()
    fun backup() {
        viewModelScope.launch {
            val room = factory.roomRepository

            _uiState.update {
                it.copy(
                    deletedConnection = room.get(factory.screensConnectionEditFormState.value.id)
                        .first()
                )
            }
        }
    }

    fun delete() {
        backup()

        val room = factory.roomRepository

        viewModelScope.launch {
            deleteConnectionUseCase(
                room.get(factory.screensConnectionEditFormState.value.id).first()
            )
        }
    }

    fun insert() {
        val con = factory.formStateRoomAdapter.convert(factory.screensConnectionEditFormState.value)

        viewModelScope.launch {
            addConnectionUseCase(con)
        }
    }

    fun load(id: String) {
        reset()

        viewModelScope.launch {
            val protocol = getProtocolFromIdUseCase(id)
            setProtocol(protocol)

            val con = factory.roomRepository.get(id).first()
            factory.screensConnectionEditFormState.emit(factory.formStateRoomAdapter.convert(con))
            val form = factory.screensConnectionEditFormState.value

            _uiState.update {
                it.copy(
                    isEditing = true,
                    unmodifiedFormHash = form.hashCode()
                )
            }
        }
    }

    // Initialize states
    fun reset() {
        setProtocol(defaultProtocol)

        viewModelScope.launch {
            factory.resetScreensConnectionEditFormState()

            _uiState.emit(
                ConnectionEditUiState(
                    deletedConnection = factory.formStateRoomAdapter.convert(
                        factory.screensConnectionEditFormState.value
                    ),
                    protocol = defaultProtocol,
                    unmodifiedFormHash = factory.screensConnectionEditFormState.value.hashCode(),
                )
            )
        }
    }

    // Restore the last deleted connection
    fun restore() {
        val formState2 = factory.formStateRoomAdapter.convert(uiState.value.deletedConnection)

        viewModelScope.launch {
            factory.screensConnectionEditFormState.emit(formState2)
        }

        insert()
    }

    fun setProtocol(type: Protocol) {
        factory = protocolFactoryManager.getFactory(type)

        _uiState.update {
            it.copy(
                protocol = type
            )
        }
    }

    fun showDialog(state: Boolean) {
        _uiState.update { it.copy(isDialogShown = state) }
    }

    fun update() {
        val con = factory.formStateRoomAdapter.convert(factory.screensConnectionEditFormState.value)

        con.state = ConnectionState.NEVER_USED
        con.enabled = false

        viewModelScope.launch {
            updateConnectionUseCase(con)
        }
    }
}
