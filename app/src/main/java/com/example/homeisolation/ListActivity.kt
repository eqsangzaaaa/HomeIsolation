package com.example.homeisolation

import android.content.Intent
import android.media.AudioPlaybackCaptureConfiguration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    lateinit var navigationView: NavigationView

    private var mAuth: FirebaseAuth? = null

    lateinit var textUsername:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.firstFragment,R.id.secondFragment),drawerLayout)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController,appBarConfiguration)
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setupWithNavController(navController)
        var header:View = navigationView.getHeaderView(0);
        textUsername = header.findViewById(R.id.user_name)

        mAuth = FirebaseAuth.getInstance()
        navigationView.findViewById<Button>(R.id.logout).setOnClickListener {
            logout()
        }

        Log.d("ListActivity onCreate", "User Success!")


    }

    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        updateUI(currentUser)

    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            Log.d("ListActivity", "User Success!"+user)
            textUsername.text = user.email
            return
        }
    }

    private fun logout() {
        mAuth!!.signOut()
        val intentSession  = Intent(this,MainActivity::class.java)
        startActivity(intentSession)
    }

}