package chiogros.etomer.data.types

abstract class Connection(open val name: String) {
    var enabled: Boolean = false

    fun getState(): Boolean { return enabled }
    fun setState(state: Boolean) { enabled = state }
}