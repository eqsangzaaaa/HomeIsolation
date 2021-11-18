package com.example.homeisolation

import android.R.attr
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.text.Editable

import android.R.attr.button

import android.text.TextWatcher





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
    lateinit var recyclerViewChat: RecyclerView
    lateinit var arrayList: ArrayList<ChatModel>
    var arrayAdapter: ArrayAdapter<String>? = null

    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var user: FirebaseUser
    lateinit var buttonSend: Button
    lateinit var txtMessage:EditText

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
        val view =  inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerViewChat = view.findViewById(R.id.RecyclerViewChat)
        buttonSend = view.findViewById(R.id.buttonSend)
        txtMessage = view.findViewById(R.id.chatText)
        recyclerViewChat.layoutManager = LinearLayoutManager(getActivity())
        recyclerViewChat.setHasFixedSize(true)
        arrayList = arrayListOf<ChatModel>()
        getChatData()

        buttonSend.setEnabled(false);
        txtMessage.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isEmpty()) {
                    buttonSend.setEnabled(false)
                } else {
                    buttonSend.setEnabled(true)
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        buttonSend.setOnClickListener {
            val currentDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
            val databaseReference =
                database.reference.child("users").child(user.uid).child("chat").push()
            databaseReference.child("user").setValue("ฉัน")
            databaseReference.child("message").setValue(txtMessage.text.toString())
            databaseReference.child("timestamp").setValue(currentDate)
            txtMessage.setText("")
        }
        return view
    }

    fun getChatData(){
        database.reference.child("users").child(user!!.uid).child("chat").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                if(snapshot.exists()){

                    for (chatSnapshot in snapshot.children){
//                        Log.d("update", "Update success ${chatSnapshot.child("user").value.toString()}")
                        var chat = chatSnapshot.getValue(ChatModel::class.java)
                        arrayList.add(chat!!)
                    }
//                    Log.d("update", "Update success ${arrayList.size}")
                    recyclerViewChat.adapter = ChatAdapter(arrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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