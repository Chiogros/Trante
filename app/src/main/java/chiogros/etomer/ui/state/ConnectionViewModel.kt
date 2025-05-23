package chiogros.etomer.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionRepository
import chiogros.etomer.data.storage.Connection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConnectionViewModel(private val repository: ConnectionRepository) : ViewModel() {
    val connections: Flow<List<Connection>> = repository.getAll().stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        emptyList()
    )

    fun update(connection: Connection) {
        viewModelScope.launch {
            repository.update(connection)
        }
    }
}
