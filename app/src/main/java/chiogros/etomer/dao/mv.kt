package chiogros.etomer.dao

import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

class MyViewModel(db: ConnectionDatabase) : ViewModel() {
    private val connectionDao = db.ConnectionDao()
    val allEntities: Flow<List<Connection>> = connectionDao.getAll()
}
