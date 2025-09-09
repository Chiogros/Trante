package chiogros.trante.data.room.crypto

// Class instead of DataClass to avoid
// overriding equals() and hashcode(), which is recommended when using DataClasses.
class EncryptedData(
    var ciphertext: ByteArray = ByteArray(0),
    var iv: ByteArray = ByteArray(0)
)