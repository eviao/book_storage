package cn.eviao.bookstorage.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.adapter.BookListAdapter
import cn.eviao.bookstorage.databinding.ActivityBookListBinding
import cn.eviao.bookstorage.viewmodel.BookListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_book_list.*

class BookListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookListBinding
    private lateinit var viewModel: BookListViewModel
    private lateinit var bookListAdapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBind()
        setSupportActionBar(binding.toolbar)

        initData()
    }

    private fun initDataBind() {
        viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityBookListBinding>(this, R.layout.activity_book_list)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initData() {
        initBookCount()
        initBookList()
    }

    private fun initBookCount() {
        val title = getString(R.string.list_title)

        viewModel.count
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count, e ->
                binding.toolbar.title = title.replace("{{count}}", count.toString())
            }
    }

    private fun initBookList() {
        bookListAdapter = BookListAdapter(this)

        rv_bookList.adapter = bookListAdapter
        rv_bookList.layoutManager = LinearLayoutManager(this)
        rv_bookList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel.allBooks.observe(this, Observer(bookListAdapter::submitList))
    }

    fun onAddClick(view: View) {
        BookScanActivity.start(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BookListActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }
}
