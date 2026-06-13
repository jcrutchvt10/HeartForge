package com.heartforge.app.core.util

import android.content.Context
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val imagesDir = File(context.filesDir, "images").apply {
        if (!exists()) mkdirs()
    }

    /**
     * Saves a Base64 encoded image string to the internal storage.
     * @return The absolute path of the saved file, or null if failed.
     */
    fun saveBase64Image(base64String: String): String? {
        return try {
            val imageData = Base64.decode(base64String, Base64.DEFAULT)
            val fileName = "img_${UUID.randomUUID()}.jpg"
            val file = File(imagesDir, fileName)
            
            FileOutputStream(file).use { fos ->
                fos.write(imageData)
            }
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Reads a file and returns its content as a Base64 string.
     */
    fun getBase64FromFile(path: String): String? {
        return try {
            val file = File(path)
            if (!file.exists()) return null
            val bytes = file.readBytes()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Deletes an image file by its path.
     */
    fun deleteImage(path: String): Boolean {
        return try {
            val file = File(path)
            if (file.exists()) file.delete() else false
        } catch (e: Exception) {
            false
        }
    }
}
