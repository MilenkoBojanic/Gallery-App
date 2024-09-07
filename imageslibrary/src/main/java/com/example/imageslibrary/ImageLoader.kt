package com.example.imageslibrary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.concurrent.TimeUnit

class ImageLoader(context: Context) {
    companion object {

        @Volatile
        private var instance: ImageLoader? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ImageLoader(context).also { instance = it }
            }
    }

    private val cacheDir = File(context.cacheDir, "images")
    private val cache = mutableMapOf<Int, Bitmap>()

    fun getImage(url: String, id: Int): Bitmap? {

        var bitmap: Bitmap? = null
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        val cachedImage = cache[id]
        if (cachedImage != null) {
            return cachedImage
        }

        val file = File(cacheDir, id.toString())
        if (file.exists() && !isCacheExpired(file)) {
            val options = BitmapFactory.Options()
            options.inSampleSize = 16
            val cachedBitmap = BitmapFactory.decodeFile(file.path, options)
            cache[id] = cachedBitmap
            return cachedBitmap
        }

        coroutineScope.launch {
            bitmap = getBitmapFromUrl(id, url)
        }

        return bitmap
    }

    private suspend fun getBitmapFromUrl(id: Int, url: String): Bitmap? =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = URL(url).openStream()

                if (inputStream != null) {
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val width = if (bitmap.width > 1000) (bitmap.width / 2) else bitmap.width
                    val height = if (bitmap.width > 1000) (bitmap.height / 2) else bitmap.height

                    val smallBitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        width,
                        height,
                        false
                    )

                    cache[id] = smallBitmap
                    return@withContext smallBitmap
                }
                return@withContext null
            } catch (ex: Exception) {
                return@withContext null
            }
        }

    private fun isCacheExpired(file: File): Boolean {
        val lastModified = file.lastModified()
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastModified) > TimeUnit.HOURS.toMillis(4)
    }

    fun clearCache() {
        cache.clear()
        cacheDir.deleteRecursively()
    }
}