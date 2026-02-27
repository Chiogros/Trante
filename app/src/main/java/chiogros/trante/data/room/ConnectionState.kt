package chiogros.trante.data.room

/** State of a connection. It may change several times during its lifetime. */
enum class ConnectionState {
    CONNECTED,
    CONNECTING,
    FAILED,
    NEVER_USED
}