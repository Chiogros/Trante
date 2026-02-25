package chiogros.trante.data.room

interface Connection {
    /** Unique ID assigned to every connection. */
    var id: String

    /** _Display_ name for the connection. */
    var name: String

    /** Has user enabled connection?. */
    var enabled: Boolean
    var state: ConnectionState

    /** @return Pretty print of the connection (it's name usually).
     * @see name
     */
    override fun toString(): String
}