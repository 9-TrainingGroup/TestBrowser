package com.alva.testbrowser.test

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {
    @Insert
    suspend fun insertWebs(vararg webs: Bookmark)

    @Update
    suspend fun updateWebs(vararg webs: Bookmark)

    @Delete
    suspend fun deleteWebs(vararg webs: Bookmark)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllWebs()

    @Query("SELECT * FROM bookmark ORDER BY id DESC")
    fun getAllWebsLive(): LiveData<List<Bookmark>>
}

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertWebs(vararg webs: History)

    @Delete
    suspend fun deleteWebs(vararg webs: History)

    @Query("DELETE FROM history")
    suspend fun deleteAllWebs()

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllWebsLive(): LiveData<List<History>>
}

@Database(entities = [Bookmark::class, History::class], version = 1, exportSchema = false)
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

    val allBookmark: LiveData<List<Bookmark>> = bookmarkDao.getAllWebsLive()

    val allHistory: LiveData<List<History>> = historyDao.getAllWebsLive()

    suspend fun insertWebs(webs: Bookmark) {
        bookmarkDao.insertWebs(webs)
    }

    suspend fun insertWebs(webs: History) {
        historyDao.insertWebs(webs)
    }

    suspend fun updateWebs(webs: Bookmark) {
        bookmarkDao.updateWebs(webs)
    }

    suspend fun deleteWebs(webs: Bookmark) {
        bookmarkDao.deleteWebs(webs)
    }

    suspend fun deleteWebs(webs: History) {
        historyDao.deleteWebs(webs)
    }

    suspend fun deleteAllBookmark() {
        bookmarkDao.deleteAllWebs()
    }

    suspend fun deleteAllHistory() {
        historyDao.deleteAllWebs()
    }
}