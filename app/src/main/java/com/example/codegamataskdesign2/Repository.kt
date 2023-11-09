package com.example.codegamataskdesign2

import com.example.codegamatask.datamodal.ProductList
import retrofit2.Response

class Repository constructor(private val apiSerive: ApiSerive) {

    suspend fun getCategoriesList() = apiSerive.getCategories()

    suspend fun getProducts( product : String,skip:Int,limit:Int) : Response<ProductList>  {
        return apiSerive.getProductList(product,skip,limit)
    }

}