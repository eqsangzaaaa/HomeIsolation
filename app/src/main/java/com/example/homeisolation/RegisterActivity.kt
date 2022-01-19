package com.example.homeisolation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.UserProfileChangeRequest


import com.google.android.gms.tasks.OnCompleteListener







class RegisterActivity : AppCompatActivity() {
    lateinit var txtEmailCreate: EditText
    lateinit var txtPasswordCreate: EditText
    lateinit var txtName: EditText
    lateinit var txtAddress: EditText
    lateinit var txtPhone: EditText

    lateinit var buttonSubmit: Button

    lateinit var email:String
    lateinit var password:String
    lateinit var name:String
    lateinit var address:String
    lateinit var phone:String

    private var mAuth: FirebaseAuth? = null
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtEmailCreate = findViewById<EditText>(R.id.txtEmailCreate)
        txtPasswordCreate = findViewById<EditText>(R.id.txtPasswordCreate)
        txtName = findViewById<EditText>(R.id.txtName)
        txtAddress = findViewById<EditText>(R.id.address)
        txtPhone = findViewById<EditText>(R.id.phone)

        buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://homeisolationo2auth-default-rtdb.asia-southeast1.firebasedatabase.app/")

        buttonSubmit.setOnClickListener {
            if(TextUtils.isEmpty(txtEmailCreate.text.toString()) || TextUtils.isEmpty(txtPasswordCreate.text.toString())){ //Email-กับ password ห้ามว่าง
                Toast.makeText(applicationContext,"Please Enter your E-mail and password",Toast.LENGTH_SHORT).show()
            }else {
                createAccount()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun createAccount() {
        email = txtEmailCreate.text.toString()
        password = txtPasswordCreate.text.toString()
        name = txtName.text.toString()
        address = txtAddress.text.toString()
        phone = txtPhone.text.toString()

        mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {//รหัสผ่านต้องยาว8ตัว

                Log.d("MyApp", "Create New User Success!")
                val user = mAuth!!.currentUser
                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name.trim()).build()
                user!!.updateProfile(profileUpdate)
                    .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            database.reference.child("users").child(user!!.uid).setValue(UserModel(user.uid,user.email,name,address,phone))
                            updateUI(user)
                        }else{
                            Log.w("MyApp", "Failure Process", task.exception)
                        }
                    })

            } else {
                Log.w("MyApp", "Failure Process", task.exception)
                Toast.makeText(this@RegisterActivity, "Authentication Failed", Toast.LENGTH_SHORT)
                    .show()
                updateUI(null)
            }
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val displayName = user.displayName
            Toast.makeText(this@RegisterActivity,"สวัสดี : $displayName",Toast.LENGTH_SHORT).show()
            val intentSession  = Intent(this,ListActivity::class.java)
            startActivity(intentSession)
        }
    }
}