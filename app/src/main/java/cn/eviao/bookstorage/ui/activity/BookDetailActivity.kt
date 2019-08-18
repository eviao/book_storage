package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.Gravity.RIGHT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import cn.eviao.bookstorage.ui.widget.tickerView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView



class BookDetailActivity : BaseActivity() {

    lateinit var ui: BookDetailActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = BookDetailActivityUi()
        ui.setContentView(this)

        ui.pictureImage.setImageURI("https://img3.doubanio.com/view/subject/l/public/s29063065.jpg")

        ui.scoreText.setText("9.7")

        ui.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.box_menu_item -> {
                    val items = arrayOf(
                        "选项1",
                        "选项2",
                        "选项3",
                        "选项4",
                        "选项5",
                        "选项6"
                    )
                    val builder = QMUIDialog.CheckableDialogBuilder(this)
                    builder.addItems(items, { dialog, int -> })

                    builder.addAction("取消", { dialog, int -> })
                    builder.addAction("确定", { dialog, int ->  })
                    builder.create().show()

                    true
                }
                else -> true
            }
        }
    }
}

class BookDetailActivityUi : AnkoComponent<BookDetailActivity> {

    lateinit var toolbar: Toolbar
    lateinit var titleText: TextView
    lateinit var subtitleText: TextView
    lateinit var pictureImage: SimpleDraweeView
    lateinit var scoreText: TickerView


    override fun createView(ui: AnkoContext<BookDetailActivity>) = with(ui) {

        verticalLayout {

            toolbar = themedToolbar(R.style.AppTheme_Book_Detail_Toolbar) {
                backgroundColor = Color.WHITE
                navigationIcon = getDrawable(context, R.drawable.ic_left)
                inflateMenu(R.menu.menu_book_detail)

                elevation = dip(1).toFloat()
            }

            themedScrollView(R.style.AppTheme_Scrollbar) {
                verticalLayout {

                    cardView {
//                        setContentPadding(dip(24), dip(16), dip(24), dip(24))
                        backgroundColor = getColor(context, R.color.colorPrimary)

                        verticalLayout {
                            titleText = textView("图书名称") {
                                textSize = sp(8).toFloat()
                                textColor = getColor(context, R.color.qmui_config_color_white)
                            }
                            subtitleText = textView("图书副标题") {
                                textSize = sp(6).toFloat()
//                                textColor = getColor(context, R.color.app_text_color_50)
                                textColor = getColor(context, R.color.qmui_config_color_75_white)
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
                                scoreText = tickerView {
                                    textSize = sp(32).toFloat()
                                    textColor = getColor(context, R.color.colorPrimary)
                                    animationDuration = 600

                                    setCharacterLists(TickerUtils.provideNumberList())
                                    text = "0.0"
                                }.lparams(width = wrapContent) {
                                    bottomMargin = dip(8)
                                    gravity = RIGHT
                                }

                                textView("作者 / 作者 / 作者")
                                textView("标签 / 标签 / 标签 / 标签 / 标签")
                            }.lparams(width = matchParent) {
                                leftMargin = dip(16)
                            }.applyRecursively {
                                when (it) {
                                    is TextView -> {
                                        it.textColor = getColor(context, R.color.app_text_color_70)
                                        it.gravity = RIGHT

                                        it.lparams(width = matchParent) {
                                            bottomMargin = dip(6)
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
                            textView("内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明") {
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
                            textView("目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录目录") {
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
                    layoutParams.bottomMargin = dip(16)
                }
            }
        }
    }
}