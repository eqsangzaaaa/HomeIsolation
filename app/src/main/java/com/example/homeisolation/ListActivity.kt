package com.example.homeisolation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioPlaybackCaptureConfiguration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class ListActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    lateinit var navHostFragment: NavHostFragment
    lateinit var navigationView: NavigationView

    private var mAuth: FirebaseAuth? = null
    lateinit var database: FirebaseDatabase



    lateinit var textUsername:TextView

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0
    var notificationTime = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.firstFragment,R.id.infoFragment,R.id.historyFragment,R.id.chatFragment),drawerLayout)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController,appBarConfiguration)
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setupWithNavController(navController)
        var header:View = navigationView.getHeaderView(0);
        textUsername = header.findViewById(R.id.user_name)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://homeisolationo2auth-default-rtdb.asia-southeast1.firebasedatabase.app/")
        navigationView.findViewById<Button>(R.id.logout).setOnClickListener {
            logout()
        }
        createNotificationChannel()
        getChatData()

        Log.d("ListActivity onCreate", "User Success!")


    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun getChatData(){
        var message = ""
        val intent = Intent(this, ListActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_IMMUTABLE)
        }

        var notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Chat")
            .setSmallIcon(R.drawable.ic_baseline_chat_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)


        val notificationManager = NotificationManagerCompat.from(this)


        database.reference.child("users").child(user!!.uid).child("chat").addValueEventListener(object:
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists() && notificationTime != 1){
                    if(snapshot.children.last().child("user").value.toString().equals("doctor")) {
                        message = snapshot.children.last().child("user").value.toString()+" : "+snapshot.children.last().child("message").value.toString()
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            notification.setContentText(message).build()
                        )
                    }
                }
                notificationTime++
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        updateUI(currentUser)

    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            Log.d("ListActivity", "User Success!"+user)
            textUsername.text = user.displayName
            return
        }
    }

    private fun logout() {
        mAuth!!.signOut()
        val intentSession  = Intent(this,MainActivity::class.java)
        startActivity(intentSession)
    }

}