package com.example.emoji.model

import java.util.*
import kotlin.collections.HashMap

enum class Month {
    JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEPT, OCT, NOV, DEC
}

class PutValueToMonth {
    private val valuesMap: HashMap<String, Int> = HashMap()
    private var count = 1000

    init {
        var indx = 0
        for (mon in Month.values()) {
            indx++
            valuesMap[mon.name.lowercase(Locale.getDefault())] = count + indx * 100
        }
    }
    fun getValueByMonth(month: String): Int {
        return valuesMap[month]!!
    }
}