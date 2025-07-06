package chiogros.etomer.data.remote.sftp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.session.ClientSession
import org.apache.sshd.common.util.io.PathUtils.setUserHomeFolderResolver
import org.apache.sshd.sftp.client.SftpClient
import org.apache.sshd.sftp.client.SftpClientFactory
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Supplier

class RemoteSftp(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    val client: SshClient
    lateinit var sftpClient: SftpClient

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

            val factory: SftpClientFactory = SftpClientFactory.instance()
            sftpClient = factory.createSftpClient(session)
        }
    }

    suspend fun listFiles(path: String) {

    }

    suspend fun readFile(path: String): ByteArray {
        val canonicalPath = sftpClient.canonicalPath(path)

        var content: ByteArray
        try {
            content = sftpClient.read(canonicalPath).readAllBytes()
        } catch (e: IOException) {
            content = ByteArray(0)
        }
        return content
    }
}