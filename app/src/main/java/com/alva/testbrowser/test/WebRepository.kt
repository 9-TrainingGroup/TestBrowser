package com.alva.testbrowser.test

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {
    @Insert
    suspend fun insertWebs(vararg webs: Bookmarktest)

    @Update
    suspend fun updateWebs(vararg webs: Bookmarktest)

    @Delete
    suspend fun deleteWebs(vararg webs: Bookmarktest)

    @Query("DELETE FROM bookmarktest")
    suspend fun deleteAllWebs()

    @Query("SELECT * FROM bookmarktest ORDER BY id DESC")
    fun getAllWebsLive(): LiveData<List<Bookmarktest>>
}

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertWebs(vararg webs: Historytest)

    @Delete
    suspend fun deleteWebs(vararg webs: Historytest)

    @Query("DELETE FROM historytest")
    suspend fun deleteAllWebs()

    @Query("SELECT * FROM historytest ORDER BY id DESC")
    fun getAllWebsLive(): LiveData<List<Historytest>>
}

@Database(entities = [Bookmarktest::class, Historytest::class], version = 1, exportSchema = false)
abstract class WebsDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: WebsDatabase? = null


        @Synchronized
        fun getDatabase(context: Context): WebsDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    WebsDatabase::class.java,
                    "bookmark_database"
                ).build()
            }
            return INSTANCE!!
        }
    }

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
}

class WebsRepository(context: Context) {
    private val websDatabase = WebsDatabase.getDatabase(context.applicationContext)
    private val bookmarkDao = websDatabase.bookmarkDao()
    private val historyDao = websDatabase.historyDao()

    val allBookmark: LiveData<List<Bookmarktest>> = bookmarkDao.getAllWebsLive()

    val allHistory: LiveData<List<Historytest>> = historyDao.getAllWebsLive()

    suspend fun insertWebs(webs: Bookmarktest) {
        bookmarkDao.insertWebs(webs)
    }

    suspend fun insertWebs(webs: Historytest) {
        historyDao.insertWebs(webs)
    }

    suspend fun updateWebs(webs: Bookmarktest) {
        bookmarkDao.updateWebs(webs)
    }

    suspend fun deleteWebs(webs: Bookmarktest) {
        bookmarkDao.deleteWebs(webs)
    }

    suspend fun deleteWebs(webs: Historytest) {
        historyDao.deleteWebs(webs)
    }

    suspend fun deleteAllBookmark() {
        bookmarkDao.deleteAllWebs()
    }

    suspend fun deleteAllHistory() {
        historyDao.deleteAllWebs()
    }
}