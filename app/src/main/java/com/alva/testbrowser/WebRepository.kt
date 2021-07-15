package com.alva.testbrowser

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

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class BookmarkDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null


        @Synchronized
        fun getDatabase(context: Context): BookmarkDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    BookmarkDatabase::class.java,
                    "bookmark_database"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }

    abstract fun bookmarkDao(): BookmarkDao
}

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null


        @Synchronized
        fun getDatabase(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    HistoryDatabase::class.java,
                    "history_database"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }

    abstract fun historyDao(): HistoryDao
}

class BookmarkRepository(context: Context) {
    private val bookmarkDatabase = BookmarkDatabase.getDatabase(context.applicationContext)
    private val bookmarkDao = bookmarkDatabase.bookmarkDao()

    val allWebsLive: LiveData<List<Bookmark>> = bookmarkDao.getAllWebsLive()

    suspend fun insertWebs(webs: Bookmark) {
        bookmarkDao.insertWebs(webs)
    }

    suspend fun updateWebs(webs: Bookmark) {
        bookmarkDao.updateWebs(webs)
    }

    suspend fun deleteWebs(webs: Bookmark) {
        bookmarkDao.deleteWebs(webs)
    }

    suspend fun deleteAllWebs() {
        bookmarkDao.deleteAllWebs()
    }
}

class HistoryRepository(context: Context) {
    private val historyDatabase = HistoryDatabase.getDatabase(context.applicationContext)
    private val historyDao = historyDatabase.historyDao()

    val allWebsLive: LiveData<List<History>> = historyDao.getAllWebsLive()

    suspend fun insertWebs(webs: History) {
        historyDao.insertWebs(webs)
    }

    suspend fun deleteWebs(webs: History) {
        historyDao.deleteWebs(webs)
    }

    suspend fun deleteAllWebs() {
        historyDao.deleteAllWebs()
    }
}