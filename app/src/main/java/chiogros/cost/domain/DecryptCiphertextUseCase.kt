package chiogros.cost.domain

class DecryptCiphertextUseCase {
    suspend operator fun invoke(cipher: String, iv: String): ByteArray {
        return ByteArray(0)
    }
}