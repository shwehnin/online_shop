package com.hninhnin.wai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hninhnin.wai.R
import com.hninhnin.wai.SingleCategoryProduct
import com.hninhnin.wai.models.Category

class CategoryAdapter(val context: Context, val cats: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row, parent, false))
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = cats[position]
        holder.itemView.findViewById<TextView>(R.id.categoryName).text = cat.name
        holder.itemView.findViewById<TextView>(R.id.categoryName).setOnClickListener {
            val intent = Intent(context, SingleCategoryProduct::class.java)
            intent.putExtra("cat_id", cat.id.toString())
            context.startActivity(intent)
        }
    }
}