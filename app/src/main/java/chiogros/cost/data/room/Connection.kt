package chiogros.cost.data.room

abstract class Connection() {
    abstract val id: String
    abstract var name: String
    abstract var enabled: Boolean
    abstract var host: String
    abstract var user: String
    abstract var state: ConnectionState

    // Get the string name of the protocol: "SFTP", "FTP", ...
    abstract override fun toString(): String
}