package chiogros.etomer.data.remote.sftp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.session.ClientSession
import org.apache.sshd.common.util.io.PathUtils.setUserHomeFolderResolver
import org.apache.sshd.sftp.client.SftpClient
import org.apache.sshd.sftp.client.SftpClientFactory
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Supplier

@RequiresApi(Build.VERSION_CODES.O)
class RemoteSftp(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    val client: SshClient

    init {
        // Set Android's filesystem path
        val path: Supplier<Path> = Supplier { Paths.get("") }
        setUserHomeFolderResolver(path)

        client = SshClient.setUpDefaultClient()
        client.start()
    }

    suspend fun connect(host: String, port: Int, user: String, pwd: String) {
        // Connect to server, execute commands
        var session: ClientSession
        withContext(coroutineDispatcher) {
            session = client.connect(user, host, port)
                .verify()
                .getClientSession()

            session.addPasswordIdentity(pwd)
            session.auth().verify()

            session.executeRemoteCommand("touch worked")

            val factory: SftpClientFactory = SftpClientFactory.instance()
            val sftpClient: SftpClient = factory.createSftpClient(session)

            val content: String =
                sftpClient.read("/home/test/worked").readAllBytes().decodeToString()

            Log.i("Test", content)
        }
        client.stop()
    }
}