package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetEnabledConnectionsUseCase(private val protocolFactoryManager: ProtocolFactoryManager) {
    operator fun invoke(): List<Connection> {
        val connections: MutableList<Connection> = mutableListOf()

        Protocol.entries.filter { protocol -> protocol != Protocol.UNKNOWN }.forEach { protocol ->
            val factory = protocolFactoryManager.getFactory(protocol)
            val room = factory.roomRepository

            runBlocking {
                connections.addAll(room.getAll().first())
            }
        }

        return connections.filter { it.enabled }
    }
}