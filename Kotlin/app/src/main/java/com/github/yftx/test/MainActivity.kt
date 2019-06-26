package com.github.yftx.test

import com.github.yftx.scaffold.ScaffoldActivity


class MainActivity : ScaffoldActivity() {
    override fun getSampleMenu(): List<Map<String, String>> {
        return mutableListOf(
            newElement("this", MainActivity::class.qualifiedName!!)
        )
    }

}


