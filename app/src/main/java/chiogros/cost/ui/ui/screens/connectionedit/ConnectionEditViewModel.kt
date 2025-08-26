package chiogros.cost.ui.ui.screens.connectionedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.ConnectionState
import chiogros.cost.data.room.repository.ConnectionManager
import chiogros.cost.data.room.sftp.ConnectionSftp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ConnectionEditFormState(
    val id: String = "",
    val host: String = "",
    val name: String = "",
    val type: String = ConnectionSftp.toString(),
    val user: String = "",
    val password: String = ""
)

data class ConnectionEditUiState(
    val formState: ConnectionEditFormState = ConnectionEditFormState(),
    // Holds initial form data, useful to check for changes
    val originalFormState: ConnectionEditFormState = ConnectionEditFormState(),
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,
    var deletedConnection: Connection = ConnectionSftp()
) {
    val isEdited: Boolean
        get() = formState != originalFormState
}

class ConnectionEditViewModel(private val repository: ConnectionManager) : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionEditUiState())
    val uiState: StateFlow<ConnectionEditUiState> = _uiState.asStateFlow()

    fun delete() {
        save()
        viewModelScope.launch {
            repository.delete(ConnectionSftp(id = uiState.value.formState.id))
        }
    }

    fun initFrom(id: String) {
        viewModelScope.launch {
            // Fill out the form with connection data
            val con: Connection = repository.get(id).first()
            val conForm = ConnectionEditFormState(
                id = con.id,
                host = con.host,
                name = con.name,
                type = con.toString(),
                user = con.user,
                password = con.password
            )

            refresh()
            _uiState.update {
                it.copy(
                    formState = conForm, originalFormState = conForm, isEditing = true
                )
            }
        }
    }

    fun insert() {
        /*
        val keyId = "salut"
        val provider = "AndroidKeyStore"
        val keyGenAlg = "AES"
        val cipherTransformation = "AES/GCM/NoPadding"
        val plaintext: ByteArray = uiState.value.formState.password.toByteArray()

        val keygen = KeyGenerator.getInstance(keyGenAlg, provider)
        keygen.init(KeyGenParameterSpec.Builder(keyId,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .setKeySize(32 * 8)
                .build())
        val encKey: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance(cipherTransformation)
        cipher.init(Cipher.ENCRYPT_MODE, encKey)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv

        val keystore = KeyStore.getInstance(provider).apply { load(null) }
        var cleartext: ByteArray
        if (keystore.containsAlias(keyId)) {
            val decKey = keystore.getKey(keyId, null)
            cipher.init(Cipher.DECRYPT_MODE, decKey, GCMParameterSpec( 8 * 16 , iv))
            cleartext = cipher.doFinal(ciphertext)
        }
         */

        viewModelScope.launch {
            repository.insert(
                when (uiState.value.formState.type) {
                    ConnectionSftp.toString() -> ConnectionSftp(
                        host = uiState.value.formState.host,
                        name = uiState.value.formState.name,
                        user = uiState.value.formState.user,
                        password = uiState.value.formState.password
                    )

                    else -> error("Type ${uiState.value.formState.type} unknown!")
                }
            )
        }
    }

    // Initialize states
    fun refresh() {
        _uiState.update {
            it.copy(
                formState = ConnectionEditFormState(),
                originalFormState = ConnectionEditFormState(),
                isEditing = false,
                isDialogShown = false,
                deletedConnection = ConnectionSftp()
            )
        }
    }

    // Restore the last deleted connection
    fun restore() {
        _uiState.update {
            it.copy(
                formState = ConnectionEditFormState(
                    id = uiState.value.deletedConnection.id,
                    host = uiState.value.deletedConnection.host,
                    name = uiState.value.deletedConnection.name,
                    user = uiState.value.deletedConnection.user,
                    password = uiState.value.deletedConnection.password
                )
            )
        }
        insert()
    }

    // Backup the deleted connection, useful in case of restore()
    fun save() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    deletedConnection = repository.get(uiState.value.formState.id).first()
                )
            }
        }
    }

    fun setIsDialogShown(state: Boolean) {
        _uiState.update {
            it.copy(
                isDialogShown = state
            )
        }
    }

    fun setHost(host: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(host = host)
            )
        }
    }

    fun setName(name: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(name = name)
            )
        }
    }

    fun setPassword(password: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(password = password)
            )
        }
    }

    fun setType(type: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(type = type)
            )
        }
    }

    fun setUser(user: String) {
        _uiState.update {
            it.copy(
                formState = uiState.value.formState.copy(user = user)
            )
        }
    }

    fun update() {
        viewModelScope.launch {
            val con: Connection = repository.get(uiState.value.formState.id).first()
            con.host = uiState.value.formState.host
            con.name = uiState.value.formState.name
            con.user = uiState.value.formState.user
            con.password = uiState.value.formState.password
            con.state = ConnectionState.NEVER_USED
            con.enabled = false
            repository.update(con)
        }
    }
}
