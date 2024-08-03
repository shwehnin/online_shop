package com.hninhnin.wai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnAlreadyLogin : Button
    private lateinit var btnRegister: Button
    private lateinit var txtRegisterName: EditText
    private lateinit var txtRegisterEmail: EditText
    private lateinit var txtRegisterPsw: EditText
    private lateinit var btnRegisterCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnAlreadyLogin = findViewById(R.id.btnAlreadyLogin)
        btnRegister = findViewById(R.id.btnRegister)
        txtRegisterName = findViewById(R.id.txtRegisterName)
        txtRegisterEmail = findViewById(R.id.txtRegisterEmail)
        txtRegisterPsw = findViewById(R.id.txtRegisterPsw)
        btnRegisterCancel = findViewById(R.id.btnRegisterCancel)

        // click register button
        btnRegister.setOnClickListener {
            val userName = txtRegisterName.text
            val email = txtRegisterEmail.text
            val password = txtRegisterPsw.text
        }

        // clear input field when clicked cancel button
        btnRegisterCancel.setOnClickListener {
            txtRegisterName.text = null
            txtRegisterEmail.text = null
            txtRegisterPsw.text = null
        }

        // go to login page
        btnAlreadyLogin.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }
}