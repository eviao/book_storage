package cn.eviao.bookstorage.ui.activity

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity.BOTTOM
import android.widget.FrameLayout
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.ui.BaseActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class ScannerActivity : BaseActivity(), ZXingScannerView.ResultHandler {

    companion object {
        const val CAMERA_REQUEST_CODE = 0x0001
    }

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scannerView = initScannerView()

        val ui = ScannerActivityUi()
        ui.setContentView(this)
        ui.scannerWrapperView.addView(scannerView)
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

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        startScanning()
    }

    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private fun startScanning() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            scannerView.startCamera()
        } else {
            EasyPermissions.requestPermissions(this,
                getString(R.string.permit_camera), CAMERA_REQUEST_CODE, Manifest.permission.CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this)
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    private fun shaking() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, 128))
    }

    override fun handleResult(rawResult: Result?) {
        shaking()
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