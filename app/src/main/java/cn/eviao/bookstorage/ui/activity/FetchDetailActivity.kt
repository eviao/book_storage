package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.Gravity.RIGHT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.contract.FetchDetailContract
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.presenter.FetchDetailPresenter
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView

class FetchDetailActivity : BaseActivity(), FetchDetailContract.View {

    lateinit override var presenter: FetchDetailContract.Presenter

    private lateinit var loadingDialog: QMUITipDialog

    lateinit var ui: FetchDetailActivityUi
    lateinit var isbn: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isbn = intent.getStringExtra("isbn") ?: "9787115249494"

        presenter = FetchDetailPresenter(this, isbn)

        loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在加载")
            .create()

        ui = FetchDetailActivityUi()
        ui.setContentView(this)
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
        QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setTipWord(message)
            .create()
            .show()
    }

    override fun renderBook(book: Book) {
        ui.pictureImage.setImageURI(book.image)

        ui.titleText.text = book.title
        ui.isbnText.text = book.isbn
        ui.authorsText.text = book.authors
        ui.publisherText.text = book.publisher
    }
}

class FetchDetailActivityUi : AnkoComponent<FetchDetailActivity> {

    lateinit var topToolbar: Toolbar

    lateinit var titleText: TextView
    lateinit var pictureImage: SimpleDraweeView
    lateinit var isbnText: TextView
    lateinit var authorsText: TextView
    lateinit var publisherText: TextView

    lateinit var submitButton: Button

    override fun createView(ui: AnkoContext<FetchDetailActivity>) = with(ui) {
        verticalLayout {

            topToolbar = themedToolbar {
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left_32_56c596)
                inflateMenu(R.menu.menu_fetch_detail)

                elevation = dip(1).toFloat()
            }

            themedScrollView(R.style.AppTheme_Scrollbar) {
                verticalLayout {

                    cardView {
                        verticalLayout {
                            titleText = textView {
                                textSize = sp(8).toFloat()
                                textColor = getColor(context, R.color.app_text_color)

                                gravity = CENTER
                            }

                            pictureImage = simpleDraweeView{
                                val hierarchy = GenericDraweeHierarchyBuilder(resources).build()
                                hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
                                setHierarchy(hierarchy)

                                gravity = CENTER
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
                                    textColor = getColor(context, R.color.app_text_color_70)
                                    gravity = RIGHT
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
                                    textColor = getColor(context, R.color.app_text_color_70)
                                    gravity = RIGHT
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
                                    textColor = getColor(context, R.color.app_text_color_70)
                                    gravity = RIGHT
                                }.lparams(width = matchParent) {
                                    leftMargin = dip(16)
                                }
                            }
                        }
                    }

                    submitButton = button("确认保存") {
                        backgroundResource = R.drawable.primary_button
                        textColor = Color.WHITE
                    }.lparams(width = matchParent) {
                        setMargins(dip(16), dip(16), dip(16), dip(16))
                    }
                }
            }
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