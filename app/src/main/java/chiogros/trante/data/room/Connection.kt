package chiogros.trante.data.room

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
open class Connection {
    open var id: String = Uuid.random().toString()
    open var name: String = String()
    open var enabled: Boolean = false
    open var state: ConnectionState = ConnectionState.NEVER_USED

    override fun toString(): String {
        return name
    }
}