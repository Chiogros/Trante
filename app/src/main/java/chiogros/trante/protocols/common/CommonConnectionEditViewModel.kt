package chiogros.trante.protocols.common

/** Common text fields and methods that should be provided by protocols.
 * In addition to common fields (such as [setName]), each protocol may provide
 * fields specific to its needs.
 * */
interface CommonConnectionEditViewModel {
    /** @see chiogros.trante.data.room.Connection.name */
    fun setName(name: String)

    /** @return Filled (and empty) fields are statically valid (no network check). */
    fun verify(): Boolean
}
