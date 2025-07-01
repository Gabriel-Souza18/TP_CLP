package com.example.tp_clp

import ConferirResposta
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlin.collections.plus
import kotlin.toString
import kotlinx.coroutines.*
class MainActivity : AppCompatActivity() {


    lateinit var btn_criar: Button
    lateinit var conferidor: ConferirResposta
    lateinit var conteiner : LinearLayout
    var perguntas = emptyArray<String>()
    var respostas = emptyArray<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btn_criar = findViewById(R.id.btn_criar)
        conteiner = findViewById<LinearLayout>(R.id.container_perguntas)

        perguntas += "Qual o melhor professor da CComp?"
        respostas += "Elder"
        adicionarPergunta(perguntas[0], respostas[0])
        conferidor = ConferirResposta()

        //Corrotina
        GlobalScope.launch(Dispatchers.IO) {
            while(true) {
                delay(30000)
                Log.d("MainActivity", "Atualizando banco de dados")
                withContext(Dispatchers.Main) {
                    TesteJava().atualizarBancoDeDados(this@MainActivity)
                }
            }
        }
    }
    fun adicionarPergunta(pergunta: String, resposta: String) {
        Log.d("MainActivity", "Adicionando PErgunta")

        val btn = Button(this)
        btn.text = pergunta
        btn.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        btn.setOnClickListener{
            val input = EditText(this)
            input.hint = "Digite sua resposta"
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(pergunta)
                .setView(input)
                .setPositiveButton("Responder") { _, _ ->
                    val respostaUsuario = input.text.toString()

                    if (conferidor.conferirResposta(respostaUsuario, resposta)){
                        conferidor.respostaCorreta(this)
                    } else {
                        conferidor.respostaIncorreta(this)
                    }

                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        conteiner.addView(btn)
    }
    fun criarPergunta(view: View) {
        Log.d("MainActivity", "Criando pergunta")
        val dialogView = layoutInflater.inflate(R.layout.criar_pergunta, null)
        val etPergunta = dialogView.findViewById<EditText>(R.id.et_pergunta)
        val etResposta = dialogView.findViewById<EditText>(R.id.et_resposta)


        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Criar Pergunta")
            .setView(dialogView)
            .setPositiveButton("Criar") { dialog, _ ->
                val pergunta = etPergunta.text.toString()
                val resposta = etResposta.text.toString()

                if (pergunta.isNotEmpty() && resposta.isNotEmpty()) {
                    perguntas += pergunta
                    respostas += resposta
                    adicionarPergunta(pergunta, resposta)
                    Toast.makeText(this, "Pergunta criada com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }
}


