package cn.eviao.bookstorage.ui.activity

import android.os.Bundle
import android.view.Gravity.RIGHT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class BookDetailActivity : BaseActivity() {

    lateinit var ui: BookDetailActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = BookDetailActivityUi()
        ui.setContentView(this)

        ui.pictureImage.setImageURI("https://img3.doubanio.com/view/subject/l/public/s29063065.jpg")
    }
}

class BookDetailActivityUi : AnkoComponent<BookDetailActivity> {

    lateinit var titleText: TextView
    lateinit var subtitleText: TextView
    lateinit var pictureImage: SimpleDraweeView

    override fun createView(ui: AnkoContext<BookDetailActivity>) = with(ui) {

        verticalLayout {

            cardView {
                linearLayout {
                    verticalLayout {
                        titleText = textView("图书名称") {
                            textSize = sp(8).toFloat()
                            textColor = getColor(context, R.color.qmui_config_color_pure_black)
                        }
                        subtitleText = textView("图书副标题") {
                            textSize = sp(6).toFloat()
                            textColor = getColor(context, R.color.qmui_config_color_75_pure_black)
                        }
                    }.lparams {
                        weight = 1f
                    }
                    textView("9.7") {
                        textSize = sp(14).toFloat()
                    }
                }
            }

            cardView {
                linearLayout {
                    pictureImage = simpleDraweeView{
//                        scaleType = ImageView.ScaleType.CENTER_CROP
                        adjustViewBounds = true
                    }.lparams(width = dip(100), height = dip(130))

                    verticalLayout {
                        linearLayout {
                            textView("ISBN:") {
                                textColor = getColor(context, R.color.qmui_config_color_gray_2)
                            }.lparams {
                                rightMargin = dip(8)
                            }
                            textView("9787115481184") {
                                textColor = getColor(context, R.color.qmui_config_color_75_pure_black)
                                gravity = RIGHT
                            }.lparams(width = matchParent)
                        }.lparams(width = matchParent) {
                            bottomMargin = dip(4)
                        }

                        linearLayout {
                            textView("作者:") {
                                textColor = getColor(context, R.color.qmui_config_color_gray_2)
                            }.lparams {
                                rightMargin = dip(8)
                            }
                            textView("作者 / 作者 / 作者") {
                                textColor = getColor(context, R.color.qmui_config_color_75_pure_black)
                                gravity = RIGHT
                            }.lparams(width = matchParent)
                        }.lparams(width = matchParent) {
                            bottomMargin = dip(4)
                        }

                        linearLayout {
                            textView("出版社:") {
                                textColor = getColor(context, R.color.qmui_config_color_gray_2)
                            }.lparams {
                                rightMargin = dip(8)
                            }
                            textView("中国邮政出版社") {
                                textColor = getColor(context, R.color.qmui_config_color_75_pure_black)
                                gravity = RIGHT
                            }.lparams(width = matchParent)
                        }.lparams(width = matchParent) {
                            bottomMargin = dip(4)
                        }

                        linearLayout {
                            textView("标签:") {
                                textColor = getColor(context, R.color.qmui_config_color_gray_2)
                            }.lparams {
                                rightMargin = dip(8)
                            }
                            textView("标签/标签/标签/标签/标签/标签/标签/标签") {
                                textColor = getColor(context, R.color.qmui_config_color_75_pure_black)
                                gravity = RIGHT
                            }.lparams(width = matchParent)
                        }
                    }.lparams(width = matchParent) {
                        leftMargin = dip(16)
                    }
                }.lparams(width = matchParent)
            }

            cardView {
                verticalLayout {
                    textView("内容说明") {
                    }
                    textView("内容说明内容说明内容说明内容说明内容说明内容说明内容说明内容说明") {

                    }
                }
            }

            cardView {
                verticalLayout {
                    textView("目录") {
                    }
                    textView("目录目录目录目录目录目录目录目录目录目录目录目录") {

                    }
                }
            }
        }.applyRecursively {
            when (it) {
                is CardView -> {
                    it.setContentPadding(dip(16), dip(16), dip(16), dip(16))
                    it.elevation = 0f

                    val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = dip(16)
                }
            }
        }
    }
}