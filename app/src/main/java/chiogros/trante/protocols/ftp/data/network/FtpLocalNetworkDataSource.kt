package chiogros.trante.protocols.ftp.data.network

import chiogros.trante.protocols.ftp.data.room.FtpRoom

class FtpLocalNetworkDataSource {
    private val openConnections: MutableMap<String, FtpNetwork> = mutableMapOf()

    fun isStillConnected(con: FtpRoom): Boolean {
        val handler = openConnections[con.id]
        return (handler != null && handler.isConnected)
    }

    fun get(con: FtpRoom): FtpNetwork {
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

    fun set(con: FtpRoom, handler: FtpNetwork) {
        openConnections[con.id] = handler
    }
}