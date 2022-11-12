package com.example.password_saver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.password_saver.Dados.Senha
import com.google.firebase.database.*

class empresas : AppCompatActivity() {

    private lateinit var  dbref: DatabaseReference
    private lateinit var  recycleView: RecyclerView
    private lateinit var senhaArrayList: ArrayList<Senha>
    private lateinit var  Adapter: Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresas)

        recycleView = findViewById(R.id.recycly_empresas)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)

        senhaArrayList = arrayListOf()

        Adapter = Adapter(senhaArrayList)

        recycleView.adapter = Adapter

        getUserData()


    }


    private fun getUserData(){
        dbref = FirebaseDatabase.getInstance().getReference("Empresas_com_reclamacoes")

        dbref.addValueEventListener(object : ValueEventListener {
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
