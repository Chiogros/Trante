package chiogros.trante.protocols.common

/** Connection's data shown to the UI. */
interface CommonConnectionEditFormState {
    /** @see chiogros.trante.data.room.Connection.id */
    val id: String

    /** @see chiogros.trante.data.room.Connection.name */
    var name: String
}