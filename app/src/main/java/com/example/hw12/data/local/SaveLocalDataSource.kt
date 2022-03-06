package com.example.hw12.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.Exception

class SaveLocalDataSource {
    private val gson by lazy {
        Gson()
    }

    private fun getFile(context: Context): File {
        val root = context.filesDir
        val directory = File(root, "user")
        if (directory.exists().not()) {
            directory.mkdirs()
        }
        val file = File(directory, "user.json")
        if (file.exists().not()) {
            file.createNewFile()
        }
        return file
    }

    fun save(serializable: Serializable, context: Context): Boolean {
        return try {
            val dest = getFile(context)
            ObjectOutputStream(dest.outputStream()).use {
                it.writeUnshared(serializable)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun <T : Serializable> load(context: Context): T? {
        return try {
            val dest = getFile(context)
            ObjectInputStream(dest.inputStream()).use {
                it.readUnshared() as T
            }
        } catch (e: Exception) {
            null
        }
    }

/*    fun save(serializable: Serializable, context: Context): Boolean {
        return try {
            val dest = getFile(context)
            val json = gson.toJson(serializable)
            dest.outputStream().bufferedWriter().use {
                it.write(json.toString())
            }
            true
        } catch (ignore: Exception) {
            false
        }
    }

    inline fun <reified T> load(context: Context): T? {
        return try {
            val dest = getFile(context)
            dest.inputStream().bufferedReader().use {
                gson.fromJson(it.readLines().joinToString("\n"), T::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }*/
}