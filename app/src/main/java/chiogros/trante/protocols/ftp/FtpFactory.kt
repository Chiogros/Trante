package chiogros.trante.protocols.ftp

import androidx.compose.runtime.Composable
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.RoomRepository
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.ProtocolFactory
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit.FtpConnectionEditFormState
import kotlinx.coroutines.flow.MutableStateFlow

class FtpFactory(
    override val networkRepository: NetworkRepository,
    override val roomRepository: RoomRepository,
    override val screensConnectionEditForm: @Composable (() -> Unit),
    override var screensConnectionEditFormState: MutableStateFlow<CommonConnectionEditFormState>,
    override val formStateRoomAdapter: FormStateToRoomAdapter
) : ProtocolFactory {
    override suspend fun resetScreensConnectionEditFormState() {
        screensConnectionEditFormState.emit(FtpConnectionEditFormState())
    }
}
