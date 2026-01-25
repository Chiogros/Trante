package chiogros.trante.domain

import chiogros.trante.data.network.File
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.first
import kotlin.io.path.Path

class GetFileStatUseCase(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) {
    suspend operator fun invoke(id: String, path: String): File {
        val protocol = getProtocolFromIdUseCase(id)
        val factory = protocolFactoryManager.getFactory(protocol)

        val room = factory.roomRepository
        val network = factory.networkRepository

        val con = room.get(id).first()

        if (!network.connect(con)) {
            return File(Path(path))
        }

        return network.getFileStat(con, path)
    }
}