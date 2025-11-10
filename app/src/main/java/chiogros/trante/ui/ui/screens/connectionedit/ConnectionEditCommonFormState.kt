package chiogros.trante.ui.ui.screens.connectionedit

open class ConnectionEditCommonFormState(
    open val id: String = String(),
    open var name: String = String()
) {
    open fun copy(id: String = this.id, name: String = this.name): ConnectionEditCommonFormState {
        return ConnectionEditCommonFormState(id = id, name = name)
    }
}