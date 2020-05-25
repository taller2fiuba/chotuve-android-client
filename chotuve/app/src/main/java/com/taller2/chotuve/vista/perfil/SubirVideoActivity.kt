package com.taller2.chotuve.vista.perfil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.componentes.VideoPortada
import com.taller2.chotuve.modelo.interactor.InteractorSubirVideo
import com.taller2.chotuve.presentador.PresentadorSubirVideo
import com.taller2.chotuve.util.obtenerDuracionVideo
import kotlinx.android.synthetic.main.subir_video.*


class SubirVideoActivity : AppCompatActivity(), VistaSubirVideo {
    private val presentador = PresentadorSubirVideo(this, InteractorSubirVideo())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccionar video"), 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        presentador.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            setContentView(R.layout.subir_video)

            val uri = data!!.data!!
            val portadaVideo = findViewById<View>(R.id.portada_video) as VideoPortada
            val duracion = obtenerDuracionVideo(uri) / 1000
            portadaVideo.setUri(uri, duracion)

            presentador.elegirVideo(uri, duracion)
        }
    }

    fun clickCrearVideo(view: View) {
        val tituloVideo = titulo.editText?.text?.toString()
        when {
            tituloVideo.isNullOrEmpty() -> titulo.error = "No puede estar vacío"
            else -> presentador.crearVideo(tituloVideo)
        }
    }

    override fun habilitarBotonSubidaAppServer() {
        boton_crear.isEnabled = true
    }

    override fun deshabilitarBotonSubidaAppServer() {
        boton_crear.isEnabled = false
    }

    override fun mostrarProgresoSubidaAppServer() {
        creando_video_barra_progreso.visibility = View.VISIBLE
    }

    override fun ocultarProgresoSubidaAppServer() {
        creando_video_barra_progreso.visibility = View.GONE
    }

    override fun setProgresoSubidaFirebase(porcentaje: Int) {
        subir_video_progress_bar.progress = porcentaje
    }

    override fun setTituloVacio() {
        titulo.error = "No puede estar vacío"
    }

    override fun setErrorRed() {
        Log.d("vista", "Error del server")
        Toast.makeText(this, "Error del server", Toast.LENGTH_LONG).show()
    }

    override fun onSubidaAppServerExitosa() {
        Log.d("vista", "Subido con éxito: terminando actividad")
        setResult(RESULT_OK)
        finish()
    }
}
