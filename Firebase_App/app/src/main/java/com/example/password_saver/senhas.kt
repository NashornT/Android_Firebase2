package com.example.password_saver

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.password_saver.Dados.Senha
import com.google.firebase.database.*
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class senhas : AppCompatActivity() {

    private lateinit var  dbref: DatabaseReference
    private lateinit var  recycleView: RecyclerView
    private lateinit var senhaArrayList: ArrayList<Senha>
    private lateinit var  Adapter: Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senhas)

        recycleView = findViewById(R.id.recycle)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)

        senhaArrayList = arrayListOf()

        Adapter = Adapter(senhaArrayList)

        recycleView.adapter = Adapter

        getUserData()


    }



   private fun getUserData(){
       val getUserpath = intent.getStringExtra("UserS")
       dbref = FirebaseDatabase.getInstance().getReference("${getUserpath}")

       dbref.addValueEventListener(object :ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {

               if (snapshot.exists()){

                   for (senhaSnapshot in snapshot.children){

                       val senha = senhaSnapshot.getValue(Senha::class.java)
                       senhaArrayList.add(senha!!)

                   }

                   recycleView.adapter = Adapter(senhaArrayList)
               }
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }
       })
   }
}