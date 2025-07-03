package chiogros.etomer.data.room

abstract class Connection() {
    abstract val id: Long
    abstract var name: String
    abstract var enabled: Boolean
    abstract var host: String
    abstract var user: String

    abstract override fun toString(): String
}