package cn.eviao.bookstorage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import cn.eviao.bookstorage.model.BookTag
import io.reactivex.Single

@Dao
interface BookTagDao {
    @Insert
    fun insert(refs: List<BookTag>): Single<List<Long>>
}