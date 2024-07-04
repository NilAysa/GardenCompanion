package com.spirala1

import android.graphics.Bitmap
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Dao
interface BiljkaDAO {

    @Transaction
    suspend fun saveBiljka(biljka: Biljka): Boolean {
        val id = savePlant(biljka)
        return id != -1L
    }

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        val offlinePlants = getOfflineBiljke()
        var numOfUpdates = 0

        for (offlinePlant in offlinePlants) {

            val checkedPlant = TrefleDAO().fixData(offlinePlant.copy())
            val isChecked = checkedPlant.onlineChecked
            checkedPlant.onlineChecked = false

            if (checkedPlant != offlinePlant && isChecked) {

                checkedPlant.onlineChecked = true
                updateBiljka(checkedPlant)

                var newBitmap = TrefleDAO().getImage(checkedPlant)
                updateBitmap(BiljkaBitmap(checkedPlant.id,newBitmap))

                numOfUpdates++
            }
        }
        return numOfUpdates
    }

    @Transaction
    suspend fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
        if (getPlantById(idBiljke) == null || getImageForBiljka(idBiljke) != null)
            return false

        return addImage(BiljkaBitmap(idBiljke, bitmap)) != -1L
    }

    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Transaction
    suspend fun clearData() {
        clearBiljkas()
        clearBiljkaBitmaps()
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun savePlant(plant: Biljka): Long

    @Update
    suspend fun updateBiljka(biljka: Biljka)

    @Update
    suspend fun updateBitmap(biljkaBitmap: BiljkaBitmap)

    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljke(): List<Biljka>

    @Query("SELECT * FROM Biljka WHERE id = :plantId")
    suspend fun getPlantById(plantId: Long): Biljka?

    @Query("SELECT id FROM Biljka WHERE naziv = :plantName")
    suspend fun getPlantIdByName(plantName: String): Long?

    @Query("SELECT bitmap FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    suspend fun getImageForBiljka(idBiljke: Long): Bitmap?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImage(biljkaBitmap: BiljkaBitmap): Long

    @Query("DELETE FROM Biljka")
    suspend fun clearBiljkas()

    @Query("DELETE FROM BiljkaBitmap")
    suspend fun clearBiljkaBitmaps()

    @Query("SELECT MAX(id)+1 FROM Biljka")
    suspend fun getNextId(): Long?
}
