package chiogros.trante.data.network

import chiogros.trante.data.room.Connection
import java.io.InputStream

/** Commands to authenticate, fetch, push and handle data to server. */
interface NetworkRepository {
    suspend fun createFile(con: Connection, path: String): Boolean

    /** Initiate connection to remote server and send credentials.
     * @return Successfully connected */
    suspend fun connect(con: Connection): Boolean

    /** @return File's metadata. */
    suspend fun getFileStat(con: Connection, path: String): File
    suspend fun listFiles(con: Connection, path: String): List<File>
    suspend fun readFile(con: Connection, path: String): InputStream
}