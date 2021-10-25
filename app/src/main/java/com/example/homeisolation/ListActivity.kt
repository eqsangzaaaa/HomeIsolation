package com.example.homeisolation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ListActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    lateinit var buttonLogout: Button
    lateinit var txtUsername:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        buttonLogout = findViewById<Button>(R.id.buttonLogout)
        txtUsername = findViewById<TextView>(R.id.txtUsername)
        mAuth = FirebaseAuth.getInstance()

        buttonLogout!!.setOnClickListener {
            logout()
        }
        
    }

    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            txtUsername.setText(user.email)
            return
        }else{
            val intentSession  = Intent(this,MainActivity::class.java)
            startActivity(intentSession)
        }
    }

    private fun logout() {
        mAuth!!.signOut()
        val intentSession  = Intent(this,MainActivity::class.java)
        startActivity(intentSession)
    }

}