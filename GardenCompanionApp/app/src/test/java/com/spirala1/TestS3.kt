package com.spirala1

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock


@RunWith(MockitoJUnitRunner::class)
class TestS3 {

    @get:Rule
    var softAssert = JUnitSoftAssertions()

    @Test
    fun fixBosiljakTest() = runBlocking{
        val dao = TrefleDAO()
        dao.setDefaultBitmap(mock())
        var fixed = dao.fixData(    Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Netacno (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ))
        softAssert.assertThat(fixed.naziv).withFailMessage("T1.1 - naziv should contain \"Ocium basilicum\"").contains("Ocimum basilicum")
        softAssert.assertThat(fixed.porodica).withFailMessage("T1.2 - porodica should contain \"Lamiaceae\"").contains("Lamiaceae")
        softAssert.assertThat(fixed.medicinskoUpozorenje).withFailMessage("T1.3 - upozorenje should contain \"NIJE JESTIVO\"").contains("NIJE JESTIVO")
        softAssert.assertThat(fixed.klimatskiTipovi).withFailMessage("T1.4 - klimatskiTipovi should contain \"Umjerena\"").contains(KlimatskiTip.UMJERENA)
        softAssert.assertAll()
    }

    @Test
    fun fixEpipactisHelleborine()= runBlocking {
        val dao = TrefleDAO()
        dao.setDefaultBitmap(mock())
        var fixed = dao.fixData(    Biljka(
            naziv = "Kruscika (Epipactis helleborine)",
            porodica = "Netacno (netacno)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE,
                MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ))

        softAssert.assertThat(fixed.naziv).withFailMessage("T2.1 - naziv should contain \"Epipactis helleborine\"").contains("Epipactis helleborine")
        softAssert.assertThat(fixed.medicinskoUpozorenje).withFailMessage("T2.2 - upozorenje should contain \"NIJE JESTIVO\"").contains("NIJE JESTIVO")
        softAssert.assertThat(fixed.klimatskiTipovi).withFailMessage("T2.3 - klimatskiTipovi should contain \"Planinska\"").contains(KlimatskiTip.PLANINSKA)
        softAssert.assertAll()
    }

    @Test
    fun getFlowerRosaPurple()= runBlocking {
        val dao = TrefleDAO()
        dao.setDefaultBitmap(mock())
        var plants = dao.getPlantswithFlowerColor("purple","rosa")
        assertTrue("T3.1 - should contain \"Rosa pendulina\"",plants.find { biljka -> biljka.naziv.contains("Rosa pendulina",ignoreCase = true) }!=null)
    }

    @Test
    fun getFlowerRampionBlue()= runBlocking {
        val dao = TrefleDAO()
        dao.setDefaultBitmap(mock())
        var plants = dao.getPlantswithFlowerColor("blue","rampion")
        assertTrue("T4.1 - should contain \"Phyteuma spicatum\"",plants.find { biljka -> biljka.naziv.contains("Phyteuma spicatum",ignoreCase = true) }!=null)
    }

}