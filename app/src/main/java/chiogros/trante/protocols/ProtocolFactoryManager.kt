package chiogros.trante.protocols

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.ftp.FtpFactory
import chiogros.trante.protocols.ftp.data.room.FtpRoom
import chiogros.trante.protocols.sftp.SftpFactory
import chiogros.trante.protocols.sftp.data.room.SftpRoom

/** Allows each object to be associated to a protocol. */
enum class Protocol {
    FTP,
    SFTP
}

/**
 * @return Factory corresponding to the object's protocol.
 * @see ProtocolFactory
 * @see Protocol
 */
class ProtocolFactoryManager(
    val sftpFactory: SftpFactory,
    val ftpFactory: FtpFactory
) {
    fun getFactory(protocol: Protocol): ProtocolFactory {
        return when (protocol) {
            Protocol.SFTP -> sftpFactory
            Protocol.FTP  -> ftpFactory
        }
    }

    fun getFactory(con: Connection): ProtocolFactory {
        return getFactory(
            when (con) {
                is SftpRoom -> Protocol.SFTP
                is FtpRoom  -> Protocol.FTP
                else        -> throw TypeNotPresentException(con.toString(), null)
            }
        )
    }
}