package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.protocols.ProtocolFactoryManager

class EnableConnectionUseCase(
    private val protocolFactoryManager: ProtocolFactoryManager,
    private val notifyContentResolverUseCase: NotifyContentResolverUseCase
) {
    suspend operator fun invoke(con: Connection) {
        val factory = protocolFactoryManager.getFactory(con)
        val room = factory.roomRepository
        val network = factory.networkRepository

        // Save ongoing action is room
        con.enabled = true
        con.state = ConnectionState.CONNECTING
        room.update(con)

        // Try to connect and update state accordingly
        if (network.connect(con)) {
            con.state = ConnectionState.CONNECTED
        } else {
            con.state = ConnectionState.FAILED
        }
        room.update(con)

        notifyContentResolverUseCase()
    }
}