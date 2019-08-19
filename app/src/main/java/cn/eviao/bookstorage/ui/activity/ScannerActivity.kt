package cn.eviao.bookstorage.ui.activity

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity.BOTTOM
import android.widget.FrameLayout
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.contract.ScannerContract
import cn.eviao.bookstorage.persistence.DataSource
import cn.eviao.bookstorage.presenter.ScannerPresenter
import cn.eviao.bookstorage.ui.BaseActivity
import cn.eviao.bookstorage.ui.BaseView
import cn.eviao.bookstorage.utils.BookUtils
import com.google.zxing.Result
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class ScannerActivity : BaseActivity(), ScannerContract.View, ZXingScannerView.ResultHandler {

    companion object {
        const val PERMISSION_CAMERA_CODE = 0x0001
    }

    override lateinit var presenter: ScannerContract.Presenter

    private lateinit var scannerView: ZXingScannerView
    private lateinit var loadingDialog: QMUITipDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ScannerPresenter(this)
        scannerView = initScannerView()

        loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("正在加载")
            .create()

        val ui = ScannerActivityUi()
        ui.setContentView(this)
        ui.scannerWrapperView.addView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
        startScanning()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
        stopScanning()
    }

    private fun initScannerView(): ZXingScannerView {
        val scanner = ZXingScannerView(this)
        scanner.setBorderColor(getColor(R.color.colorPrimary))
        scanner.setBorderCornerRadius(10)
        scanner.setBorderStrokeWidth(10)
        scanner.setLaserColor(getColor(R.color.colorAccent))
        scanner.setAutoFocus(true)
        return scanner
    }

    @AfterPermissionGranted(PERMISSION_CAMERA_CODE)
    private fun startScanning() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permit_camera),
                PERMISSION_CAMERA_CODE, Manifest.permission.CAMERA
            )
        }
    }

    override fun restartScanning() {
        Handler().postDelayed({
            scannerView.resumeCameraPreview(this)
        }, 1000)
    }

    private fun stopScanning() {
        scannerView.stopCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun startShaking() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, 128))
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun hideLoading() {
        loadingDialog.hide()
    }

    override fun showErrorISBN(isbn: String) {
        scannerView.longSnackbar("无效的书号: ${isbn}")
    }

    override fun showFetchDetail() {
        println("show fetchdetail")
    }

    override fun showBookDetail() {
        println("show bookdetail")
    }

    override fun handleResult(rawResult: Result) {
        startShaking()
        presenter.loadDetail(rawResult.text)
    }
}

class ScannerActivityUi : AnkoComponent<ScannerActivity> {

    lateinit var scannerWrapperView: FrameLayout

    override fun createView(ui: AnkoContext<ScannerActivity>) = with(ui) {
        frameLayout {
            scannerWrapperView = frameLayout {}.lparams(width = matchParent, height = matchParent)

            verticalLayout {}.lparams(width = matchParent, height = wrapContent) {
                gravity = BOTTOM
            }
        }
    }
}