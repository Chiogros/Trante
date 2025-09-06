package chiogros.cost.data.room.crypto

import kotlinx.coroutines.flow.Flow

class CryptoRoomRepository(private val localDataSource: CryptoRoomDataSource) {
    suspend fun delete(con: CryptoRoom) {
        localDataSource.delete(con)
    }

    fun get(id: String): Flow<CryptoRoom> {
        return localDataSource.get(id)
    }

    suspend fun insert(con: CryptoRoom) {
        localDataSource.insert(con)
    }

    suspend fun update(con: CryptoRoom) {
        localDataSource.update(con)
    }
}
