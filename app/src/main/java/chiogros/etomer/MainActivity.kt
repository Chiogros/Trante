package chiogros.etomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.room.Room
import chiogros.etomer.dao.ConnectionDatabase
import chiogros.etomer.dao.MyViewModel
import chiogros.etomer.ui.theme.EtomerTheme

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