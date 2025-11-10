package chiogros.trante.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.domain.AddConnectionUseCase
import chiogros.trante.domain.DeleteConnectionUseCase
import chiogros.trante.domain.GetConnectionUseCase
import chiogros.trante.domain.GetProtocolFromIdUseCase
import chiogros.trante.domain.UpdateConnectionUseCase
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectionEditViewModel(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val deleteConnectionUseCase: DeleteConnectionUseCase,
    private val addConnectionUseCase: AddConnectionUseCase,
    private val getConnectionUseCase: GetConnectionUseCase,
    private val updateConnectionUseCase: UpdateConnectionUseCase,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    private

    // Backup the deleted connection, useful in case of restore()
    fun backup() {

        viewModelScope.launch {
            val protocol =
                getProtocolFromIdUseCase(uiState.value.formState.value.id)
            val factory = protocolFactoryManager.getFactory(protocol)
            val room = factory.roomRepository

            _uiState.update {
                it.copy(
                    deletedConnection = room.get(uiState.value.formState.value.id).first()
                )
            }
        }
    }

    fun delete() {
        backup()

        viewModelScope.launch {
            val protocol = getProtocolFromIdUseCase(uiState.value.formState.value.id)
            val factory = protocolFactoryManager.getFactory(protocol)
            val room = factory.roomRepository
            deleteConnectionUseCase(room.get(uiState.value.formState.value.id).first())
        }
    }

    fun initFrom(id: String) {
        viewModelScope.launch {
            refresh()

            val protocol = getProtocolFromIdUseCase(id)
            val factory = protocolFactoryManager.getFactory(protocol)
            val con = factory.roomRepository.get(id).first()
            val form = factory.screensConnectionEditForm
            val formState = factory.formStateRoomAdapter.convert(con)
            val mutableFormState: MutableStateFlow<ConnectionEditCommonFormState> =
                MutableStateFlow(formState)

            _uiState.update {
                it.copy(
                    form = form,
                    //formState = mutableFormState,
                    isEditing = true,
                    originalFormState = mutableFormState.asStateFlow(),
                    protocol = protocol
                )
            }

            _uiState.value.formState.tryEmit(formState)
        }
    }

    fun insert() {
        val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
        val con = factory.formStateRoomAdapter.convert(uiState.value.formState.value)

        viewModelScope.launch {
            addConnectionUseCase(con)
        }
    }

    // Initialize states
    fun refresh() {
        _uiState.update { ConnectionEditUiState() }
        setType(uiState.value.protocol)
    }

    // Restore the last deleted connection
    fun restore() {
        val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
        val formState = factory.formStateRoomAdapter.convert(uiState.value.deletedConnection)
        val mutableFormState: MutableStateFlow<ConnectionEditCommonFormState> =
            MutableStateFlow(formState)

        _uiState.update {
            it.copy(formState = mutableFormState)
        }

        insert()
    }

    fun setIsDialogShown(state: Boolean) {
        _uiState.update { it.copy(isDialogShown = state) }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            uiState.value.formState.emit(uiState.value.formState.value.copy(name = name))
        }
    }

    fun setType(type: Protocol) {
        val factory = protocolFactoryManager.getFactory(type)
        _uiState.update {
            it.copy(
                form = factory.screensConnectionEditForm,
                //formState = MutableStateFlow(factory.screensConnectionEditFormState),
                protocol = type
            )
        }
        viewModelScope.launch {
            uiState.value.formState.emit(factory.screensConnectionEditFormState)
        }
    }

    fun update() {
        val factory = protocolFactoryManager.getFactory(uiState.value.protocol)
        val con = factory.formStateRoomAdapter.convert(uiState.value.formState.value)

        con.state = ConnectionState.NEVER_USED
        con.enabled = false

        viewModelScope.launch {
            updateConnectionUseCase(con)
        }
    }
}
