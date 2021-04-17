package com.example.instaclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class LogInActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    lateinit var email: EditText
    lateinit var pw: EditText
    lateinit var signBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        email = findViewById(R.id.email_et)
        pw = findViewById(R.id.password_et)
        signBtn = findViewById(R.id.signin_btn)

        auth = FirebaseAuth.getInstance()
    }

    fun signInUp() {
        auth?.createUserWithEmailAndPassword(email.text.toString(), pw.text.toString())
            ?.addOnCompleteListener {

                when {
                    it.isSuccessful -> {

                    }
                    it.exception?.message.isNullOrEmpty() -> {

                    }
                    else -> {

                    }
                }
            }
    }

    fun signInEmail() {
        auth?.signInWithEmailAndPassword(email.text.toString(), pw.text.toString())
            ?.addOnCompleteListener {
                when {
                    it.isSuccessful -> {

                    }
                    else -> {

                    }
                }
            }
    }
}