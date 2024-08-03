package com.hninhnin.wai

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hninhnin.wai.adapters.CartAdapter
import com.hninhnin.wai.libby.Helper
import com.hninhnin.wai.libby.Helper.Companion.USER_TOKEN
import com.hninhnin.wai.libby.Helper.Companion.debugLog
import com.hninhnin.wai.models.CartProduct
import com.hninhnin.wai.models.ErrorResponse
import com.hninhnin.wai.services.ServiceBuilder
import com.hninhnin.wai.services.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCartActivity : AppCompatActivity() {
    var cartCount: TextView? = null
    private lateinit var cartRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // add action bar title
        supportActionBar?.title = "My Cart's Item"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // get all item
        val cartKeys = Helper.getAllKeys()

        Helper.debugLog("Cart Items $cartKeys")

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // cart product layout
        cartRecycler = findViewById(R.id.cartRecycler)
        cartRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        getCartProducts(cartKeys)
    }

    // get cart products
    private fun getCartProducts(cartKeys: String) {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCartProducts : Call<List<CartProduct>> = services.getCartPreviewItems("Bearer ${USER_TOKEN}", cartKeys)
        responseCartProducts.enqueue(object : Callback<List<CartProduct>> {
            override fun onResponse(p0: Call<List<CartProduct>>, p1: Response<List<CartProduct>>) {
                if(p1.isSuccessful) {
                    val products : List<CartProduct> = p1.body()!!
                    cartRecycler.adapter = CartAdapter(this@MyCartActivity, products)
                    Helper.debugLog("Product size ${products.size}")

                }else {
                    Helper.debugLog("Something is not right")
                }
            }

            override fun onFailure(p0: Call<List<CartProduct>>, p1: Throwable) {
                Helper.debugLog(p1.message.toString())
            }

        })
    }

    // create menu action
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // check out bill
    private fun billOut() {
        var cartKeys = Helper.getAllKeys()
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCartOrder: Call<ErrorResponse> = services.billOutOrder("Bearer $USER_TOKEN", cartKeys)
        responseCartOrder.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(p0: Call<ErrorResponse>, p1: Response<ErrorResponse>) {
                if(p1.isSuccessful) {
                    val message : ErrorResponse = p1.body()!!
                    Toast.makeText(this@MyCartActivity, message.msg, Toast.LENGTH_SHORT).show()
                    // remove cart after checkout
                    Helper.clearCart()
                }else {
                    debugLog("Something went wrong")
                }
            }

            override fun onFailure(p0: Call<ErrorResponse>, p1: Throwable) {
                debugLog(p1.message.toString())
            }

        })
    }

    // menu item option selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.billOut) { // click checkout menu icon
            billOut()
        }else if(item.itemId == R.id.productUpload) { // click upload menu icon
            // go to product upload page
            val intent = Intent(this, ProductUploadActivity::class.java)
            startActivity(intent)
        }
        // app action bar back button
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)

        }

    }
}