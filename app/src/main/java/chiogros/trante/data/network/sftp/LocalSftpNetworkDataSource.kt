package chiogros.trante.data.network.sftp

import chiogros.trante.data.room.sftp.SftpRoom

class LocalSftpNetworkDataSource {
    private val openConnections: MutableMap<String, SftpNetwork> = mutableMapOf()

    fun isStillConnected(con: SftpRoom): Boolean {
        val handler = openConnections[con.id]
        return (handler != null && handler.isConnected)
    }

    fun get(con: SftpRoom): SftpNetwork {
        if (!isStillConnected(con)) {
            throw Exception("")
        }
        val handler = openConnections[con.id]
        if (handler != null) {
            return handler
        } else {
            throw ArrayIndexOutOfBoundsException()
        }
    }

    fun set(con: SftpRoom, handler: SftpNetwork) {
        openConnections[con.id] = handler
    }
}