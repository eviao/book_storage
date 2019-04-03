package cn.eviao.bookstorage.activity

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AfterPermissionGranted
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.eviao.bookstorage.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_book_scanning.*

class BookScanActivity : AppCompatActivity(), QRCodeView.Delegate {

    private var lightState = false

    companion object {
        const val CAMERA_REQUEST_CODE: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_scanning)
        setSupportActionBar(toolbar)

        initToolbar()
        initScanner()
    }

    override fun onStart() {
        super.onStart()
        scanningTask()
    }

    override fun onStop() {
        zbv_scanner.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        zbv_scanner.onDestroy()
        super.onDestroy()
    }

    private fun initToolbar() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initScanner() {
        zbv_scanner.setDelegate(this);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_scan, menu)
        return true
    }

    private fun toggleLight() {
        val button = toolbar.menu.findItem(R.id.action_light)

        if (lightState) {
            zbv_scanner.closeFlashlight()
            lightState = false
            button.icon = getDrawable(R.drawable.ic_flash_on_white_24dp)
        } else {
            zbv_scanner.openFlashlight()
            lightState = true
            button.icon = getDrawable(R.drawable.ic_flash_off_white_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_light -> {
                toggleLight()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private fun scanningTask() {
        val perms = Manifest.permission.CAMERA
        if (EasyPermissions.hasPermissions(this, perms)) {
            // unfix: 小米手机在首次确认摄像头权限时无法识别
            zbv_scanner.startCamera()
            zbv_scanner.startSpotAndShowRect()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.rationale_camera), CAMERA_REQUEST_CODE, perms)
        }
    }

    private fun vibrateTask() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, 128))
    }

    override fun onScanQRCodeSuccess(result: String) {
        vibrateTask()
        Snackbar.make(zbv_scanner, result, Snackbar.LENGTH_LONG).show()
    }

    override fun onScanQRCodeOpenCameraError() {
        Snackbar.make(zbv_scanner, "图书编号扫描失败", Snackbar.LENGTH_LONG).show()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) { }
}
