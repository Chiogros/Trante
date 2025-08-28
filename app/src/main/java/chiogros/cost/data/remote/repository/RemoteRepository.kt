package chiogros.cost.data.remote.repository

import chiogros.cost.data.remote.File
import chiogros.cost.data.room.Connection
import java.io.InputStream

abstract class RemoteRepository() {
    abstract suspend fun connect(con: Connection): Boolean
    abstract suspend fun getFileStat(path: String): File
    abstract suspend fun listFiles(path: String): List<File>
    abstract suspend fun readFile(path: String): InputStream
}
