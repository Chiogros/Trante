package chiogros.etomer.data.remote

import java.nio.file.Path
import kotlin.io.path.Path

enum class FileAttributesType {
    REGULAR, DIRECTORY, SYMLINK, UNKNOWN
}

class File(val path: Path) {
    var parent: Path = Path("")
    var size: Long = 0
    var type: FileAttributesType = FileAttributesType.REGULAR
    var content: ByteArray = ByteArray(0)
}