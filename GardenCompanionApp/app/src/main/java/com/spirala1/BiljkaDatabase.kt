package com.spirala1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Biljka::class, BiljkaBitmap::class), version = 1)
@TypeConverters(
    MedicinskaKoristConverter::class,
    ProfilOkusaBiljkeConverter::class,
    StringListConverter::class,
    KlimatskiTipConverter::class,
    ZemljisteConverter::class,
    BitmapConverter::class
)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO
    companion object {
        private var INSTANCE: BiljkaDatabase? = null
        fun getInstance(context: Context): BiljkaDatabase {
            if (INSTANCE == null)
                synchronized(BiljkaDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            return INSTANCE!!
        }
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BiljkaDatabase::class.java,
                "biljke-db"
            ).fallbackToDestructiveMigration(false)
                .build()
    }
}
