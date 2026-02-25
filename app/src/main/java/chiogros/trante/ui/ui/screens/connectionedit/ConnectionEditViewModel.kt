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
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectionEditViewModel(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val deleteConnectionUseCase: DeleteConnectionUseCase,
    private val addConnectionUseCase: AddConnectionUseCase,
    private val updateConnectionUseCase: UpdateConnectionUseCase,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) : ViewModel() {
    val defaultProtocol = Protocol.SFTP

    var factory: ProtocolFactory = protocolFactoryManager.getFactory(defaultProtocol)

    /** Keep track of current form state following currently selected protocol. */
    val formStateState: MutableStateFlow<MutableStateFlow<CommonConnectionEditFormState>> =
        MutableStateFlow(factory.screensConnectionEditFormState)

    private val _uiState = MutableStateFlow(getNewUiState())

    // Allows to track multiple StateFlow from a single "uiState" variable.
    // Actually, data displayed on screen results from those stored in "_uiState" and in each
    // protocol form, so we need to track data updates coming from these two sources.
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Track for protocol change first, THEN
            // track for protocol's state changes and process if modifications in form.
            formStateState.flatMapLatest { formState -> formState }.collect { form ->
                _uiState.update {
                    it.copy(
                        isModified = it.unmodifiedFormHash != form.hashCode()
                    )
                }
            }
        }
    }

    // Backup the deleted connection, useful in case of restore()
    fun backup() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    deletedConnection = factory.screensConnectionEditFormState.value
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

    private fun getNewUiState(): ConnectionEditUiState = ConnectionEditUiState(
        deletedConnection = factory.screensConnectionEditFormState.value,
        protocol = defaultProtocol,
        unmodifiedFormHash = factory.screensConnectionEditFormState.value.hashCode()
    )

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
            val form = factory.formStateRoomAdapter.convert(con)


            _uiState.update {
                it.copy(
                    isEditing = true,
                    // Hashcode must be set before emitting new form
                    // state (see instruction below _uiState.update).
                    // Otherwise, once state gets emitted, modification check (see init{} block
                    // )won't compare to the right value.
                    unmodifiedFormHash = form.hashCode(),
                )
            }

            factory.screensConnectionEditFormState.emit(form)
        }
    }

    // Initialize states
    fun reset() {
        setProtocol(defaultProtocol)

        viewModelScope.launch {
            _uiState.emit(getNewUiState())
        }
    }

    // Restore the last deleted connection
    fun restore() {
        val formState = uiState.value.deletedConnection

        viewModelScope.launch {
            val protocol = getProtocolFromIdUseCase(formState.id)
            setProtocol(protocol)

            factory.screensConnectionEditFormState.emit(formState)
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

        viewModelScope.launch {
            factory.resetScreensConnectionEditFormState()
            formStateState.emit(factory.screensConnectionEditFormState)
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
