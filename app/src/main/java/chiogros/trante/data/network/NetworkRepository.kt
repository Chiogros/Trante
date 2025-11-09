package chiogros.trante.data.network

import chiogros.trante.data.room.Connection
import java.io.InputStream

abstract class NetworkRepository() {
    abstract suspend fun createFile(con: Connection, path: String): Boolean
    abstract suspend fun connect(con: Connection): Boolean
    abstract suspend fun getFileStat(con: Connection, path: String): File
    abstract suspend fun listFiles(con: Connection, path: String): List<File>
    abstract suspend fun readFile(con: Connection, path: String): InputStream
}