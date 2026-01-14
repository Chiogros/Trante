package chiogros.trante.protocols

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.RoomRepository
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.common.CommonConnectionEditFormState

abstract class ProtocolFactory {
    abstract val networkRepository: NetworkRepository
    abstract val roomRepository: RoomRepository
    abstract val screensConnectionEditForm: @Composable (() -> Unit)
    abstract val screensConnectionEditFormState: CommonConnectionEditFormState
    abstract val formStateRoomAdapter: FormStateToRoomAdapter
}