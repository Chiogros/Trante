package chiogros.trante.data.network

import java.nio.file.Path

enum class FileAttributesType {
    REGULAR, DIRECTORY, SYMLINK, UNKNOWN
}

class File(val path: Path) {
    var size: Long = 0
    var type: FileAttributesType = FileAttributesType.REGULAR
}