package com.gitee.yftx.test

import android.app.Activity
import android.app.ListActivity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter

class MainActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf(
            newElement("this", MainActivity::class.qualifiedName!!)
        )

        listAdapter = SimpleAdapter(
            this, list,
            android.R.layout.simple_list_item_1,
            arrayOf("title"),
            intArrayOf(android.R.id.text1)
        )
        val s = HashMap<String, Class<Activity>>()
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val item: Map<String, String>? = l?.getItemAtPosition(position) as? Map<String, String>
        item?.get("class")?.let {
            val intent = Intent()
            intent.component = ComponentName(this, it)
            startActivity(intent)
        }
    }

    private fun newElement(title: String, className: String): Map<String, String> =
        mapOf("title" to title, "class" to className)
}