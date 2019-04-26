package cn.eviao.bookstorage.service

import android.content.Context
import android.util.Log
import cn.eviao.bookstorage.data.AppDatabase
import cn.eviao.bookstorage.http.Http
import cn.eviao.bookstorage.http.Response
import cn.eviao.bookstorage.http.impl.DoubanCrawlImpl
import cn.eviao.bookstorage.model.BookTag
import cn.eviao.bookstorage.model.Tag
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class BookService(private val context: Context) {
    private val TAG = this.javaClass.name

    private val database = AppDatabase.get(context)
    private val bookDao = database.bookDao()
    private val tagDao = database.tagDao()
    private val bookTagDao = database.bookTagDao()

    private val http: Http = DoubanCrawlImpl()

    fun pullBook(isbn: String): Completable {
        return http.fetch(isbn).flatMapCompletable { addBook(it) }
    }

    fun ensureTags(texts: List<String>): Single<List<Long>> {
        val loadTagId = { text: String -> tagDao.loadByText(text).map { it.id } }
        val insertTag = { text: String -> tagDao.insert(Tag(id = 0, text = text)) }

        val tags = texts.map { loadTagId(it).switchIfEmpty(insertTag(it)) }
        return Single.concatArray(*(Array(tags.count(), { tags.get(it) }))).toList()
    }

    fun addBook(response: Response): Completable {
        val insertBook = bookDao.insert(response.book)
        val insertTags = ensureTags(response.tags)
        val insertRefs = { refs: List<BookTag> -> bookTagDao.insert(refs) }

        val combineBookWithTag = BiFunction<Long, List<Long>, Pair<Long, List<Long>>> { bookId, tagId -> bookId to tagId }

        return Completable.create { emitter ->
            AppDatabase.get(context).runInTransaction {
                Single.zip(insertBook, insertTags, combineBookWithTag).flatMap { (bookId, tagIds) ->
                    insertRefs(tagIds.map { BookTag(id = 0, bookId = bookId, tagId = it) })
                }.subscribe(
                    {
                        Log.d(TAG, "[${response.book.title}] added successfully.")
                        emitter.onComplete()
                    },
                    {
                        Log.e(TAG, "[${response.book.title}] persistence failed.", it)
                        emitter.onError(it)
                    }
                )
            }
        }
    }
}