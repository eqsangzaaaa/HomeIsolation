package com.example.homeisolation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var txtName: EditText
    lateinit var txtAddress: EditText
    lateinit var txtPhone: EditText
    lateinit var buttonUpdate: Button
    var user: FirebaseUser? = null;

    lateinit var database: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard(activity as ListActivity)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://homeisolationo2auth-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_second, container, false)
        user = mAuth!!.currentUser
        txtName = view.findViewById<EditText>(R.id.txtName)
        txtAddress = view.findViewById<EditText>(R.id.address)
        txtPhone = view.findViewById<EditText>(R.id.phone)
        buttonUpdate = view.findViewById<Button>(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            hideKeyboard(activity as ListActivity)
            updateAccount()
        }
        if(user != null){
            getAccount()
        }
        return view
    }

    private fun updateAccount() {
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(txtName.text.toString().trim()).build()
            user!!.updateProfile(profileUpdate)
                .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    if (task.isSuccessful) {
                        var userUpdate = mapOf<String,String>(
                            "name" to txtName.text.toString(),
                            "address" to txtAddress.text.toString(),
                            "phone" to txtPhone.text.toString(),
                        )
                        database.getReference("users").child(user!!.uid).updateChildren(userUpdate).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("update", "Update success ${it}")
                                Toast.makeText(getActivity(),"แก้ไขข้อมูลเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                                getAccount()
                            }else{
                                Log.d("update", "Failure ",task.exception)
                            }
                        }
                    }else{
                        Log.w("MyApp", "Failure Process", task.exception)
                    }
                })
    }

    private fun getAccount(){
        database.reference.child("users").child(user!!.uid).get().addOnSuccessListener {
            if(it.exists()){
                Log.d("firebase", "Got value ${it}")
                txtName.setText(it.child("name").value.toString())
                txtAddress.setText(it.child("address").value.toString())
                txtPhone.setText(it.child("phone").value.toString())

                val listActivity: ListActivity = activity as ListActivity
                listActivity.findViewById<TextView>(R.id.user_name).text = it.child("name").value.toString()
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
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