package chiogros.trante.domain

import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.first

class CreateFileUseCase(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) {
    suspend operator fun invoke(id: String, path: String): Boolean {
        val protocol = getProtocolFromIdUseCase(id)
        val factory = protocolFactoryManager.getFactory(protocol)

        val room = factory.roomRepository
        val network = factory.networkRepository

        val con = room.get(id).first()
        return network.createFile(con, path)
    }
}