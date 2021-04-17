package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.sign

class LogInActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? =null
    var GOOGLE_LOGIN_CODE = 9001

    lateinit var email: EditText
    lateinit var pw: EditText
    lateinit var signBtn: Button
    lateinit var googleBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        email = findViewById(R.id.email_et)
        pw = findViewById(R.id.password_et)
        signBtn = findViewById(R.id.signin_btn)
        googleBtn = findViewById(R.id.google_sign_btn)


        auth = FirebaseAuth.getInstance()

        signBtn.setOnClickListener {
            signInUp()
        }
        googleBtn.setOnClickListener {
            googleLogin()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess){
                var account = result.signInAccount
                firebaseAuthWithGoogle(account)
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                when {
                    it.isSuccessful -> {
                        moveMainPage(it.result?.user)
                    }
                    else -> {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    fun signInUp() {
        auth?.createUserWithEmailAndPassword(email.text.toString(), pw.text.toString())
            ?.addOnCompleteListener {

                when {
                    it.isSuccessful -> {
                        moveMainPage(it.result?.user)
                    }
                    it.exception?.message.isNullOrEmpty() -> {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()

                    }
                    else -> {
                        signInEmail()
                    }
                }
            }
    }

    fun signInEmail() {
        auth?.signInWithEmailAndPassword(email.text.toString(), pw.text.toString())
            ?.addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        moveMainPage(it.result?.user)
                    }
                    else -> {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    fun moveMainPage(user: FirebaseUser?){
        if(user!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}