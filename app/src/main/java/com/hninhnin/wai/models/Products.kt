package com.hninhnin.wai.models

import com.google.gson.annotations.SerializedName

class Products(
    val currentPage: Int,
    @SerializedName("data")
    val products: List<Product>
)