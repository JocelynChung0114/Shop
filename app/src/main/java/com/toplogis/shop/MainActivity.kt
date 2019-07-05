package com.toplogis.shop

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_functions.view.*

class MainActivity : AppCompatActivity() {
    private val RC_SIGNUP = 100
    private val RC_NICKNAME = 101

    val auth = FirebaseAuth.getInstance()
    val functions = listOf<String>(
        "Invite friend",
        "Parking",
        "Movies",
        "Bus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        auth.addAuthStateListener { auth ->
            authChanged(auth)
         }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Spinner
        val colors = arrayOf("White", "Yellow", "Blue")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> constraintLayout.setBackgroundColor(Color.WHITE)
                    1 -> constraintLayout.setBackgroundColor(Color.YELLOW)
                    2 -> constraintLayout.setBackgroundColor(Color.BLUE)
                }
            }
        }

        // Recycle
        recycler_func.layoutManager = LinearLayoutManager(this)
        recycler_func.setHasFixedSize(true)
        recycler_func.adapter = FunctionAdapter()
    }

    inner class FunctionAdapter(): RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_functions, parent, false)
            val holder = FunctionHolder(view)
            return  holder
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.functionName.text = functions.get(position)
            holder.itemView.setOnClickListener {
                functionClicked(position)
            }
        }

    }

    private fun functionClicked(position: Int) {
        when (position) {
            0 -> startActivity(Intent(this, ContactActivity::class.java))
            1 -> startActivity(Intent(this, ParkingActivity::class.java))
            2 -> startActivity(Intent(this, MovieActivity::class.java))
            3 -> startActivity(Intent(this, BusActivity::class.java))
        }
    }

    class FunctionHolder(view:View): RecyclerView.ViewHolder(view) {
        var functionName = view.tv_function;
    }


    private fun authChanged(auth: FirebaseAuth) {
        if(auth.currentUser == null) {
            startActivityForResult(Intent(this, SignUpActivity::class.java), RC_SIGNUP)
        } else {
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(auth.currentUser!!.uid)
                .child("nickname")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        tv_name.text = dataSnapshot.value as String
                    }

                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGNUP) {
            if(resultCode == Activity.RESULT_OK) {
                startActivityForResult(Intent(this, NickNameActivity::class.java), RC_NICKNAME)
            }
        }

        if(requestCode == RC_NICKNAME) {
            if(resultCode == Activity.RESULT_OK) {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
