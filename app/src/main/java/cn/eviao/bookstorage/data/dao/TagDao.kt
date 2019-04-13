package cn.eviao.bookstorage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.eviao.bookstorage.model.Tag
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TagDao {

    @Insert
    fun insert(tag: Tag): Single<Long>

    @Insert
    fun insert(tags: List<Tag>): Single<List<Long>>

    @Query("SELECT * FROM tags WHERE id = :id")
    fun loadById(id: Long): Maybe<Tag>

    @Query("SELECT * FROM tags WHERE text = :text")
    fun loadByText(text: String): Maybe<Tag>

    @Query("SELECT tag.* FROM tags tag WHERE tag.id IN (SELECT ref.tag_id FROM books_tags_reference ref WHERE ref.book_id = :bookId)")
    fun loadByBookId(bookId: Long): Maybe<List<Tag>>
}