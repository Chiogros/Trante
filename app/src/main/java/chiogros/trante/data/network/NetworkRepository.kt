package chiogros.trante.data.network

import chiogros.trante.data.room.Connection
import java.io.InputStream

interface NetworkRepository {
    suspend fun createFile(con: Connection, path: String): Boolean
    suspend fun connect(con: Connection): Boolean
    suspend fun getFileStat(con: Connection, path: String): File
    suspend fun listFiles(con: Connection, path: String): List<File>
    suspend fun readFile(con: Connection, path: String): InputStream
}