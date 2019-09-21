package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity.*
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.base.BaseActivity
import cn.eviao.bookstorage.contract.BookDetailContract
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BookDetailPresenter
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import cn.eviao.bookstorage.ui.widget.tickerView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView


@Suppress("DEPRECATION")
class BookDetailActivity : BaseActivity(), BookDetailContract.View {

    lateinit override var presenter: BookDetailContract.Presenter

    private lateinit var ui: BookDetailUi

    private var isbn: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isbn = intent.getStringExtra("isbn")
        if (isbn == null) {
            throw RuntimeException("isbn must not be null")
        }

        this.isbn = isbn
        presenter = BookDetailPresenter(this, isbn)

        ui = BookDetailUi()
        ui.setContentView(this)

        ui.topToolbar.setNavigationOnClickListener {
            startBookList()
        }
        ui.topToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.all_menu_item -> {
                    showDetailDialog()
                    true
                }
                R.id.box_menu_item -> {
                    presenter.loadBoxs()
                    true
                }
                R.id.delete_menu_item -> {
                    deleteBook()
                    true
                }
                else -> true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showError(message: String) {
        longToast(message)
    }

    override fun renderBook() {
        val book = (presenter as BookDetailPresenter).book
        if (book == null) {
            longToast("图书信息加载失败")
            return
        }

        ui.titleText.text = book.title

        val subtitle = book.subtitle ?: book.originTitle
        if (subtitle.isNullOrBlank()) {
            ui.subtitleText.visibility = GONE
        } else {
            ui.subtitleText.text = subtitle
        }

        if (book.image.isNullOrBlank()) {
            ui.pictureImage.visibility = GONE
        } else {
            ui.pictureImage.setImageURI(book.image)
        }

        if (book.rating == null) {
            ui.ratingText.visibility = GONE
        } else {
            ui.ratingText.text = book.rating.toString()
        }

        if (book.authors.isNullOrBlank()) {
            ui.authorsText.visibility = GONE
        } else {
            ui.authorsText.text = book.authors
        }

        if (book.tags.isNullOrBlank()) {
            ui.tagsText.visibility = GONE
        } else {
            ui.tagsText.text = book.tags
        }

        if (book.summary.isNullOrBlank()) {
            ui.summaryCard.visibility = GONE
        } else {
            ui.summaryText.text = book.summary
        }

        if (book.catalog.isNullOrBlank()) {
            ui.catalogCard.visibility = GONE
        } else {
            ui.catalogText.text = book.catalog
        }
    }

    fun showDetailDialog() {
        val book = (presenter as BookDetailPresenter).book
        val box = (presenter as BookDetailPresenter).box!!

        if (book == null) {
            longToast("图书信息加载失败")
            return
        }

        val dialogUi = BookDetailDialogUi()
        val view = dialogUi.createView(AnkoContext.create(this, this))

        dialogUi.isbnText.text = book.isbn
        dialogUi.pubdateText.text = book.pubdate
        dialogUi.publisherText.text = book.publisher

        dialogUi.boxText.text = if (box == Box.EMPTY) "无" else {
            if (box.intro.isNullOrBlank()) box.name else "${box.name} [${box.intro}]"
        }

        alert {
            customView {
                addView(view, LinearLayout.LayoutParams(matchParent, matchParent))
            }
            negativeButton("关闭", { dialog -> dialog.dismiss() })
        }.show()
    }

    override fun showUpdateBoxDialog(boxs: List<Box>) {
        if (boxs.isEmpty()) {
            alert {
                message = getString(R.string.box_list_empty)
                negativeButton("关闭", { dialog -> dialog.dismiss() })
            }.show()
            return
        }

        val title = getString(R.string.box_list_title)
        val items = boxs.map { it.name!! }

        selector(title, items, { _, index ->
            presenter.updateBox(boxs.get(index))
        })
    }

    override fun startBookList() {
        startActivity<BookListActivity>()
    }

    fun deleteBook() {
        alert {
            message = "确定要删除吗？"
            negativeButton("取消", { dialog -> dialog.dismiss() })
            positiveButton("确定", { _ -> presenter.deleteBook() })
        }.show()
    }
}

class BookDetailUi : AnkoComponent<BookDetailActivity> {

    lateinit var topToolbar: Toolbar

    lateinit var titleText: TextView
    lateinit var subtitleText: TextView
    lateinit var pictureImage: SimpleDraweeView

    lateinit var ratingText: TickerView
    lateinit var authorsText: TextView
    lateinit var tagsText: TextView

    lateinit var summaryCard: CardView
    lateinit var summaryText: TextView

    lateinit var catalogCard: CardView
    lateinit var catalogText: TextView

