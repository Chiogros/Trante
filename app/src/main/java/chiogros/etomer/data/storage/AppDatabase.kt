package chiogros.etomer.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import chiogros.etomer.R

@Database(entities = [ConnectionSftp::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ConnectionSftpDao(): ConnectionSftpDao

    companion object {

        @Volatile
        private var db: AppDatabase? = null // Holds a single instance of the database.

        fun getDatabase(context: Context): AppDatabase {
            if (db == null) {
                synchronized(this) { // Ensures thread safety during instance creation.
                    db = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        context.getString(R.string.app_name)
                    ).build()
                }
            }
            return db!!
        }
    }
}