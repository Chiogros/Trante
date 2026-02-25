package chiogros.trante.protocols

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.RoomRepository
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import kotlinx.coroutines.flow.MutableStateFlow

/** Describe all members protocols need to implement. */
interface ProtocolFactory {
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository

    /** Form UI containing text fields */
    val screensConnectionEditForm: @Composable (() -> Unit)

    /** MutableStateFlow so it allows to track changes on the connection's form. */
    var screensConnectionEditFormState: MutableStateFlow<CommonConnectionEditFormState>

    val formStateRoomAdapter: FormStateToRoomAdapter

    /** Convenient way to clear out form data.
     * @see screensConnectionEditFormState
     */
    suspend fun resetScreensConnectionEditFormState()
}