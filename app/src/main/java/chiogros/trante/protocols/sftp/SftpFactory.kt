package chiogros.trante.protocols.sftp

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.RoomRepository
import chiogros.trante.protocols.ProtocolFactory
import chiogros.trante.protocols.sftp.domain.FormStateToRoomAdapterSftp
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

class SftpFactory(
    override val networkRepository: NetworkRepository,
    override val roomRepository: RoomRepository,
    override val screensConnectionEditForm: @Composable (() -> Unit),
    override val screensConnectionEditCommonFormState: ConnectionEditCommonFormState,
    override val formStateRoomAdapter: FormStateToRoomAdapterSftp
) : ProtocolFactory()
