package chiogros.etomer.domain

import android.os.Build
import androidx.annotation.RequiresApi
import chiogros.etomer.data.repositories.remote.RemoteRepository
import chiogros.etomer.data.repositories.room.ConnectionManager
import kotlinx.coroutines.flow.first

class EnableConnectionUseCase(
    private val repository: ConnectionManager,
    private val remote: RemoteRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(id: String) {
        val con = repository.get(id).first()
        con.enabled = true
        repository.update(con)

        remote.connect(con)
    }
}