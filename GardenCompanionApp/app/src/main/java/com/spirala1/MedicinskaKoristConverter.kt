package com.spirala1

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MedicinskaKoristConverter {
    @TypeConverter
    fun fromList(value: List<MedicinskaKorist>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<MedicinskaKorist> {
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class ProfilOkusaBiljkeConverter {
    @TypeConverter
    fun fromProfilOkusa(value: ProfilOkusaBiljke?): String? = value?.name

    @TypeConverter
    fun toProfilOkusa(value: String?): ProfilOkusaBiljke? = value?.let { ProfilOkusaBiljke.valueOf(it) }
}

class StringListConverter {
    @TypeConverter
    fun fromList(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class KlimatskiTipConverter {
    @TypeConverter
    fun fromList(value: List<KlimatskiTip>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<KlimatskiTip> {
        val type = object : TypeToken<List<KlimatskiTip>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class ZemljisteConverter {
    @TypeConverter
    fun fromList(value: List<Zemljiste>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<Zemljiste> {
        val type = object : TypeToken<List<Zemljiste>>() {}.type
        return Gson().fromJson(value, type)
    }
}