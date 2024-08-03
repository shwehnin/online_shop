package com.hninhnin.wai

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hninhnin.wai.adapters.ProductAdapter
import com.hninhnin.wai.libby.Helper
import com.hninhnin.wai.libby.Helper.Companion.USER_TOKEN
import com.hninhnin.wai.libby.Helper.Companion.debugLog
import com.hninhnin.wai.models.Products
import com.hninhnin.wai.services.ServiceBuilder
import com.hninhnin.wai.services.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleCategoryProduct : AppCompatActivity() {
    private lateinit var singleCategoryProductRecycler: RecyclerView
     var cartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Single Category Product"

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_single_category_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // check logged user
        if(Helper.checkAuthUser()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        var bundle : Bundle? = intent.extras
        var catId = bundle?.getString("cat_id")
        singleCategoryProductRecycler = findViewById(R.id.singleCategoryProductRecycler)
//        Toast.makeText(this, catId, Toast.LENGTH_SHORT).show()
        singleCategoryProductRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        loadAllProductByCategory(catId.toString())
    }

    //for actionbar back button
    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }

    // product by category
    private fun loadAllProductByCategory(catId: String) {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseProduct : Call<Products> = services.getProductByCategory("Bearer $USER_TOKEN",catId)
        responseProduct.enqueue(object : Callback<Products> {
            override fun onResponse(p0: Call<Products>, p1: Response<Products>) {
                if(p1.isSuccessful) {
                    val productResponse : Products = p1.body()!!
                    val products = productResponse.products
                    singleCategoryProductRecycler.adapter = ProductAdapter(this@SingleCategoryProduct, products)
//                    debugLog("There are ${products.size}")
                }else {
                    debugLog("Something went wrong")
                }
            }

            override fun onFailure(p0: Call<Products>, p1: Throwable) {
                debugLog(p1.message.toString())
            }

        })
    }

    // show menu item cart on action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item: MenuItem = menu!!.findItem(R.id.cart)
        MenuItemCompat.setActionView(item, R.layout.my_cart_layout)
        val cartView = MenuItemCompat.getActionView(item)
        // cart count
        cartCount = cartView.findViewById(R.id.cartCount)
        // cart image
        val cartImage = cartView.findViewById<ImageView>(R.id.cartImage)
        cartImage.setOnClickListener {
            // got to my cart page
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }
        return super.onCreateOptionsMenu(menu)
    }
}