package com.alva.testbrowser.test

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WebDao {
    @Insert(entity = Bookmarks::class)
    suspend fun insertWebs(vararg webs: Bookmarks)

    @Insert(entity = Histories::class)
    suspend fun insertWebs(vararg webs: Histories)

    @Update(entity = Bookmarks::class)
    suspend fun updateWebs(vararg webs: Bookmarks)

    @Delete(entity = Bookmarks::class)
    suspend fun deleteWebs(vararg webs: Bookmarks)

    @Delete(entity = Histories::class)
    suspend fun deleteWebs(vararg webs: Histories)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks()

    @Query("DELETE FROM histories")
    suspend fun deleteAllHistories()

    @Query("SELECT * FROM bookmarks ORDER BY id DESC")
    fun getAllBookmarks(): LiveData<List<Bookmarks>>

    @Query("SELECT * FROM histories ORDER BY id DESC")
    fun getAllHistories(): LiveData<List<Histories>>
}

@Database(entities = [Bookmarks::class, Histories::class], version = 1, exportSchema = false)
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

    abstract fun webDao(): WebDao
}

class WebsRepository(context: Context) {
    private val websDatabase = WebsDatabase.getDatabase(context.applicationContext)
    private val webDao = websDatabase.webDao()

    val allBookmark: LiveData<List<Bookmarks>> = webDao.getAllBookmarks()

    val allHistory: LiveData<List<Histories>> = webDao.getAllHistories()

    suspend fun insertWebs(webs: Bookmarks) {
        webDao.insertWebs(webs)
    }

    suspend fun insertWebs(webs: Histories) {
        webDao.insertWebs(webs)
    }

    suspend fun updateWebs(webs: Bookmarks) {
        webDao.updateWebs(webs)
    }

    suspend fun deleteWebs(webs: Bookmarks) {
        webDao.deleteWebs(webs)
    }

    suspend fun deleteWebs(webs: Histories) {
        webDao.deleteWebs(webs)
    }

    suspend fun deleteAllBookmark() {
        webDao.deleteAllBookmarks()
    }

    suspend fun deleteAllHistory() {
        webDao.deleteAllHistories()
    }
}