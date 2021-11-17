package com.example.homeisolation

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonRegister: Button
    lateinit var buttonForget: Button

    lateinit var email:String
    lateinit var password:String

    private var mAuth: FirebaseAuth? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val PERMISSION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(permissions, PERMISSION_REQUEST)
        txtEmail = findViewById<EditText>(R.id.txtEmail)
        txtPassword = findViewById<EditText>(R.id.txtPassword)
        buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonRegister = findViewById<Button>(R.id.buttonRegister)
        buttonForget = findViewById<Button>(R.id.buttonForget)


        mAuth = FirebaseAuth.getInstance()

        buttonRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            if(TextUtils.isEmpty(txtEmail.text.toString())){
                Toast.makeText(applicationContext,"กรุณากรอก Email",Toast.LENGTH_SHORT).show()
            }else {
                if(TextUtils.isEmpty(txtPassword.text.toString())){
                    Toast.makeText(applicationContext,"กรุณากรอกรหัสผ่าน",Toast.LENGTH_SHORT).show()
                }else {
                    signIn()
                }
            }
        }

        buttonForget.setOnClickListener {
            val intent = Intent(this,ForgetActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {
        email = txtEmail.text.toString()
        password = txtPassword.text.toString()
        mAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                task -> if (task.isSuccessful){
            Log.d("MyApp","SignIn SuccessFul!")
            val user = mAuth!!.currentUser
            updateUI(user)
        }else{
            Log.w("MyApp","Failure Process",task.exception)
            Toast.makeText(this@MainActivity,"ไม่พบข้อมูลบัญชีผู้ใช้", Toast.LENGTH_SHORT).show()
            updateUI(null)
        }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val displayName = user.displayName
            Toast.makeText(this@MainActivity,"สวัสดี : $displayName", Toast.LENGTH_SHORT).show()
            val intentSession  = Intent(this,ListActivity::class.java)
            startActivity(intentSession)
        }
    }
}