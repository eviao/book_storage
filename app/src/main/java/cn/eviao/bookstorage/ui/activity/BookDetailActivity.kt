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
import cn.eviao.bookstorage.contract.BookDetailContract
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.presenter.BookDetailPresenter
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import cn.eviao.bookstorage.ui.widget.tickerView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView


class BookDetailActivity : BaseActivity(), BookDetailContract.View {

    lateinit override var presenter: BookDetailContract.Presenter

    lateinit var ui: BookDetailActivityUi
    lateinit var isbn: String

    private lateinit var loadingDialog: QMUITipDialog
    private lateinit var updateBoxDialog: QMUIDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isbn = intent.getStringExtra("isbn")
        presenter = BookDetailPresenter(this, isbn)

        loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在加载")
            .create()
        updateBoxDialog = QMUIDialog.MessageDialogBuilder(this)
            .setTitle(R.string.box_list_title)
            .setMessage("暂无记录")
            .addAction("关闭", { dialog, int -> dialog.dismiss() })
            .create(R.style.Dialog)

        ui = BookDetailActivityUi()
        ui.setContentView(this)

        ui.topToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.box_menu_item -> {
                    updateBoxDialog.show()
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

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun hideLoading() {
        loadingDialog.hide()
    }

    override fun showError(message: String) {
        longToast(message)
    }

    override fun renderBook(book: Book) {

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

        ui.summaryText.text = book.summary
        ui.catalogText.text = book.catalog
    }

    override fun createUpdateBoxDialog(boxs: List<Box>, book: Book) {
        if (boxs.isEmpty()) {
            return
        }

        val builder = QMUIDialog.CheckableDialogBuilder(this)
        builder.setTitle(R.string.box_list_title)
        builder.addItems(boxs.map { it.name }.toTypedArray(), { dialog, int -> })
        builder.addAction("取消", { dialog, int -> dialog.dismiss() })
        builder.addAction("确定", { dialog, int ->
            if (builder.checkedIndex < 0) {
                dialog.dismiss()
            } else {
                toast(builder.checkedIndex.toString())
            }
        })

        if (book.boxId != null) {
            builder.checkedIndex = boxs.indexOf(boxs.find { it.id == book.boxId })
        }

        updateBoxDialog = builder.create(R.style.Dialog)
    }
}

class BookDetailActivityUi : AnkoComponent<BookDetailActivity> {

    lateinit var topToolbar: Toolbar

    lateinit var titleText: TextView
    lateinit var subtitleText: TextView
    lateinit var pictureImage: SimpleDraweeView

    lateinit var ratingText: TickerView
    lateinit var authorsText: TextView
    lateinit var tagsText: TextView

    lateinit var summaryText: TextView
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
                                textColor = getColor(context, R.color.app_text_color)
                            }
                            subtitleText = textView {
                                textSize = sp(6).toFloat()
                                textColor = getColor(context, R.color.app_text_color_50)
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
                                        it.textColor = getColor(context, R.color.app_text_color_70)
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

                    cardView {
                        verticalLayout {
                            themedTextView("内容说明", R.style.TextBold) {
                                textColor = getColor(context, R.color.app_text_color_70)
                                textAppearance = R.style.TextBold
                            }.lparams {
                                bottomMargin = dip(4)
                            }

                            summaryText = textView {
                                textColor = getColor(context, R.color.app_text_color_50)
                            }
                        }
                    }

                    cardView {
                        verticalLayout {
                            themedTextView("目录", R.style.TextBold) {
                                textColor = getColor(context, R.color.app_text_color)
                            }.lparams {
                                bottomMargin = dip(4)
                            }

                            catalogText = textView {
                                textColor = getColor(context, R.color.app_text_color_50)
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