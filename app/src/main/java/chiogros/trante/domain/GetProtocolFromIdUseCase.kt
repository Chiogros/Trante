package chiogros.trante.domain

import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.first

class GetProtocolFromIdUseCase(private val protocolFactoryManager: ProtocolFactoryManager) {
    suspend operator fun invoke(id: String): Protocol {
        Protocol.entries.filter { protocol -> protocol != Protocol.UNKNOWN }.forEach { protocol ->
            val factory = protocolFactoryManager.getFactory(protocol)
            val room = factory.roomRepository

            try {
                room.get(id).first()
                return protocol
            } catch (_: Exception) {
            }
        }

        return Protocol.UNKNOWN
    }
}