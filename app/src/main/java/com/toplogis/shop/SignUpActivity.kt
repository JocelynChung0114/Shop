package com.toplogis.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.info
import org.jetbrains.anko.okButton

class SignUpActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        bt_signup.setOnClickListener {
            val email = ed_email.text.toString()
            val pwd = ed_pwd.text.toString()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        alert("Account created!!", "Sign up") {
                            okButton {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }.show()
                    } else {
                        alert(it.exception?.message.toString(), "Sign up") {
                            okButton {
                            }
                        }.show()
                    }
                }
        }
    }
}
