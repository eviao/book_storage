package cn.eviao.bookstorage.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity.*
import android.view.View.GONE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.base.BaseActivity
import cn.eviao.bookstorage.contract.FetchDetailContract
import cn.eviao.bookstorage.presenter.FetchDetailPresenter
import cn.eviao.bookstorage.ui.widget.multipleStatusView
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.classic.common.MultipleStatusView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView


@Suppress("DEPRECATION")
class FetchDetailActivity : BaseActivity(), FetchDetailContract.View {

    lateinit override var presenter: FetchDetailContract.Presenter

    private lateinit var ui: FetchDetailUi

    private var isbn: String? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isbn = intent.getStringExtra("isbn")
        if (isbn == null) {
            throw RuntimeException("isbn must not be null")
        }

        this.isbn = isbn
        presenter = FetchDetailPresenter(this, isbn)

        ui = FetchDetailUi()
        ui.setContentView(this)

        ui.topToolbar.setNavigationOnClickListener {
            finish()
        }
        ui.topToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh_menu_item -> {
                    presenter.loadBook()
                    true
                }
                else -> true
            }
        }

        ui.submitButton.setOnClickListener {
            presenter.saveBook()
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

    override fun showSkeleton() {
        ui.statusView.showLoading(
            R.layout.layout_fetch_detail_loading,
            RelativeLayout.LayoutParams(matchParent, matchParent)
        )
    }

    override fun showError(message: String) {
        val view = layoutInflater.inflate(R.layout.layout_fetch_detail_error, null)
        val messageText = view.findViewById<TextView>(R.id.message_text)
        messageText.text = message

        val lparams = RelativeLayout.LayoutParams(matchParent, matchParent)
        ui.statusView.showError(view, lparams)
    }

    override fun showLoading() {
        loadingDialog = indeterminateProgressDialog("正在加载") {
            setCancelable(false)
        }
    }

    override fun hideLoading() {
        loadingDialog?.dismiss()
    }

    override fun disableSubmitButton() {
        ui.submitButton.isEnabled = false
    }

    override fun startBookList() {
        startActivity<BookListActivity>()
    }

    override fun renderBook() {
        ui.statusView.showContent()

        val book = (presenter as FetchDetailPresenter).book
        if (book == null) {
            return
        }

        if (book.image != null) {
            ui.pictureImage.setImageURI(book.image)
        }else {
            ui.pictureImage.visibility = GONE
        }

        ui.titleText.text = book.title
        ui.isbnText.text = book.isbn
        ui.authorsText.text = book.authors ?: "无"
        ui.publisherText.text = book.publisher ?: "无"
    }
}

class FetchDetailUi : AnkoComponent<FetchDetailActivity> {

    lateinit var topToolbar: Toolbar
    lateinit var statusView: MultipleStatusView

    lateinit var titleText: TextView
    lateinit var pictureImage: SimpleDraweeView
    lateinit var isbnText: TextView
    lateinit var authorsText: TextView
    lateinit var publisherText: TextView

    lateinit var submitButton: Button

    override fun createView(ui: AnkoContext<FetchDetailActivity>) = with(ui) {
        verticalLayout {

            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)

            topToolbar = themedToolbar {
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left_32_56c596)
                inflateMenu(R.menu.menu_fetch_detail)

                elevation = dip(1).toFloat()
            }

            themedScrollView(R.style.AppTheme_Scrollbar) {
                isFillViewport = true

                statusView = multipleStatusView {
                    verticalLayout {
                        layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)

                        cardView {
                            verticalLayout {
                                titleText = textView {
                                    gravity = CENTER

                                    textSize = sp(8).toFloat()
                                    textColor = getColor(context, R.color.app_text_color)
                                }

                                pictureImage = simpleDraweeView{
                                    gravity = CENTER

                                    val hierarchy = GenericDraweeHierarchyBuilder(resources).build()
                                    hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
                                    setHierarchy(hierarchy)
                                }.lparams(width = dip(200), height = dip(260)) {
                                    topMargin = dip(16)
                                }
                            }
                        }.lparams(width = matchParent, height = wrapContent) {
                            topMargin = dip(16)
                        }

                        cardView {
                            verticalLayout {

                                linearLayout {
                                    textView("ISBN") {
                                        textColor = getColor(context, R.color.app_text_color_50)
                                    }
                                    isbnText = textView {
                                        gravity = END
                                        textColor = getColor(context, R.color.app_text_color_70)
                                    }.lparams(width = matchParent) {
                                        leftMargin = dip(16)
                                    }
                                }.lparams(width = matchParent) {
                                    bottomMargin = dip(8)
                                }

                                linearLayout {
                                    textView("作者") {
                                        textColor = getColor(context, R.color.app_text_color_50)
                                    }
                                    authorsText = textView {
                                        gravity = END
                                        textColor = getColor(context, R.color.app_text_color_70)
                                    }.lparams(width = matchParent) {
                                        leftMargin = dip(16)
                                    }
                                }.lparams(width = matchParent) {
                                    bottomMargin = dip(8)
                                }

                                linearLayout {
                                    textView("出版社") {
                                        textColor = getColor(context, R.color.app_text_color_50)
                                    }
                                    publisherText = textView {
                                        gravity = END
                                        textColor = getColor(context, R.color.app_text_color_70)
                                    }.lparams(width = matchParent) {
                                        leftMargin = dip(16)
                                    }
                                }
                            }
                        }

                        submitButton = button("确认保存") {
                            backgroundResource = R.drawable.button_primary
                            textColor = Color.WHITE
                        }.lparams(width = matchParent) {
                            setMargins(dip(16), dip(16), dip(16), dip(16))
                        }
                    }
                }.lparams(width = matchParent, height = matchParent)
            }.lparams(width = matchParent, height = matchParent)
        }.applyRecursively {
            when (it) {
                is CardView -> {
                    it.setContentPadding(dip(24), dip(24), dip(24), dip(24))
                    it.elevation = 1.0f

                    val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = dip(16)
                }
            }
        }
    }
}