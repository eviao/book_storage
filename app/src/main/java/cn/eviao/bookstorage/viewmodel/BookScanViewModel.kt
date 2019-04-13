package cn.eviao.bookstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cn.eviao.bookstorage.data.AppDatabase
import io.reactivex.Single

class BookScanViewModel(app: Application) : AndroidViewModel(app) {
    private val bookDao = AppDatabase.get(app).bookDao()

    fun checkBookExists(isbn: String): Single<Boolean> {
        return bookDao.loadExistsByIsbn(isbn)
    }
}