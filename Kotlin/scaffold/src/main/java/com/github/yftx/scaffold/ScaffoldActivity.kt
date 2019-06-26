package com.github.yftx.scaffold

import android.Manifest
import android.app.ListActivity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import timber.log.Timber


private val NECESSARY_PERMISSION = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

abstract class ScaffoldActivity : ListActivity() {

    abstract fun getSampleMenu(): List<Map<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        initLog()
        super.onCreate(savedInstanceState)
        checkRuntimePermission()
    }

    private fun initLog() {
        Timber.plant(Timber.DebugTree())
    }

    private fun scaffoldInit() {
        setContentView(R.layout.scaffold_layout)
        listAdapter = SimpleAdapter(
            this, getSampleMenu(),
            android.R.layout.simple_list_item_1,
            arrayOf("title"),
            intArrayOf(android.R.id.text1)
        )
    }

    private fun checkRuntimePermission() {
        Dexter.withActivity(this)
            .withPermissions(*NECESSARY_PERMISSION)
            .withListener(CompositeMultiplePermissionsListener(
                DialogWithGoSettingOnAnyDeniedMultiplePermissionsListener.Builder
                    .withContext(this)
                    .withTitle("缺少必要权限")
                    .withMessage("请前往设置界面赋予全部权限")
                    .withButtonText("确定")
                    .withConfirmClickListener {
                        val myAppSettings =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
                        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(myAppSettings)
                        finish()
                    }.build(),
                object : BaseMultiplePermissionsListener() {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.deniedPermissionResponses.size == 0)
                                scaffoldInit()
                        }
                    }
                }
            )).check()
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val item: Map<String, String>? = l?.getItemAtPosition(position) as? Map<String, String>
        item?.get("class")?.let {
            val intent = Intent()
            intent.component = ComponentName(this, it)
            startActivity(intent)
        }
    }

    protected fun newElement(title: String, className: String): Map<String, String> =
        mapOf("title" to title, "class" to className)
}