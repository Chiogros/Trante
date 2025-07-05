package chiogros.etomer.data.repositories.remote

import chiogros.etomer.data.room.Connection

abstract class RemoteRepository() {
    abstract suspend fun connect(con: Connection)
}
