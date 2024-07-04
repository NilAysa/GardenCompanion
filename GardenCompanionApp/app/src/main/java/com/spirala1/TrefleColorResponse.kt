package com.spirala1

data class TrefleColorResponse(
    val data: List<PlantWithFlowerColor>
)

data class PlantWithFlowerColor(
    val common_name: String,
    val family: String,
    val scientific_name:String
)
