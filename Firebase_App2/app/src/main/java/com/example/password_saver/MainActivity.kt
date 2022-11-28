package com.example.password_saver

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.password_saver.Auth.Login
import com.example.password_saver.Dados.Senha
import com.example.password_saver.Encrypt.Encry_Decry
import com.example.password_saver.RestApi.Interface
import com.example.password_saver.RestApi.Model
import com.example.password_saver.RestApi.RestActivity
import com.example.password_saver.RestApi.Retro
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class MainActivity : AppCompatActivity() {
    //todo pode dar ruim aqui
    var encry_name:String = ""
    var encry:String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        val getUserpath = intent.getStringExtra("User")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.btn_salvar)
        val btn_result = findViewById<Button>(R.id.btn_senhas)
        val btn_sair = findViewById<TextView>(R.id.txt_sair)
        val btn_alterar = findViewById<Button>(R.id.txt_alterar)
        val btn_empresas = findViewById<Button>(R.id.btn_empresas)
        val btn_rest = findViewById<Button>(R.id.btn_rest)


        val nome = findViewById<EditText>(R.id.nome).text
        val nome_empresa = findViewById<EditText>(R.id.nome_empresa).text
        val senha = findViewById<EditText>(R.id.senha).text
        val reclama = findViewById<EditText>(R.id.reclama)

        btn.isEnabled = false

        reclama.doAfterTextChanged {
            if(nome.isNotEmpty() and nome_empresa.isNotEmpty() and senha.isNotEmpty() and reclama.text.isNotEmpty()){
                btn.isEnabled = true
            }
        }

        btn.setOnClickListener {
            saveAll()

        }
        btn_result.setOnClickListener {
            val intent = Intent(this,senhas::class.java)
            intent.putExtra("UserS","${getUserpath}")
            startActivity(intent)
        }
        btn_sair.setOnClickListener{
            val intent = Intent(this,Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()

        }
        btn_alterar.setOnClickListener{
            val intent = Intent(this,MudarDados::class.java)
            intent.putExtra("UserS","${getUserpath}")
            startActivity(intent)
        }
        btn_empresas.setOnClickListener {
            val intent = Intent(this,empresas::class.java)
            startActivity(intent)
        }
        btn_rest.setOnClickListener {
            val intent = Intent(this,RestActivity::class.java)
            startActivity(intent)
        }
    }


    private fun saveAll()  {

        val getUserpath = intent.getStringExtra("User")
        val nome = findViewById<EditText>(R.id.nome).text.toString().trim()
        val nome_empresa = findViewById<EditText>(R.id.nome_empresa).text.toString().trim()
        val senha = findViewById<EditText>(R.id.senha).text.toString().trim()
        val reclama = findViewById<EditText>(R.id.reclama).text.toString().trim()

        val ref = FirebaseDatabase.getInstance().getReference("${getUserpath}")

//        val db = FirebaseFirestore.getInstance()
//        db.collection("senhas")

        if(nome.isEmpty() or nome_empresa.isEmpty() or senha.isEmpty() or reclama.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos!!",Toast.LENGTH_SHORT).show()
        }else{

            encry = encrypt(this,senha).toString()
            encry_name = encrypt(this,nome_empresa).toString()


            val senhaid = ref.push().key
            val senhaDB = Senha(nome,encry_name,encry,reclama,senhaid.toString())

            ref.child(nome).setValue(senhaDB)

            val ref2 = FirebaseDatabase.getInstance().getReference("Empresas_com_reclamacoes")

            val nomeEncry = "****"
            val reclamaDB = Senha(nomeEncry,nome_empresa,senha,reclama,senhaid.toString())

            ref2.child(senhaid.toString()).setValue(reclamaDB)

            Toast.makeText(this,"Reclamação Enviada",Toast.LENGTH_SHORT).show()

        }




    }

    fun encrypt(context: Context, strToEncrypt: String): ByteArray {
        val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key = keygen.generateKey()
        saveSecretKey(context, key)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(plainText)
        saveInitializationVector(context, cipher.iv)

        val sb = StringBuilder()
        for (b in cipherText) {
            sb.append(b.toChar())
        }
//        Toast.makeText(context, "dbg encrypted = [" + sb.toString() + "]", Toast.LENGTH_LONG).show()

        return cipherText
    }

    fun saveSecretKey(context:Context, secretKey: SecretKey) {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(secretKey)
        val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString("secret_key", strToSave)
        editor.apply()
    }

    fun saveInitializationVector(context: Context, initializationVector: ByteArray) {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(initializationVector)
        val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString("initialization_vector", strToSave)
        editor.apply()
    }







}