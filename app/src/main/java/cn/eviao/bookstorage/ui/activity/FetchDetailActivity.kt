package cn.eviao.bookstorage.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.Gravity.RIGHT
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.widget.simpleDraweeView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.cardview.v7.cardView

class FetchDetailActivity : BaseActivity() {

    lateinit var ui: FetchDetailActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = FetchDetailActivityUi()
        ui.setContentView(this)

        ui.pictureImage.setImageURI("https://img3.doubanio.com/view/subject/l/public/s29063065.jpg")
    }
}

class FetchDetailActivityUi : AnkoComponent<FetchDetailActivity> {

    lateinit var toolbar: Toolbar
    lateinit var pictureImage: SimpleDraweeView

    override fun createView(ui: AnkoContext<FetchDetailActivity>) = with(ui) {
        verticalLayout {

            toolbar = themedToolbar {
                backgroundColor = Color.WHITE
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_left)
                inflateMenu(R.menu.menu_fetch_detail)

                elevation = dip(1).toFloat()
            }

            themedScrollView(R.style.AppTheme_Scrollbar) {
                verticalLayout {

                    cardView {
                        verticalLayout {
                            textView("Java核心技术 卷1") {
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
                                textView("ISBN")
                                textView("975823528281238") {
                                    gravity = RIGHT
                                }.lparams(width = matchParent)
                            }.lparams(width = matchParent) {
                                bottomMargin = dip(8)
                            }

                            linearLayout {
                                textView("作者")
                                textView("作者 / 作者 / 作者") {
                                    gravity = RIGHT
                                }.lparams(width = matchParent)
                            }.lparams(width = matchParent) {
                                bottomMargin = dip(8)
                            }

                            linearLayout {
                                textView("出版社")
                                textView("中国邮政出版社") {
                                    gravity = RIGHT
                                }.lparams(width = matchParent)
                            }
                        }
                    }

                    button("确认保存") {
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
                    it.setContentPadding(dip(24), dip(16), dip(24), dip(16))
                    it.elevation = 0f

                    val layoutParams = it.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = dip(16)
                }
            }
        }
    }
}