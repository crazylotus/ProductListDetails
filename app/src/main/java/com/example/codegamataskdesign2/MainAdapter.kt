package com.example.codegamataskdesign2
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.codegamatask.datamodal.Category
import com.example.codegamataskdesign2.databinding.CategoryLayoutBinding


class MainAdapter(context : Context,listener : OnItemClickListerner) : RecyclerView.Adapter<MainAdapter.ViewHolder>()  {

    lateinit var context : Context
    lateinit var listener: OnItemClickListerner
    init {
        this.context = context
        this.listener = listener
    }

    private var categoryList = ArrayList<Category>()
    fun setMovieList(categoryList : List<Category>){
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : CategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)  {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = categoryList[position]
        holder.binding.category.text = category.title

        if(category.selected){
            holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.category.setBackground(ContextCompat.getDrawable(context, R.drawable.select_item))
            holder.binding.category.setPadding(20,20,20,20)
        }else{
            holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.binding.category.setBackground(ContextCompat.getDrawable(context, R.drawable.un_select_item))
            holder.binding.category.setPadding(20,20,20,20)
        }

        holder.binding.category.setOnClickListener {
            if(category.selected)
                return@setOnClickListener
            else{
                categoryList.apply {
                    for(item in this)
                        item.selected = false
                }
                categoryList[position].selected = true
               notifyDataSetChanged()
                listener.onProductClicked(position)
            }

        }
    }

    interface OnItemClickListerner
    {
        fun onProductClicked(position: Int)
    }

}