package chiogros.cost.data.room.crypto

import androidx.room.TypeConverter
import kotlin.io.encoding.Base64

class EncryptedDataTypeConverter {
    // To split ciphertext from IV in database
    val delimiter = ':'

    @TypeConverter
    fun read(value: String): EncryptedData {
        val (ciphertextB64, ivB64) = value.split(delimiter)

        val ciphertext = Base64.decode(ciphertextB64)
        val iv = Base64.decode(ivB64)

        return EncryptedData(
            ciphertext = ciphertext,
            iv = iv
        )
    }

    @TypeConverter
    fun write(value: EncryptedData): String {
        val ciphertextB64 = Base64.encode(value.ciphertext)
        val ivB64 = Base64.encode(value.iv)

        return "$ciphertextB64$delimiter$ivB64"
    }
}