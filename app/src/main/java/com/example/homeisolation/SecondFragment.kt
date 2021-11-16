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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_second, container, false)
        user = mAuth!!.currentUser
//        txtUsername = view.findViewById<TextView>(R.id.txtUsername) as TextView
        txtName = view.findViewById<EditText>(R.id.txtName)
        txtAddress = view.findViewById<EditText>(R.id.address)
        txtPhone = view.findViewById<EditText>(R.id.phone)
        buttonUpdate = view.findViewById<Button>(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            hideKeyboard()
            updateAccount()

        }
        if(user != null){
            getAccount()

        }
        // Inflate the layout for this fragment

        return view
    }

    private fun updateAccount() {
        database.reference.child("users").child(user!!.uid).setValue(User(user!!.uid,
            user!!.email,txtName.text.toString(),txtAddress.text.toString(),txtPhone.text.toString())).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("update", "Update success ${it}")
                Toast.makeText(getActivity(),"แก้ไขข้อมูลเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                getAccount()
            }

        }
    }

    private fun getAccount(){
        database.reference.child("users").child(user!!.uid).get().addOnSuccessListener {
            if(it.exists()){
                Log.d("firebase", "Got value ${it}")
                txtName.setText(it.child("name").value.toString())
                txtAddress.setText(it.child("address").value.toString())
                txtPhone.setText(it.child("phone").value.toString())
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

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}