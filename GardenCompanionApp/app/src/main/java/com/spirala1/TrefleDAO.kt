package com.spirala1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.URL

class TrefleDAO() {
    private val trefleApiToken: String = BuildConfig.TREFLE_API_KEY
    private var defaultBitmap: Bitmap?=null
    private val apiService: TrefleApi = TrefleApiAdapter.instance.create(TrefleApi::class.java)
    private lateinit var biljkaDAO: BiljkaDAO

    fun setDefaultBitmap(bitmap: Bitmap) {
        defaultBitmap = bitmap
    }

    suspend fun getImage(biljka: Biljka): Bitmap {
        defaultBitmap = getDefaultBitmap(100, 100)
        val plantNameLatin = extractTextInBrackets(biljka.naziv)
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<TrefleImageResponse> =
                    apiService.searchPlants(plantNameLatin, trefleApiToken)
                if (response.isSuccessful) {
                    val plantList = response.body()?.data ?: emptyList()
                    if (plantList.isNotEmpty()) {
                        val imageUrl = plantList[0].image_url
                        val bitmap = getImageBitmapFromUrl(imageUrl)
                        return@withContext bitmap
                    } else {
                        return@withContext defaultBitmap!!
                    }
                } else {
                    return@withContext defaultBitmap!!
                }
            } catch (e: Exception) {
                return@withContext defaultBitmap!!
            }
        }
    }




    suspend fun fixData(biljka: Biljka): Biljka {
        val plantNameLatin = extractTextInBrackets(biljka.naziv)

        return withContext(Dispatchers.IO) {
            try {
                val searchResponse: Response<TrefleImageResponse> =
                    apiService.searchPlants(plantNameLatin, trefleApiToken)
                if (searchResponse.isSuccessful) {
                    val plantList = searchResponse.body()?.data ?: emptyList()
                    if (plantList.isNotEmpty()) {
                        val plantId = plantList[0].id
                        val detailResponse: Response<TrefleFixDataResponse> =
                            apiService.getPlantDetails(plantId, trefleApiToken)
                        if (detailResponse.isSuccessful) {
                            val plantData = detailResponse.body()?.data ?: return@withContext biljka
                            // Update family name if necessary
                            if (plantData.family?.name != null && biljka.porodica != plantData.family.name) {
                                biljka.porodica = plantData.family.name
                            }

                            if (!plantData.edible) {
                                biljka.jela = emptyList()
                                if (!biljka.medicinskoUpozorenje.contains(
                                        "NIJE JESTIVO",
                                        ignoreCase = true
                                    )
                                ) {
                                    biljka.medicinskoUpozorenje += " NIJE JESTIVO"
                                }
                            }

                            if (plantData.main_species?.specifications?.toxicity != null &&
                                !biljka.medicinskoUpozorenje.contains("TOKSIČNO", ignoreCase = true)
                            ) {
                                biljka.medicinskoUpozorenje += " TOKSIČNO"
                            }

                            val soilTexture = plantData.main_species?.growth?.soil_texture
                            if (soilTexture != null) {
                                val validSoilTypes = mapOf(
                                    1 to Zemljiste.GLINENO,
                                    2 to Zemljiste.GLINENO,
                                    3 to Zemljiste.PJESKOVITO,
                                    4 to Zemljiste.PJESKOVITO,
                                    5 to Zemljiste.ILOVACA,
                                    6 to Zemljiste.ILOVACA,
                                    7 to Zemljiste.CRNICA,
                                    8 to Zemljiste.CRNICA,
                                    9 to Zemljiste.SLJUNKOVITO,
                                    10 to Zemljiste.KRECNJACKO
                                )
                                val validType = validSoilTypes[soilTexture]
                                biljka.zemljisniTipovi =
                                    if (validType != null) listOf(validType) else emptyList()
                            }

                            val light = plantData.main_species?.growth?.light
                            val humidity = plantData.main_species?.growth?.atmospheric_humidity

                            if (light != null && humidity != null) {
                                val klimatskiTip = getKlimatskiTip(light, humidity)

                                if (klimatskiTip != null) {
                                    biljka.klimatskiTipovi = klimatskiTip
                                }
                            }

                            return@withContext biljka
                        } else {

                            return@withContext biljka
                        }
                    } else {

                        return@withContext biljka
                    }
                } else {
                    return@withContext biljka
                }
            } catch (e: Exception) {
                return@withContext biljka
            }
        }
    }


    suspend fun getPlantswithFlowerColor(flowerColor: String, query: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            try {

                val response: Response<TrefleColorResponse> =
                    apiService.getPlantsWithColor(query, flowerColor, trefleApiToken)
                if (response.isSuccessful) {
                    val plantList = response.body()?.data ?: emptyList()
                    if (plantList.isEmpty()) {
                        return@withContext emptyList()
                    }

                    return@withContext plantList.map {
                        Biljka(
                            0,
                            naziv = it.scientific_name ?: "",
                            porodica = it.family,
                            medicinskoUpozorenje = "",
                            medicinskeKoristi = emptyList(),
                            profilOkusa = null,
                            klimatskiTipovi = listOf(KlimatskiTip.PLANINSKA),
                            zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO),
                            jela = emptyList(),
                        )
                    }
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }
    }


//https://trefle.io/api/v1/plants/99889?token=etDi12gKSdHURSJW9L9edVJH18JWCEl7oQKp-f4FSo0 - milfoil

    private fun getImageBitmapFromUrl(imageUrl: String): Bitmap {
        val inputStream = URL(imageUrl).openStream()
        return BitmapFactory.decodeStream(inputStream)
    }

    fun getDefaultBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Default", width / 2f, height / 2f, paint)

        return bitmap
    }

    fun extractTextInBrackets(input: String): String {
        val startIndex = input.indexOf('(')
        val endIndex = input.indexOf(')')
        return if (startIndex != -1 && endIndex != -1) {
            input.substring(startIndex + 1, endIndex)
        } else {
            input
        }
    }

    private fun getKlimatskiTip(light: Int, humidity: Int): List<KlimatskiTip> {
        // Defining the ranges based on the table from the image
        val climateTypes = mapOf(
            KlimatskiTip.SREDOZEMNA to (6..9 to 1..5),
            KlimatskiTip.TROPSKA to (8..10 to 7..10),
            KlimatskiTip.SUBTROPSKA to (6..9 to 5..8),
            KlimatskiTip.UMJERENA to (4..7 to 3..7),
            KlimatskiTip.SUHA to (7..9 to 1..2),
            KlimatskiTip.PLANINSKA to (0..5 to 3..7)
        )

        val matchingTypes = mutableListOf<KlimatskiTip>()

        for ((tip, ranges) in climateTypes) {
            val (lightRange, humidityRange) = ranges
            if (light in lightRange && humidity in humidityRange) {
                matchingTypes.add(tip)
            }
        }
        return matchingTypes
    }

}