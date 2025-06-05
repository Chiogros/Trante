package chiogros.etomer.data.storage

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Entity
import androidx.room.PrimaryKey
import chiogros.etomer.R

@Entity
data class ConnectionSftp(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String = "",
    var enabled: Boolean = false,
    var host: String = "",
    var user: String = "",
) {
    companion object {
        @Composable
        fun asString(): String {
            return stringResource(R.string.sftp)
        }
    }
}