import android.content.Context
import android.widget.Toast

class ConferirResposta{

    fun conferirResposta(resposta: String, respostaCorreta: String): Boolean {
        return resposta.equals(respostaCorreta, ignoreCase = true)
    }
    fun respostaCorreta(contexto: Context){
        Toast.makeText(contexto,
            "Resposta Correta",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    fun respostaIncorreta(contexto: Context) {
        Toast.makeText(contexto,
            "Resposta Errada",
            Toast.LENGTH_LONG
        ).show()
        return
    }

}
