package chiogros.etomer.ui.state

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.etomer.data.repositories.ConnectionManager
import chiogros.etomer.data.room.Connection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.session.ClientSession
import org.apache.sshd.common.util.io.PathUtils.setUserHomeFolderResolver
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Supplier

data class ConnectionListUiState(
    val connections: StateFlow<List<Connection>>, val isConnectionDeleted: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
class ConnectionListViewModel(private val repository: ConnectionManager) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ConnectionListUiState(
            connections = repository.getAll().stateIn(
                viewModelScope, WhileSubscribed(5000), emptyList()
            )
        )
    )
    val uiState: StateFlow<ConnectionListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Set Android's filesystem path
            val path: Supplier<Path> = Supplier { Paths.get("/").normalize() }
            setUserHomeFolderResolver(path)

            // Create a client handler
            var client: SshClient = SshClient.setUpDefaultClient()
            client.start()

            // Connect to server, execute commands
            var session: ClientSession = client.connect("test", "192.168.1.32", 22)
                .verify()
                .getClientSession()
            session.addPasswordIdentity("testtest")
            session.auth().verify()
            session.executeRemoteCommand("touch worked")

            client.stop()
        }
    }

    fun delete(connection: Connection) {
        viewModelScope.launch {
            repository.delete(connection)
        }
    }

    fun insert(connection: Connection) {
        viewModelScope.launch {
            repository.insert(connection)
        }
    }

    fun update(connection: Connection) {
        viewModelScope.launch {
            repository.update(connection)

            // This is a horrible workaround to trigger recomposition
            _uiState.update {
                it.copy(
                    connections = repository.getAll().stateIn(
                        viewModelScope, WhileSubscribed(5000), emptyList()
                    )
                )
            }
        }
    }

    fun toggle(connection: Connection) {
        connection.enabled = !connection.enabled
        update(connection)
    }
}
