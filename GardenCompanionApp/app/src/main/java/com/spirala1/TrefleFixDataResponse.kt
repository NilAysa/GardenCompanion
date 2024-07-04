package com.spirala1

data class TrefleFixDataResponse(
    val data: PlantData
)

data class PlantData(
    val id: Int,
    val common_name: String,
    val family_name: Family,
    val edible: Boolean,
    val main_species: MainSpecies?,
    val family: Family?
)

data class Family(
    val name: String
)

data class MainSpecies(
    val specifications: Specifications?,
    val growth: Growth?
)

data class Specifications(
    val toxicity: String?,
)

data class Growth(
    val soil_texture: Int?,
    val light: Int,
    val atmospheric_humidity: Int
)