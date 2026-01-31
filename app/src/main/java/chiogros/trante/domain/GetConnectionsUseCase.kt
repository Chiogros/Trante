package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.ProtocolFactoryManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class GetConnectionsUseCase(private val protocolFactoryManager: ProtocolFactoryManager) {
    operator fun invoke(): Flow<Pair<Protocol, List<Connection>>> {
        var allConnections = emptyFlow<Pair<Protocol, List<Connection>>>()

        Protocol.entries.filter { protocol -> protocol != Protocol.UNKNOWN }.forEach { protocol ->
            val factory = protocolFactoryManager.getFactory(protocol)
            val room = factory.roomRepository

            val connections = room.getAll().map { cons -> Pair(protocol, cons) }
            allConnections = merge(allConnections, connections)
        }

        return allConnections
    }
}