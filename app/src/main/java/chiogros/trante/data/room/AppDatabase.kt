package chiogros.trante.data.room

import android.content.Context
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import chiogros.trante.protocols.ftp.data.room.FtpRoom
import chiogros.trante.protocols.ftp.data.room.FtpRoomDao
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import chiogros.trante.protocols.sftp.data.room.SftpRoomDao

@Database(
    entities = [SftpRoom::class, FtpRoom::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun connectionSftpDao(): SftpRoomDao
    abstract fun connectionFtpDao(): FtpRoomDao

    companion object {
        @Volatile
        // Holds a single instance of the database.
        private var db: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (db == null) {
                init(context)
            }

            // !! since it got initialized, or it raised an exception
            return db!!
        }

        private fun init(context: Context) {
            val appName = context.packageName.split('.').last().capitalize(Locale("en-GB"))

            // Ensures thread safety during instance creation.
            synchronized(this) {
                db = Room.databaseBuilder(context, AppDatabase::class.java, appName).build()
            }
        }
    }
}