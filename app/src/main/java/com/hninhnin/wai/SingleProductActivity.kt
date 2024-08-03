package com.hninhnin.wai

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hninhnin.wai.libby.Helper
import com.hninhnin.wai.models.Product
import com.squareup.picasso.Picasso

class SingleProductActivity : AppCompatActivity() {
    private lateinit var productTitle : TextView
    private lateinit var productImage : ImageView
    private lateinit var productPrice: TextView
    private lateinit var productDesc: TextView
    private lateinit var addToCartImage: ImageView

    var cartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Product Details"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_single_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        productTitle = findViewById(R.id.productTitle)
        productImage = findViewById(R.id.productImage)
        productPrice = findViewById(R.id.productPrice)
        productDesc = findViewById(R.id.productDesc)
        addToCartImage = findViewById(R.id.addToCartImage)

        val product = intent.getParcelableExtra<Product>("product")
        productTitle.text = product?.name
        Picasso.get().load(product?.image).into(productImage)
        productPrice.text = product?.price.toString()
        productDesc.text = product?.description

        // click cart image
        addToCartImage.setOnClickListener {
            Helper.addToCart(product!!.id)
            cartCount?.text =  Helper.getCartCount().toString()
        }
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

        // add cart count
        cartCount?.text =  Helper.getCartCount().toString()

        cartImage.setOnClickListener {
            // got to my cart page
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }
        return super.onCreateOptionsMenu(menu)
    }
}