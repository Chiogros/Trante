package chiogros.cost.data.remote.sftp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.session.ClientSession
import org.apache.sshd.common.util.io.PathUtils.setUserHomeFolderResolver
import org.apache.sshd.sftp.client.SftpClient
import org.apache.sshd.sftp.client.SftpClientFactory
import java.io.IOException
import java.io.InputStream
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

    suspend fun connect(host: String, port: Int, user: String, pwd: String): Boolean {
        var session: ClientSession? = null

        withContext(coroutineDispatcher) {
            try {
                // Connect to server
                session = client.connect(user, host, port)
                    .verify()
                    .clientSession

                session.addPasswordIdentity(pwd)

                if (session.auth().verify().isSuccess) {
                    val factory: SftpClientFactory = SftpClientFactory.instance()
                    sftpClient = factory.createSftpClient(session)
                } else {
                    session = null
                }

            } catch (_: IOException) {
                session = null
            }
        }

        return (session != null)
    }

    suspend fun getFileStat(path: String): SftpClient.Attributes =
        withContext(coroutineDispatcher) {
            sftpClient.stat(sftpClient.canonicalPath(path))
        }

    suspend fun listFiles(path: String): Iterable<SftpClient.DirEntry> =
        withContext(coroutineDispatcher) {
            sftpClient.readEntries(sftpClient.canonicalPath(path))
        }

    suspend fun readFile(path: String): InputStream {
        var content: InputStream

        withContext(coroutineDispatcher) {
            var canonicalPath: String

            canonicalPath = sftpClient.canonicalPath(path)
            sftpClient.open(canonicalPath)

            content = sftpClient.read(canonicalPath)
        }

        return content
    }
}