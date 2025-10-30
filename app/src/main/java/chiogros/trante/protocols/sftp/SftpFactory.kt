package chiogros.trante.protocols.sftp

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.repository.NetworkRepository
import chiogros.trante.data.room.repository.RoomRepository
import chiogros.trante.protocols.ProtocolFactory
import chiogros.trante.protocols.sftp.domain.FormStateRoomAdapter
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditFormState

class SftpFactory(
    override val networkRepository: NetworkRepository,
    override val roomRepository: RoomRepository,
    override val screensConnectionEditForm: Composable,
    override val screensConnectionEditFormState: ConnectionEditFormState,
    override val formStateRoomAdapter: FormStateRoomAdapter
) : ProtocolFactory()
