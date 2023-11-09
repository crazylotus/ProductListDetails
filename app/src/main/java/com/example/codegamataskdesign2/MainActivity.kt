package com.example.codegamataskdesign2
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.codegamatask.datamodal.Category
import com.example.codegamatask.datamodal.Product
import com.example.codegamatask.datamodal.ProductList
import com.example.codegamataskdesign2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),MainAdapter.OnItemClickListerner {

    private lateinit var binding : ActivityMainBinding

    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter
    lateinit var productAdapter : ProductAdapter

    var categoriesList : ArrayList<Category> = ArrayList()
    var productList : ArrayList<Product> = ArrayList()

    var skip = 0
    var selectedPosition = 0
    lateinit var product : Product
    lateinit var productDetails : ProductList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiSerive = ApiSerive.getInstance()
        val mainRepository = Repository(apiSerive)
        adapter = MainAdapter(this,this)
        productAdapter = ProductAdapter(this)

        var layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var layoutManager2 = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        binding.rvCategorylist.layoutManager = layoutManager
        binding.rvCategorylist.adapter = adapter

        binding.rvProductList.layoutManager = layoutManager2
        binding.rvProductList.adapter = productAdapter

        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainViewModel::class.java]


        viewModel.categoriesList.observe(this) {

            skip = 0
            categoriesList.clear()
            for (item in it) {
                var categoryitem = item.capitalize().replace("-"," ")
                val category = Category(categoryitem,item)
                categoriesList.add(category)
            }
            categoriesList[0].selected=true
            adapter.setMovieList(categoriesList)

            viewModel.getProducts(categoriesList[0].key,skip,1)
            Log.e("MainActivity","category fetched")
        }

        viewModel.productList.observe(this){

            productList.clear()

            productList.addAll(it.products)
            productAdapter.setProduct(productList)
            Log.e("MainActivity","product fetched")
            Log.e("MainActivity","product items count ${productList.size}")
            productDetails = it
            product = it.products[0]

            binding.productTitleTextView.text=product.title
            binding.productDescriptionTextView.text=product.description
            binding.productPriceTextView.text = "Price: \$"+product.price

            Glide.with(this).load(product.thumbnail).into(binding.productImageView)

            if(productDetails.total-1==skip)
                binding.skipButton.visibility = View.GONE
            else
                binding.skipButton.visibility = View.VISIBLE

        }

        binding.skipButton.setOnClickListener {
            skip++
            if(skip<productDetails.total)
            viewModel.getProducts(categoriesList[selectedPosition].key,skip,1)
        }

        binding.addToCartButton.setOnClickListener {
            showSuccessDialog(this,"Item added in the cart")
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
        viewModel.getAllCategories()
    }

    fun showSuccessDialog(context: Context, message: String, onDismiss: () -> Unit = {}) {
        val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)

        val dialogTitle = view.findViewById<TextView>(R.id.dialogTitle)
        dialogTitle.text = "Success"

        val dialogMessage = view.findViewById<TextView>(R.id.dialogMessage)
        dialogMessage.text = message
        val alertDialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()
        val okButton = view.findViewById<TextView>(R.id.okButton)
        okButton.setOnClickListener {
            onDismiss.invoke()
            alertDialog.dismiss()
        }



        alertDialog.show()
    }

    override fun onProductClicked(position: Int) {
        skip = 0
        selectedPosition = position
        viewModel.getProducts(categoriesList[position].key,skip,1)
    }
}