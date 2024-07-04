package com.spirala1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import android.util.Base64
import java.io.ByteArrayOutputStream

class BitmapConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(encodedString: String): Bitmap {
        val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}