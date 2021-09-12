package com.alva.testbrowser.test

import androidx.room.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
    fun getAllBookmarks(): Flow<List<Bookmarks>>

    @Query("SELECT * FROM histories ORDER BY id DESC")
    fun getAllHistories(): Flow<List<Histories>>
}

@Database(entities = [Bookmarks::class, Histories::class], version = 1, exportSchema = false)
abstract class WebsDatabase : RoomDatabase() {
    abstract fun webDao(): WebDao
}

@ViewModelScoped
class WebsRepository @Inject constructor(
    websDatabase: WebsDatabase
) {
    private val webDao = websDatabase.webDao()

    fun getAllBookmarks() = webDao.getAllBookmarks()

    fun getAllHistories() = webDao.getAllHistories()

    suspend fun insertWebs(webs: Bookmarks) = webDao.insertWebs(webs)

    suspend fun insertWebs(webs: Histories) = webDao.insertWebs(webs)

    suspend fun updateWebs(webs: Bookmarks) = webDao.updateWebs(webs)

    suspend fun deleteWebs(webs: Bookmarks) = webDao.deleteWebs(webs)

    suspend fun deleteWebs(webs: Histories) = webDao.deleteWebs(webs)

    suspend fun deleteAllBookmark() = webDao.deleteAllBookmarks()

    suspend fun deleteAllHistory() = webDao.deleteAllHistories()
}