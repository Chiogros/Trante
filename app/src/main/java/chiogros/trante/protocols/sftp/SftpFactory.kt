package chiogros.trante.protocols.sftp

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.RoomRepository
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.ProtocolFactory
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

class SftpFactory(
    override val networkRepository: NetworkRepository,
    override val roomRepository: RoomRepository,
    override val screensConnectionEditForm: @Composable (() -> Unit),
    override val screensConnectionEditFormState: ConnectionEditCommonFormState,
    override val formStateRoomAdapter: FormStateToRoomAdapter
) : ProtocolFactory()
