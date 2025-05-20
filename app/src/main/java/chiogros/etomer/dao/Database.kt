package chiogros.etomer.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Connection::class], version = 2)
abstract class ConnectionDatabase : RoomDatabase() {

    abstract fun ConnectionDao(): ConnectionDao // Provides DAO instance.

    companion object {

        @Volatile
        private var INSTANCE: ConnectionDatabase? = null // Holds a single instance of the database.

        fun getDatabase(context: Context): ConnectionDatabase {
            if (INSTANCE == null) {
                synchronized(this) { // Ensures thread safety during instance creation.
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ConnectionDatabase::class.java,
                        "ConnectionDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}