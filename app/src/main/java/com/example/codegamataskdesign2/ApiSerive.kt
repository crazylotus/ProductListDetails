package com.example.codegamataskdesign2

import com.example.codegamatask.datamodal.CategoriesList
import com.example.codegamatask.datamodal.ProductList
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSerive {

    var category: String
    @GET("categories")
    suspend fun getCategories() : Response<CategoriesList>

    @GET("category/{category_name}")
    suspend fun getProductList(@Path("category_name") categoryName : String,@Query("skip") skip : Int,@Query("limit") limit : Int) : Response<ProductList>


    companion object {
        var apiSerive: ApiSerive? = null
        fun getInstance() : ApiSerive {
            if (apiSerive == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://dummyjson.com/products/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                apiSerive = retrofit.create(ApiSerive::class.java)
            }
            return apiSerive!!
        }

    }
}


