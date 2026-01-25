package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.Flow

class GetConnectionUseCase(private val protocolFactoryManager: ProtocolFactoryManager) {
    operator fun invoke(con: Connection): Flow<Connection> {
        val factory = protocolFactoryManager.getFactory(con)
        val room = factory.roomRepository

        return room.get(con.id)
    }
}