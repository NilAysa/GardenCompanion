package com.spirala1

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BiljkaDatabase::class.java
        ).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun biljkaJeUnesenaUBazu() {
        val inputPlant =
            Biljka(
                1,
                "Ruža (Rosa)",
                "Rosaceae",
                "Bodljikavo",
                listOf(MedicinskaKorist.SMIRENJE),
                ProfilOkusaBiljke.BEZUKUSNO,
                listOf("Sok od ruže"),
                listOf(KlimatskiTip.UMJERENA),
                listOf(Zemljiste.CRNICA),
                false
            )

        CoroutineScope(Dispatchers.IO).launch {

            biljkaDao.saveBiljka(inputPlant)

            val fetchedPlants = biljkaDao.getAllBiljkas()
            assertThat(fetchedPlants[0].naziv, equalTo("Ruža (Rosa)"))
        }
    }

    @Test
    @Throws(Exception::class)
    fun bazaJePraznaNakonClearData() {
        val inputPlants = listOf(
            Biljka(
                1,
                "Ruža (Rosa)",
                "Rosaceae",
                "Bodljikavo",
                listOf(MedicinskaKorist.SMIRENJE),
                ProfilOkusaBiljke.BEZUKUSNO,
                listOf("Sok od ruže"),
                listOf(KlimatskiTip.UMJERENA),
                listOf(Zemljiste.CRNICA),
                false
            ), Biljka(
                2,
                "Bosiljak (Ocimum basilicum)",
                "Lamiaceae (usnate)",
                "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
                ProfilOkusaBiljke.BEZUKUSNO,
                listOf("Salata od paradajza", "Punjene tikvice"),
                listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
                listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            ),
            Biljka(
                3,
                "Lavanda (Lavandula angustifolia)",
                "Lamiaceae (metvice)",
                "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
                listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
                ProfilOkusaBiljke.AROMATICNO,
                listOf("Jogurt sa voćem"),
                listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
                listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
            )
        )

        CoroutineScope(Dispatchers.IO).launch {

            for(testPlant in inputPlants)
                biljkaDao.saveBiljka(testPlant)

            var fetchedPlants = biljkaDao.getAllBiljkas()

            assertThat(fetchedPlants[0].naziv, equalTo("Ruža (Rosa)"))
            assertThat(fetchedPlants[1].naziv, equalTo("Bosiljak (Ocimum basilicum)"))
            assertThat(fetchedPlants[2].naziv,  equalTo("Lavanda (Lavandula angustifolia)"))

            biljkaDao.clearData()

            fetchedPlants = biljkaDao.getAllBiljkas()
            assertThat(fetchedPlants.size, equalTo(0))

        }
    }
}