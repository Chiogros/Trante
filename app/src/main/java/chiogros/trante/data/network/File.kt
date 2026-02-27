package chiogros.trante.data.network

import java.nio.file.Path

/** Type of file. */
enum class FileAttributesType {
    REGULAR, DIRECTORY, SYMLINK, UNKNOWN
}

/** Abstract file representation. */
class File(val path: Path) {
    var size: Long = 0
    var type: FileAttributesType = FileAttributesType.REGULAR
}