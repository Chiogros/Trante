package chiogros.cost.data.room.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoUtils {
    val keyId = "keyId"
    var keygen: KeyGenerator = KeyGenerator.getInstance(KEY_GEN_ALG, PROVIDER)
    val cipher: Cipher = Cipher.getInstance(CRYPTO_AEAD)

    companion object {
        const val PROVIDER = "AndroidKeyStore"
        const val KEY_GEN_ALG = KeyProperties.KEY_ALGORITHM_AES
        const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        const val CRYPTO_AEAD: String = "$KEY_GEN_ALG/$BLOCK_MODE/$PADDING"
        const val CRYPTO_AEAD_KEY_SIZE = 32
        const val CRYPTO_AEAD_TAG_SIZE = 16
    }

    init {
        keygen.init(
            KeyGenParameterSpec.Builder(
                keyId,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(PADDING)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .setKeySize(CRYPTO_AEAD_KEY_SIZE * 8)
                .build()
        )
    }

    fun decrypt(encData: EncryptedData): ByteArray {
        return decrypt(
            ciphertext = encData.ciphertext,
            iv = encData.iv
        )
    }

    fun decrypt(ciphertext: ByteArray, iv: ByteArray): ByteArray {
        val keystore = KeyStore.getInstance(PROVIDER).apply { load(null) }

        if (!keystore.containsAlias(keyId)) {
            throw NoSuchElementException("Missing key \"$keyId\" in KeyStore for decryption.")
        }

        val decKey = keystore.getKey(keyId, null)
        cipher.init(
            Cipher.DECRYPT_MODE,
            decKey,
            GCMParameterSpec(8 * CRYPTO_AEAD_TAG_SIZE, iv)
        )

        return cipher.doFinal(ciphertext)
    }

    fun encrypt(plaintext: ByteArray): EncryptedData {
        val encKey: SecretKey = keygen.generateKey()

        cipher.init(Cipher.ENCRYPT_MODE, encKey)

        val res: ByteArray = cipher.doFinal(plaintext)

        return EncryptedData(res, cipher.iv)
    }
}