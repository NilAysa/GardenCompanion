package com.spirala1

data class TrefleImageResponse(
    val data: List<Plants>
)

data class Plants(
    val id: Int,
    val image_url: String,
)

