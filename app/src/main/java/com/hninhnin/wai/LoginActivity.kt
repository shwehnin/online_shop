package com.hninhnin.wai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hninhnin.wai.libby.Helper.Companion.USER_TOKEN
import com.hninhnin.wai.libby.Helper.Companion.debugLog
import com.hninhnin.wai.models.Category
import com.hninhnin.wai.models.Token
import com.hninhnin.wai.services.ServiceBuilder
import com.hninhnin.wai.services.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var btnNotMember: Button
    private lateinit var btnLogin: Button
    private lateinit var txtEmailLogin: EditText
    private lateinit var txtPasswordLogin: EditText
    private lateinit var btnLoginCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnNotMember = findViewById(R.id.btnNotMember)
        btnLogin = findViewById(R.id.btnLogin)
        txtEmailLogin = findViewById(R.id.txtEmailLogin)
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin)
        btnLoginCancel = findViewById(R.id.btnLoginCancel)

        // create login
        btnLogin.setOnClickListener {
            val email = txtEmailLogin.text.trim().toString()
            val password = txtPasswordLogin.text.trim().toString()
            loginUser(email, password)
        }

        // go to register page
        btnNotMember.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // click btn login
        btnLoginCancel.setOnClickListener {
            txtEmailLogin.text = null
            txtPasswordLogin.text = null
        }

    }

    // login
    private fun loginUser(email: String, password: String) {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseLogin: Call<Token> = services.loginUser(email, password)
        debugLog("Response Login is $responseLogin")

        responseLogin.enqueue(object: Callback<Token> {
            override fun onResponse(p0: Call<Token>, p1: Response<Token>) {
                val token: Token = p1.body()!!
                debugLog("My token $token")
                USER_TOKEN = token.token
                val intent = Intent(this@LoginActivity, CategoryActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(p0: Call<Token>, p1: Throwable) {
                debugLog(p1.message.toString())
            }

        })
    }
}