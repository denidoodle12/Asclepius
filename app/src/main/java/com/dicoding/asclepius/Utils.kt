package com.dicoding.asclepius

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private const val RELATIVE_PATH = "Pictures/MyCamera/"

fun createImageUri(context: Context): Uri? {
    // Membuat timestamp untuk nama file
    val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    // Mengisi ContentValues dengan detail gambar
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, RELATIVE_PATH)
    }

    // Menyimpan dan mengembalikan URI
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
}