    override fun createView(ui: AnkoContext<BookDetailActivity>) = with(ui) {

        verticalLayout {

            topToolbar = themedToolbar {
                backgroundColor = Color.WHITE
                navigationIcon = getDrawable(context, R.drawable.ic_left_32_56c596)
                inflateMenu(R.menu.menu_book_detail)

                elevation = dip(1.0f).toFloat()
            }

            themedScrollView(R.style.AppTheme_Scrollbar) {
                verticalLayout {

                    cardView {
                        backgroundColor = Color.WHITE

                        verticalLayout {
                            titleText = textView {
                                textSize = sp(8).toFloat()
                                textColor = getColor(context, R.color.app_text_color_primary)

                                setTextIsSelectable(true)
                            }
                            subtitleText = textView {
                                textSize = sp(6).toFloat()
                                textColor = getColor(context, R.color.app_text_color_tertiary)
                            }
                        }
                    }

                    cardView {
                        linearLayout {
                            verticalLayout {
                                pictureImage = simpleDraweeView{
                                    gravity = CENTER

                                    val hierarchy = GenericDraweeHierarchyBuilder(resources).build()
                                    hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
                                    setHierarchy(hierarchy)
                                }.lparams(width = dip(100), height = dip(130))
                            }.lparams(width = dip(120))

                            verticalLayout {
                                ratingText = tickerView {
                                    textSize = sp(32).toFloat()
                                    textColor = getColor(context, R.color.colorPrimary)
                                    animationDuration = 600

                                    setCharacterLists(TickerUtils.provideNumberList())
                                    text = "0.0"
                                }.lparams(width = wrapContent) {
                                    bottomMargin = dip(8)
                                    gravity = END
                                }

                                authorsText = textView {
                                    textSize = sp(5).toFloat()
                                }

                                tagsText = textView {
                                    textSize = sp(5).toFloat()
                                }
                            }.lparams(width = matchParent) {
                                leftMargin = dip(0)
                            }.applyRecursively {
                                when (it) {
                                    is TextView -> {
                                        it.textColor = getColor(context, R.color.app_text_color_secondary)
                                        it.gravity = END

                                        it.lparams(width = matchParent) {
                                            topMargin = dip(4)
                                            bottomMargin = dip(4)
                                        }
                                    }
                                }
                            }
                        }.lparams(width = matchParent)
                    }

                    summaryCard = cardView {
                        verticalLayout {
                            themedTextView("内容说明", R.style.AppTheme_BookDetail_Header) {
                                textColor = getColor(context, R.color.app_text_color_secondary)
                            }.lparams {
                                bottomMargin = dip(4)
                            }

                            summaryText = textView {
                                textColor = getColor(context, R.color.app_text_color_tertiary)
                            }
                        }
                    }

                    catalogCard = cardView {
                        verticalLayout {
                            themedTextView("目录", R.style.AppTheme_BookDetail_Header) {
                                textColor = getColor(context, R.color.app_text_color_secondary)
                            }.lparams {
                                bottomMargin = dip(4)
                            }

                            catalogText = textView {
                                textColor = getColor(context, R.color.app_text_color_tertiary)
                                setTextIsSelectable(true)
                            }
                        }
                    }
                }
            }
        }.applyRecursively {
            when (it) {
                is CardView -> {
                    it.setContentPadding(dip(24), dip(24), dip(24), dip(24))
                    it.elevation = 0f

                    val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                    layoutParams.topMargin = dip(16)
                }
            }
        }
    }
}


class BookDetailDialogUi : AnkoComponent<BookDetailActivity> {

    lateinit var isbnText: TextView
    lateinit var pubdateText: TextView
    lateinit var publisherText: TextView
    lateinit var boxText: TextView

    override fun createView(ui: AnkoContext<BookDetailActivity>) = with(ui) {
        verticalLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)
            setPadding(dip(24), dip(24), dip(24), dip(24))

            linearLayout {
                themedTextView("ISBN", R.style.AppTheme_BookDetail_Label)
                isbnText = themedTextView(R.style.AppTheme_BookDetail_Description).lparams(width = matchParent)
            }

            linearLayout {
                themedTextView("出版时间", R.style.AppTheme_BookDetail_Label)
                pubdateText = themedTextView(R.style.AppTheme_BookDetail_Description).lparams(width = matchParent)
            }

            linearLayout {
                themedTextView("出版社", R.style.AppTheme_BookDetail_Label)
                publisherText = themedTextView(R.style.AppTheme_BookDetail_Description).lparams(width = matchParent)
            }

            linearLayout {
                themedTextView(R.string.box_list_title, R.style.AppTheme_BookDetail_Label)
                boxText = themedTextView(R.style.AppTheme_BookDetail_Description).lparams(width = matchParent)
            }
        }.applyRecursively {
            when (it) {
                is LinearLayout -> {
                    val lparams = LinearLayout.LayoutParams(matchParent, wrapContent)
                    lparams.bottomMargin = dip(8)

                    it.layoutParams = lparams
                }
                is TextView -> {
                    it.textSize = sp(6).toFloat()
                }
            }
        }
    }
}
