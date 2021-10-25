package com.example.homeisolation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {
    lateinit var txtEmailForget:EditText
    lateinit var buttonReset:Button
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        mAuth = FirebaseAuth.getInstance()
        txtEmailForget = findViewById<EditText>(R.id.txtEmailForget)
        buttonReset = findViewById<Button>(R.id.buttonReset)

        buttonReset!!.setOnClickListener {
            val email = txtEmailForget.text.toString()
            if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please Enter your Email!!",Toast.LENGTH_SHORT).show()
            }else{
                mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener {
                    task -> if(task.isSuccessful){
                    Toast.makeText(this@ForgetActivity,"Please Check your Email",Toast.LENGTH_SHORT).show()
                    }else{
                    Toast.makeText(this@ForgetActivity,"fail to send reset password email",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}