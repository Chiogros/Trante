package chiogros.trante.protocols

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.sftp.SftpFactory
import chiogros.trante.protocols.sftp.data.room.SftpRoom

enum class Protocol {
    SFTP,
    UNKNOWN
}

class ProtocolFactoryManager(
    val sftpFactory: SftpFactory
) {
    fun getFactory(protocol: Protocol): ProtocolFactory {
        return when (protocol) {
            Protocol.SFTP -> sftpFactory
            else          -> error("Unsupported connection type!")
        }
    }

    fun getFactory(con: Connection): ProtocolFactory {
        return getFactory(
            when (con) {
                is SftpRoom -> Protocol.SFTP
                else        -> Protocol.UNKNOWN
            }
        )
    }
}