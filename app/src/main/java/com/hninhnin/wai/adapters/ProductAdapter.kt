package com.hninhnin.wai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hninhnin.wai.R
import com.hninhnin.wai.SingleProductActivity
import com.hninhnin.wai.models.Product
import com.squareup.picasso.Picasso

class ProductAdapter(val context: Context, val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_row, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.itemView.findViewById<TextView>(R.id.singleCatProductTitle).text = product.name
        Picasso.get().load(product.image).into(holder.itemView.findViewById<ImageView>(R.id.singleCatProductImg))
        holder.itemView.findViewById<TextView>(R.id.singleCatProductPrice).text = product.price.toString()
        holder.itemView.findViewById<Button>(R.id.singleCatProductBtn).setOnClickListener {
            val intent = Intent(context, SingleProductActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }
    }

}