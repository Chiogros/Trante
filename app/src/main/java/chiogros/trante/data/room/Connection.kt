package chiogros.trante.data.room

interface Connection {
    var id: String
    var name: String
    var enabled: Boolean
    var state: ConnectionState

    override fun toString(): String
}