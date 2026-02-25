package chiogros.trante.data.room.crypto

/** Holds ciphered data and metadata associated to an encrypted asset.
 * Class instead of DataClass to avoid overriding [equals] and hashcode().
 */
class EncryptedData(
    var ciphertext: ByteArray = ByteArray(0),
    var iv: ByteArray = ByteArray(0)
)