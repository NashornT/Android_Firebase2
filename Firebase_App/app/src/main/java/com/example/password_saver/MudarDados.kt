package com.example.password_saver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MudarDados : AppCompatActivity() {


    private lateinit var  db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mudar_dados)

        val btn_udpate = findViewById<Button>(R.id.btn_update_pop)
        val btn_delete = findViewById<Button>(R.id.btn_delete_pop)


        btn_udpate.setOnClickListener {
            updateData()
        }

        btn_delete.setOnClickListener {

            val nome = findViewById<EditText>(R.id.txt_name_rounded).text.toString().trim()
            if (nome.isNotEmpty()){
                deleteData()
            }else{
                Toast.makeText(this,"Campo NOME vazio",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun deleteData() {

        val getUserpath = intent.getStringExtra("UserS")
        val nome = findViewById<EditText>(R.id.txt_name_rounded).text.toString().trim()
        db = FirebaseDatabase.getInstance().getReference("${getUserpath}")
        db.child(nome).removeValue().addOnSuccessListener{
            findViewById<EditText>(R.id.txt_name_rounded).text.clear()
            Toast.makeText(this,"Reclamação removido",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Erro ao remover valor",Toast.LENGTH_SHORT).show()
        }


    }

    private fun updateData() {

        val nome = findViewById<EditText>(R.id.txt_name_rounded).text.toString().trim()
        val reclama = findViewById<EditText>(R.id.txt_senha_rounded).text.toString().trim()

        val getUserpath = intent.getStringExtra("UserS")
        db = FirebaseDatabase.getInstance().getReference("${getUserpath}")

        val user = mapOf<String,String>(
            "nome" to nome,
            "reclama" to  reclama
        )

        db.child(nome).updateChildren(user).addOnSuccessListener {

            findViewById<EditText>(R.id.txt_name_rounded).text.clear()
            findViewById<EditText>(R.id.txt_senha_rounded).text.clear()
            Toast.makeText(this,"Reclamação alterada com sucesso",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Erro ao alterar valores",Toast.LENGTH_SHORT).show()
        }
    }
}