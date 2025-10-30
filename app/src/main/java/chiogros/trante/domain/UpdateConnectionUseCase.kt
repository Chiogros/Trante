package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.ProtocolFactoryManager

class UpdateConnectionUseCase(private val protocolFactoryManager: ProtocolFactoryManager) {
    suspend operator fun invoke(con: Connection) {
        val factory = protocolFactoryManager.getFactory(con)
        val room = factory.roomRepository

        return room.update(con)
    }
}