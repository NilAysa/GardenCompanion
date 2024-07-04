package com.spirala1

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "Biljka")
data class Biljka(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "naziv") var naziv: String,
    @ColumnInfo(name = "family") var porodica: String,
    @ColumnInfo(name = "medicinskoUpozorenje") var medicinskoUpozorenje: String,
    @TypeConverters(MedicinskaKoristConverter::class)
    @ColumnInfo(name = "medicinskeKoristi") var medicinskeKoristi: List<MedicinskaKorist>,
    @TypeConverters(ProfilOkusaBiljkeConverter::class)
    @ColumnInfo(name = "profilOkusa") var profilOkusa: ProfilOkusaBiljke?,
    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "jela") var jela: List<String>,
    @TypeConverters(KlimatskiTipConverter::class)
    @ColumnInfo(name = "klimatskiTipovi") var klimatskiTipovi: List<KlimatskiTip>,
    @TypeConverters(ZemljisteConverter::class)
    @ColumnInfo(name = "zemljisniTipovi") var zemljisniTipovi: List<Zemljiste>,
    @ColumnInfo(name = "onlineChecked") var onlineChecked: Boolean = false
)

@Entity(tableName = "BiljkaBitmap")
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idBiljke") val idBiljke: Long? = null,
    @TypeConverters(BitmapConverter::class)
    @ColumnInfo(name = "bitmap") val bitmap: Bitmap?
)
