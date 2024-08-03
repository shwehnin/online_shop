package com.hninhnin.wai

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hninhnin.wai.adapters.CategoryAdapter
import com.hninhnin.wai.libby.Helper.Companion.USER_TOKEN
import com.hninhnin.wai.libby.Helper.Companion.checkAuthUser
import com.hninhnin.wai.libby.Helper.Companion.debugLog
import com.hninhnin.wai.models.Category
import com.hninhnin.wai.services.ServiceBuilder
import com.hninhnin.wai.services.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {
    private lateinit var categoryRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // menu action title
        supportActionBar?.title = "All Categories"
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // check logged user
        if(checkAuthUser()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        categoryRecycler = findViewById(R.id.categoryRecycler)
        categoryRecycler.layoutManager = GridLayoutManager(this, 2)
        loadAllCategory()
    }

    // get all categories
    private fun loadAllCategory() {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCats: Call<List<Category>> = services.getAllCat("Bearer $USER_TOKEN")
        responseCats.enqueue(object: Callback<List<Category>> {
            override fun onResponse(p0: Call<List<Category>>, p1: Response<List<Category>>) {
                if(p1.isSuccessful) {
                    val cats: List<Category> = p1.body()!!
                    categoryRecycler.adapter = CategoryAdapter(this@CategoryActivity,cats)
                }
            }

            override fun onFailure(p0: Call<List<Category>>, p1: Throwable) {
                debugLog(p1.message.toString())
            }

        })
    }

    // show menu item cart on action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item: MenuItem = menu!!.findItem(R.id.cart)
        MenuItemCompat.setActionView(item, R.layout.my_cart_layout)
        return super.onCreateOptionsMenu(menu)
    }
}