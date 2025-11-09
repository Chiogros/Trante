package chiogros.trante.domain

import chiogros.trante.data.network.File
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.first

class ListFilesInDirectoryUseCase(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) {
    suspend operator fun invoke(id: String, path: String): List<File> {
        val protocol = getProtocolFromIdUseCase(id)
        val factory = protocolFactoryManager.getFactory(protocol)

        val room = factory.roomRepository
        val network = factory.networkRepository

        val con = room.get(id).first()

        if (!network.connect(con)) {
            return emptyList()
        }

        return network.listFiles(con, path)
    }
}