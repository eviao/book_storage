package cn.eviao.bookstorage.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AfterPermissionGranted
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.utils.BookUtils
import cn.eviao.bookstorage.viewmodel.BookScanViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_book_scan.*
import java.util.concurrent.TimeUnit

class BookScanActivity : AppCompatActivity(), QRCodeView.Delegate {

    private var lightState = false

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(BookScanViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_scan)
        setSupportActionBar(toolbar)

        initToolbar()
        initScanner()
    }

    override fun onStart() {
        super.onStart()
        doScanning()
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
        toolbar.setNavigationOnClickListener { finish() }
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

        val open = {
            zbv_scanner.openFlashlight()
            lightState = true
            button.icon = getDrawable(R.drawable.ic_flash_off_white_24dp)
        }
        val close = {
            zbv_scanner.closeFlashlight()
            lightState = false
            button.icon = getDrawable(R.drawable.ic_flash_on_white_24dp)
        }

        if (lightState) close() else open()
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
    private fun doScanning() {
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

    private fun doVibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, 128))
    }

    private fun showNextPage(isbn: String) {
        viewModel
            .checkBookExists(isbn)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, _ ->
                if (result) {
                    BookDetailActivity.start(this, isbn)
                } else {
                    BookAddActivity.start(this, isbn)
                }
            }
    }

    override fun onScanQRCodeSuccess(result: String) {
        doVibrate()

        if (BookUtils.isValidIsbn(result)) {
            showNextPage(result)
        } else {
            Snackbar.make(zbv_scanner, "无效的书号[${result}]", Snackbar.LENGTH_SHORT).show()

            Observable
                .timer(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    zbv_scanner.startSpot()
                }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        Snackbar.make(zbv_scanner, "图书条码扫描失败", Snackbar.LENGTH_LONG).show()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) { }

    companion object {
        const val CAMERA_REQUEST_CODE: Int = 1

        fun start(context: Context) {
            val intent = Intent(context, BookScanActivity::class.java)
            context.startActivity(intent)
        }
    }
}
