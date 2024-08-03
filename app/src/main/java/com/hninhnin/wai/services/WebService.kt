package com.hninhnin.wai.services

import com.hninhnin.wai.models.CartProduct
import com.hninhnin.wai.models.Category
import com.hninhnin.wai.models.ErrorResponse
import com.hninhnin.wai.models.FileInfo
import com.hninhnin.wai.models.Products
import com.hninhnin.wai.models.Token
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface WebService {

    // get all carts
    @GET("cats")
    fun getAllCat(@Header("Authorization") token: String) : Call<List<Category>>

    // user login
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Token>

    // get products by category
    @GET("product/cat/{id}")
    fun getProductByCategory(
        @Header("Authorization") token: String,
        @Path("id") id: String) : Call<Products>

    // get my carts
    @FormUrlEncoded
    @POST("previewCart")
    fun getCartPreviewItems(
        @Header("Authorization") token: String,
        @Field("items") items: String) : Call<List<CartProduct>>

    @FormUrlEncoded
    @POST("order")
    fun billOutOrder(
        @Header("Authorization") token: String,
        @Field("orders") orders: String
    ) : Call<ErrorResponse>

    @Multipart
    @POST("imageUpload")
    fun imageUpload(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ) : Call<FileInfo>

    @FormUrlEncoded
    @POST("newProduct")
    fun newProductUpload(
        @Header("Authorization") token: String,
        @Field("cart_id") cartId: Int,
        @Field("name") name: String,
        @Field("price") price: Double,
        @Field("image") image: String,
        @Field("description") description: String,
    ) : Call<ErrorResponse>
}