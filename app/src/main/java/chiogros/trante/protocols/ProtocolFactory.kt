package chiogros.trante.protocols

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.repository.NetworkRepository
import chiogros.trante.data.room.repository.RoomRepository
import chiogros.trante.protocols.sftp.domain.FormStateRoomAdapter
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditFormState

abstract class ProtocolFactory {
    abstract val networkRepository: NetworkRepository
    abstract val roomRepository: RoomRepository
    abstract val screensConnectionEditForm: Composable
    abstract val screensConnectionEditFormState: ConnectionEditFormState
    abstract val formStateRoomAdapter: FormStateRoomAdapter
}