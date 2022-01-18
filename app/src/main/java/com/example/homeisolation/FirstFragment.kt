package com.example.homeisolation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import android.widget.*
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var heart_rate:TextView
lateinit var spo2:TextView
lateinit var txtStatus:TextView
lateinit var sendData:Button

var heartRateTemp = ""
var spo2Temp = ""
var locationTemp = ""


lateinit var mAuth: FirebaseAuth
lateinit var database: FirebaseDatabase
lateinit var user: FirebaseUser

private lateinit var fusedLocationClient: FusedLocationProviderClient


class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard(activity as ListActivity)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://homeisolationo2auth-default-rtdb.asia-southeast1.firebasedatabase.app/")
        user = mAuth!!.currentUser!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_first, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context!!)
        if (ActivityCompat.checkSelfPermission(
                view.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                view.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(view.context!! as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    locationTemp= location.latitude.toString() +","+location.longitude.toString()
                }

            }

        if(user != null){
            Log.d("FirstFragment", "User Sucess!"+user.email)
        }
        heart_rate = view.findViewById(R.id.heart_rate)
        spo2 = view.findViewById(R.id.spo2)
        txtStatus = view.findViewById(R.id.txtStatus)
        sendData = view.findViewById(R.id.sendData)




        database.reference.child("transaction").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("FirstFragment", "User Sucess!"+dataSnapshot.children)
                var status:Boolean = dataSnapshot.child("status").value as Boolean

                if(status){
                    heartRateTemp =  dataSnapshot.child("heart_rate").value.toString()
                    spo2Temp =  dataSnapshot.child("spo2").value.toString()

                    txtStatus.text = "กำลังแสดงค่าที่ได้จากเครื่อง 'Arduino'..."
                    heart_rate.text = "HeartRate "+heartRateTemp.toString() + " BPM"
                    spo2.text = "SpO2 " +spo2Temp.toString()+" %"

                    if(heartRateTemp.toDouble()<50 || heartRateTemp.toDouble()>100){
                        view.findViewById<TextView>(R.id.heart_rate).setTextColor(Color.RED)
                    }else{
                        view.findViewById<TextView>(R.id.heart_rate).setTextColor(Color.GREEN)
                    }
                    if(spo2Temp.toDouble()<90){
                        view.findViewById<TextView>(R.id.spo2).setTextColor(Color.RED)
                    }else if(spo2Temp.toDouble()<95){
                        view.findViewById<TextView>(R.id.spo2).setTextColor(Color.rgb(255,165,0))
                    }else{
                        view.findViewById<TextView>(R.id.spo2).setTextColor(Color.GREEN)

                    }
                    sendData.isEnabled = true
                }else{
                    txtStatus.text = "วางนิ้วที่เครื่อง 'Arduino' เพื่อวัดค่า"
                    heart_rate.text = "HeartRate"
                    spo2.text = "SpO2"
                    sendData.isEnabled = false
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        sendData.setOnClickListener {
            val currentDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
            val databaseReference = database.reference.child("users").child(user.uid).child("transaction").push()
            databaseReference.child("location").setValue(locationTemp)
            databaseReference.child("timestamp").setValue(currentDate)
            databaseReference.child("heart_rate").setValue(heartRateTemp)
            databaseReference.child("spo2").setValue(spo2Temp)
            Toast.makeText(view.context,"ส่งข้อมูลเรียบร้อยแล้ว\nHeartRate : $heartRateTemp BPM, SpO2 : $spo2Temp %", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}