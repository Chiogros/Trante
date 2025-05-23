package chiogros.etomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import chiogros.etomer.data.datasource.ConnectionRoomDataSource
import chiogros.etomer.data.repositories.ConnectionRepository
import chiogros.etomer.data.storage.AppDatabase
import chiogros.etomer.ui.state.ConnectionViewModel
import chiogros.etomer.ui.ui.theme.EtomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectionViewModel = ConnectionViewModel(
            ConnectionRepository(
                ConnectionRoomDataSource(
                    AppDatabase.getDatabase(this).ConnectionDao()
                )
            )
        )

        enableEdgeToEdge()
        setContent {
            EtomerTheme {
                Etomer(connectionViewModel)
            }
        }
    }
}