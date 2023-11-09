package com.example.codegamataskdesign2
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codegamatask.datamodal.Product
import com.example.codegamataskdesign2.databinding.ProductLayoutBinding


class ProductAdapter (context : Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>()  {

    lateinit var context : Context

    init {
        this.context = context
    }

    private var productList = ArrayList<Product>()
    fun setProduct(productList : List<Product>){
        this.productList = productList as ArrayList<Product>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : ProductLayoutBinding) : RecyclerView.ViewHolder(binding.root)  {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = productList[position]

        holder.binding.textViewProductName.text = category.title
        holder.binding.textViewProductDescription.text = category.description
        holder.binding.textViewProductPrice.text = "Price: \$"+category.price

        Glide.with(context).load(category.thumbnail).into(holder.binding.imageViewProduct)

    }


}