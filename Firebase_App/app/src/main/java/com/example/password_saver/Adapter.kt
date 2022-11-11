package com.example.password_saver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.password_saver.Dados.Senha

class Adapter(private  val senhaList: ArrayList<Senha>): RecyclerView.Adapter<Adapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.model_senhas,
            parent, false)
        return  MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val senha:Senha = senhaList[position]

        holder.Nome.text = senha.nome
        holder.senha.text = senha.senha
        holder.reclama.text = senha.reclama
        holder.id.text = senha.id
    }

    override fun getItemCount(): Int {
        return senhaList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val Nome: TextView = itemView.findViewById(R.id.txt_name)
        val senha: TextView = itemView.findViewById(R.id.txt_senha)
        val reclama: TextView = itemView.findViewById(R.id.txt_reclama)
        val id: TextView = itemView.findViewById(R.id.txt_id)

    }
}