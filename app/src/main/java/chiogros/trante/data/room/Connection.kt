package chiogros.trante.data.room

import chiogros.trante.data.room.crypto.EncryptedData

abstract class Connection {
    abstract val id: String
    abstract var name: String
    abstract var enabled: Boolean
    abstract var host: String
    abstract var user: String
    abstract var state: ConnectionState
    abstract var password: EncryptedData

    // Get the string name of the protocol: "SFTP", "FTP", ...
    abstract override fun toString(): String
}