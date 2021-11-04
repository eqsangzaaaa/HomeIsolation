package com.example.homeisolation

import android.content.Intent
import android.media.AudioPlaybackCaptureConfiguration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ListActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    lateinit var navHostFragment: NavHostFragment

    private var mAuth: FirebaseAuth? = null
    lateinit var buttonLogout: Button
    lateinit var txtUsername:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        drawerLayout = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        toolbar.setupWithNavController(navController,appBarConfiguration)

        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)


//        buttonLogout = findViewById<Button>(R.id.buttonLogout)
//        txtUsername = findViewById<TextView>(R.id.user_name)
        mAuth = FirebaseAuth.getInstance()

//        buttonLogout!!.setOnClickListener {
//            logout()
//        }
        
    }

    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
//            txtUsername.setText(user.email)
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