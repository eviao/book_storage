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
import cn.eviao.bookstorage.base.BaseActivity
import cn.eviao.bookstorage.contract.ScannerContract
import cn.eviao.bookstorage.presenter.ScannerPresenter
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


@Suppress("DEPRECATION")
class ScannerActivity : BaseActivity(), ScannerContract.View, ZXingScannerView.ResultHandler {

    companion object {
        const val PERMISSION_CAMERA_CODE = 0x0001
    }

    override lateinit var presenter: ScannerContract.Presenter

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ScannerPresenter(this)

        scannerView = initScannerView()

        val ui = ScannerUi()
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

    override fun showError(message: String) {
        longToast(message)
    }

    override fun showInvalidISBN(isbn: String) {
        scannerView.longSnackbar("无效的书号: ${isbn}")
    }

    override fun startFetchDetail(isbn: String) {
        startActivity<FetchDetailActivity>("isbn" to isbn)
    }

    override fun startBookDetail(isbn: String) {
        startActivity<BookDetailActivity>("isbn" to isbn)
    }

    override fun handleResult(rawResult: Result) {
        startShaking()
        presenter.loadBook(rawResult.text)
    }
}

class ScannerUi : AnkoComponent<ScannerActivity> {

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