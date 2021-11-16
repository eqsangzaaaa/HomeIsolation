package com.example.homeisolation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var listView: ListView? = null
    var arrayList: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null

    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val view =  inflater.inflate(R.layout.fragment_chat, container, false)
        listView = view.findViewById(R.id.listViewChat)
        database.reference.child("users").child(user!!.uid).child("chat").get().addOnSuccessListener {
            if(it.exists()){
                Log.d("firebase", "Got value ${it}")

            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}