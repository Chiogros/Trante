package chiogros.trante.protocols.common

interface CommonConnectionEditFormState {
    val id: String
    var name: String
    fun clone(): CommonConnectionEditFormState
}