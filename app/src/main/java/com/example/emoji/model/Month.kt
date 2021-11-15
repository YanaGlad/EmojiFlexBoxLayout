package com.example.emoji.model

import java.util.*
import kotlin.collections.HashMap

enum class Month {
    JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEPT, OCT, NOV, DEC
}

class PutValueToMonth {

    companion object {
        private const val MONTH_STEP = 100
        private const val COUNT = 1000
    }

    private val valuesMap: HashMap<String, Int> = HashMap()

    init {
        var indx = 0
        for (mon in Month.values()) {
            indx++
            valuesMap[mon.name.lowercase(Locale.getDefault())] = COUNT + indx * MONTH_STEP
        }
    }

    fun getValueByMonth(month: String): Int {
        return valuesMap[month]!!
    }
}
