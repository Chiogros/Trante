package chiogros.cost.data.remote.sftp

import chiogros.cost.data.room.sftp.ConnectionSftp

class LocalSftpDataSource {
    private val openConnections: MutableMap<String, RemoteSftp> = mutableMapOf()

    fun isStillConnected(con: ConnectionSftp): Boolean {
        val handler = openConnections[con.id]
        return (handler != null && handler.isConnected)
    }

    fun get(con: ConnectionSftp): RemoteSftp {
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

    fun set(con: ConnectionSftp, handler: RemoteSftp) {
        openConnections[con.id] = handler
    }
}