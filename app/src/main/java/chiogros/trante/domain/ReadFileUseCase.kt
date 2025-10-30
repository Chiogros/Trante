package chiogros.trante.domain

import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.first
import java.io.ByteArrayInputStream
import java.io.InputStream

class ReadFileUseCase(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val getProtocolFromIdUseCase: GetProtocolFromIdUseCase
) {
    suspend operator fun invoke(id: String, path: String): InputStream {
        val protocol = getProtocolFromIdUseCase(id)
        val factory = protocolFactoryManager.getFactory(protocol)

        val room = factory.roomRepository
        val network = factory.networkRepository

        val con = room.get(id).first()
        if (!network.connect(con)) {
            return ByteArrayInputStream(ByteArray(0))
        }
        return network.readFile(con, path)
    }
}