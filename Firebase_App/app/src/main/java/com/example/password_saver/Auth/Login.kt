package com.example.password_saver.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.password_saver.MainActivity
import com.example.password_saver.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn = findViewById<Button>(R.id.btn_entrar)
        val txt_cadastar = findViewById<TextView>(R.id.txt_btn_cadastar)


        firebaseAuth = FirebaseAuth.getInstance()
        txt_cadastar.setOnClickListener{
            val intent = Intent(this,Cadastro::class.java)
            startActivity(intent)
        }

        btn.setOnClickListener {

            val email = findViewById<EditText>(R.id.txt_email).text.toString()
            val senha = findViewById<EditText>(R.id.txt_senha).text.toString()
            val Email  = email.split("@")[0]
            var word = ""

            if(Email.contains(".")){
                val email2 = email.split(".")[0]
                val email3 = email.split(".")[1].split("@")[0]
                word = email2 + email3
                Log.d("valueFirebase","$word")

            }else{
                word = Email

            }

            val path = email.split("@")[1].split(".")[0]

            val user = (word + path)


            if( email.isNotEmpty() && senha.isNotEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(email,senha).addOnSuccessListener {
                        //Todo() pode dar erro aqui

                        val intent = Intent(this, MainActivity::class.java )
                        Log.d("value","$user")
                        intent.putExtra("UserFirebase","${user}")
                        startActivity(intent)

                    }

            }else{
                Toast.makeText(this,"Campos Vazios não são permitidos", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this,"Entrando...",Toast.LENGTH_SHORT).show()

        }


    }
}