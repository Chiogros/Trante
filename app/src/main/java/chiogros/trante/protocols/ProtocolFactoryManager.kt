package chiogros.trante.protocols

import chiogros.trante.protocols.sftp.SftpFactory

enum class Protocol {
    SFTP
}

class ProtocolFactoryManager(
    val sftpFactory: SftpFactory
) {
    fun getFactory(protocol: Protocol): ProtocolFactory {
        return when (protocol) {
            Protocol.SFTP -> sftpFactory
        }
    }
}