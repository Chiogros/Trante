package chiogros.etomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import chiogros.etomer.data.storage.ConnectionDatabase
import chiogros.etomer.ui.state.MyViewModel
import chiogros.etomer.ui.ui.theme.EtomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mv = MyViewModel(ConnectionDatabase.getDatabase(this))

        enableEdgeToEdge()
        setContent {
            EtomerTheme {
                Etomer(mv)
            }
        }
    }
}