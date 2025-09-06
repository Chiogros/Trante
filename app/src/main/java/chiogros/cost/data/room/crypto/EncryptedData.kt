package chiogros.cost.data.room.crypto

class EncryptedData(
    var ciphertext: ByteArray = ByteArray(0),
    var iv: ByteArray = ByteArray(0)
)