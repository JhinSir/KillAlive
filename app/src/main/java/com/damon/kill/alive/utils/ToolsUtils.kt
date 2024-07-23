package com.damon.kill.alive.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

object ToolsUtils {
    fun getProcessName(): String? {
        var mBufferedReader: BufferedReader? = null
        return try {
            val file = File("/proc/self/cmdline")
            mBufferedReader = BufferedReader(FileReader(file))
            mBufferedReader.readLine().trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            if (mBufferedReader != null) {
                try {
                    mBufferedReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}