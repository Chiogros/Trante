package chiogros.trante.protocols

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.RoomRepository
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import kotlinx.coroutines.flow.MutableStateFlow

interface ProtocolFactory {
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
    val screensConnectionEditForm: @Composable (() -> Unit)
    var screensConnectionEditFormState: MutableStateFlow<CommonConnectionEditFormState>
    val formStateRoomAdapter: FormStateToRoomAdapter
    suspend fun resetScreensConnectionEditFormState()
}