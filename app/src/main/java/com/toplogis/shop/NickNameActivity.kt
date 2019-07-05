package com.toplogis.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_nick_name.*

class NickNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        if(ed_name.text != null) {
            bt_confirm.setOnClickListener {
                setPreferences("NICKNAME", ed_name.text.toString())
                FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("nickname")
                    .setValue(ed_name.text.toString())
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
