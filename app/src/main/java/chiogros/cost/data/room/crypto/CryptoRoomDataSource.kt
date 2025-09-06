package chiogros.cost.data.room.crypto

import kotlinx.coroutines.flow.Flow

class CryptoRoomDataSource(private val dao: CryptoRoomDao) {
    suspend fun delete(con: CryptoRoom) {
        dao.delete(con)
    }

    fun get(id: String): Flow<CryptoRoom> {
        return dao.get(id)
    }

    suspend fun insert(con: CryptoRoom) {
        dao.insert(con)
    }

    suspend fun update(con: CryptoRoom) {
        dao.update(con)
    }

}