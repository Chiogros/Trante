package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.ProtocolFactoryManager

class DisableConnectionUseCase(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val notifyContentResolverUseCase: NotifyContentResolverUseCase
) {
    suspend operator fun invoke(con: Connection) {
        val factory = protocolFactoryManager.getFactory(con)
        val room = factory.roomRepository

        con.enabled = false
        room.update(con)

        notifyContentResolverUseCase()
    }
}