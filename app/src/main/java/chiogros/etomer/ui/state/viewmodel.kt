package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import chiogros.etomer.data.storage.Connection
import chiogros.etomer.data.storage.ConnectionDatabase
import kotlinx.coroutines.flow.Flow

class MyViewModel(db: ConnectionDatabase) : ViewModel() {
    private val connectionDao = db.ConnectionDao()
    val allEntities: Flow<List<Connection>> = connectionDao.getAll()
}
