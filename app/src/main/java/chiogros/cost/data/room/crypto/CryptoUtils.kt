package chiogros.cost.data.room.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoUtils {
    val keyId = "salut"
    var keygen: KeyGenerator = KeyGenerator.getInstance(keyGenAlg, provider)
    val cipher = Cipher.getInstance(cipherTransformation)

    companion object {
        val provider = "AndroidKeyStore"
        val keyGenAlg = KeyProperties.KEY_ALGORITHM_AES
        val cipherTransformation = "AES/GCM/NoPadding"
        val CRYPTO_AEAD: String = "AES/GCM/NoPadding"
        val CRYPTO_AEAD_KEY_SIZE = 32
        val CRYPTO_AEAD_TAG_SIZE = 16
    }

    init {
        keygen.init(
            KeyGenParameterSpec.Builder(
                keyId,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .setKeySize(CRYPTO_AEAD_KEY_SIZE * 8)
                .build()
        )

        /*
        val sec: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance(cipherTransformation)
        cipher.init(Cipher.ENCRYPT_MODE, sec)

        val result: ByteArray = cipher.doFinal(data)
        val tag: ByteArray =
            result.copyOfRange(result.size - CRYPTO_AEAD_TAG_SIZE, result.size);
        val encrypted: ByteArray =
            result.copyOfRange(0, result.size - CRYPTO_AEAD_TAG_SIZE)
        */
    }

    fun decrypt(encData: EncryptedData): ByteArray {
        return decrypt(
            ciphertext = encData.ciphertext,
            iv = encData.iv
        )
    }

    fun decrypt(ciphertext: ByteArray, iv: ByteArray): ByteArray {
        val keystore = KeyStore.getInstance(provider).apply { load(null) }

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

    fun getEncryptedObj(ciphertext: ByteArray, iv: ByteArray): EncryptedData {
        return EncryptedData(
            ciphertext = ciphertext,
            iv = iv
        )
    }

    fun encrypt(plaintext: ByteArray): EncryptedData {
        val encKey: SecretKey = keygen.generateKey()

        cipher.init(Cipher.ENCRYPT_MODE, encKey)

        val res: ByteArray = cipher.doFinal(plaintext)
        val tag = res.copyOfRange(res.size - CRYPTO_AEAD_TAG_SIZE, res.size)
        val ciphertext = res.copyOfRange(0, res.size - CRYPTO_AEAD_TAG_SIZE)

        return EncryptedData(res, cipher.iv)
    }
}