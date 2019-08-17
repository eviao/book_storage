package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity.CENTER
import android.view.Gravity.RIGHT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
    }
}

class BookDetailActivityUi : AnkoComponent<BookDetailActivity> {

    lateinit var titleText: TextView
    lateinit var subtitleText: TextView
    lateinit var pictureImage: SimpleDraweeView
    lateinit var scoreText: TickerView


    override fun createView(ui: AnkoContext<BookDetailActivity>) = with(ui) {

        verticalLayout {
            scrollView {
                verticalLayout {

                    themedToolbar(R.style.AppTheme_Book_Detail_Toolbar) {
                        backgroundColor = Color.WHITE
                        navigationIcon = getDrawable(context, R.drawable.ic_left)
                        inflateMenu(R.menu.menu_book_detail)
                    }

                    cardView {
                        setContentPadding(dip(24), dip(8), dip(24), dip(24))

                        linearLayout {
                            verticalLayout {
                                titleText = textView("图书名称") {
                                    textSize = sp(8).toFloat()
                                    textColor = getColor(context, R.color.app_text_color)
                                }
                                subtitleText = textView("图书副标题") {
                                    textSize = sp(6).toFloat()
                                    textColor = getColor(context, R.color.app_text_color_50)
                                }
                            }.lparams {
                                weight = 1f
                            }

                            scoreText = tickerView {
                                textSize = sp(36).toFloat()
                                textColor = getColor(context, R.color.colorPrimary)
                                animationDuration = 600

                                setCharacterLists(TickerUtils.provideNumberList())
                                text = "0.0"
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
                                textView("9787115481184")
                                textView("作者 / 作者 / 作者")
                                textView("中国邮政出版社")
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
                            textView("内容说明") {
                                textColor = getColor(context, R.color.app_text_color_70)
                                textAppearance = R.style.AppTheme_Book_Detail_Title
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
                            textView("目录") {
                                textColor = getColor(context, R.color.app_text_color)
                                textAppearance = R.style.AppTheme_Book_Detail_Title
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
                    if (it.contentPaddingTop == 0) {
                        it.setContentPadding(dip(24), dip(24), dip(24), dip(24))
                    }
                    it.elevation = 0f

                    val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = dip(16)
                }
            }
        }
    }
}