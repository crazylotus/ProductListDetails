package com.example.codegamataskdesign2
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codegamatask.datamodal.ProductList

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel constructor(private val mainRepository: Repository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val categoriesList = MutableLiveData<List<String>>()
    val productList = MutableLiveData<ProductList>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllCategories() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getCategoriesList()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    categoriesList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    fun getProducts(product : String,skip: Int,limit: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getProducts(product,skip,limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}