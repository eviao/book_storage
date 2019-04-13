package cn.eviao.bookstorage.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.databinding.ActivityBookAddBinding
import cn.eviao.bookstorage.viewmodel.BookAddViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_book_add.*


class BookAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookAddBinding
    private lateinit var viewModel: BookAddViewModel
    private lateinit var isbn: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBind()
        setSupportActionBar(binding.toolbar)

        initParams()
        initData()
        initToolbar()
    }

    private fun initDataBind() {
        viewModel = ViewModelProviders.of(this).get(BookAddViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityBookAddBinding>(this, R.layout.activity_book_add)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initToolbar() {
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun fetchBook() {
        progress.showLoading()

        viewModel.fetchBook(isbn)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { progress.showContent() },
                {
                    progress.showError(
                        getDrawable(R.drawable.ic_description_blue_grey_50_24dp),
                        "加载错误",
                        it.message,
                        "重试"
                    ) {
                        fetchBook()
                    }
                }
            )
    }

    private fun initParams() {
        isbn = intent.getStringExtra("isbn") ?: "9787111135104"
    }

    private fun initData() {
        fetchBook()
    }

    private fun addBook() {
        viewModel.response.value?.let {
            viewModel.addBook(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    BookListActivity.start(this)
                }, {
                    progress.showError(
                        getDrawable(R.drawable.ic_description_blue_grey_50_24dp),
                        "保存错误",
                        it.message,
                        "重试"
                    ) {
                        addBook()
                    }
                })
        }
    }

    fun onSaveClick(view: View) {
        addBook()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_add, menu)
        return true
    }

    companion object {
        fun start(context: Context, isbn: String) {
            val intent = Intent(context, BookAddActivity::class.java)
            intent.putExtra("isbn", isbn)
            context.startActivity(intent)
        }
    }
}
