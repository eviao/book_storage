package cn.eviao.bookstorage.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.adapter.TabPagerAdapter
import cn.eviao.bookstorage.databinding.ActivityBookDetailBinding
import cn.eviao.bookstorage.fragment.BookDetailBasicFragment
import cn.eviao.bookstorage.fragment.BookDetailCatalogFragment
import cn.eviao.bookstorage.viewmodel.BookDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding
    private lateinit var viewModel: BookDetailViewModel

    private lateinit var isbn: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBind()
        setSupportActionBar(binding.toolbar)

        initTabs()
        initToolbar()

        initParams()
        initData()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initDataBind() {
        viewModel = ViewModelProviders.of(this).get(BookDetailViewModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityBookDetailBinding>(this, R.layout.activity_book_detail)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initParams() {
        isbn = intent.getStringExtra("isbn") ?: "9787111544937"
    }

    private fun showError(t: Throwable) {
        val icon = getDrawable(R.drawable.ic_description_blue_grey_50_24dp)
        progress.showError(icon, "发生错误", t.message, "关闭") {
            finish()
        }
    }

    private fun initData() {
        progress.showLoading()

        viewModel.loadBook(isbn).flatMap { viewModel.loadTags(it.id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                initContent()
                progress.showContent()
            }, {
                showError(it)
            })
    }

    private fun initContent() {
        viewModel.book.value?.let { book ->
            val tags = viewModel.tags.value ?: arrayListOf()

            val adapter = TabPagerAdapter(supportFragmentManager, listOf(
                "基本信息" to BookDetailBasicFragment.newInstance(book, tags),
                "目录" to BookDetailCatalogFragment.newInstance(book)
            ))
            vp_content.adapter = adapter
        }
    }

    private fun initTabs() {
        tl_tabs.setupWithViewPager(vp_content)
    }

    private fun deleteBook() {
        AlertDialog.Builder(this)
            .setTitle("确认删除？")
            .setPositiveButton("确认") { dialog, _ ->
                viewModel.book.value?.let { book ->
                    viewModel.deleteBook(book.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            finish()
                        }, {
                            showError(it)
                        })
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_delete -> {
                deleteBook()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun start(context: Context, isbn: String) {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra("isbn", isbn)
            context.startActivity(intent)
        }
    }
}
