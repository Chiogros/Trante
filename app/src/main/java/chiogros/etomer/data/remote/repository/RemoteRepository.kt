package chiogros.etomer.data.remote.repository

import chiogros.etomer.data.remote.File
import chiogros.etomer.data.room.Connection

abstract class RemoteRepository() {
    abstract suspend fun connect(con: Connection): Boolean
    abstract suspend fun listFiles(path: String): List<File>
    abstract suspend fun readFile(path: String): ByteArray
}
