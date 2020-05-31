package com.taller2.chotuve.vista.perfil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.taller2.chotuve.Chotuve.Companion.context
import com.taller2.chotuve.R
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
            val visibilidades = listOf("Publico", "Privado")
            val adapter = ArrayAdapter(context, R.layout.opcion_list, visibilidades)
            (visibilidad.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            val uri = data!!.data!!
            Glide.with(this).load(uri).into(portada_video)
            duracion.text = DateUtils.formatElapsedTime(obtenerDuracionVideo(uri) / 1000)
            presentador.elegirVideo(uri)
        }
    }

    fun clickCrearVideo(view: View) {
        val tituloString = titulo.editText?.text?.toString()
        val ubicacionString = ubicacion.editText?.text?.toString()
        val descripcionString = descripcion.editText?.text?.toString()
        val visibilidadString = visibilidad.editText?.text?.toString()
        when {
            tituloString.isNullOrEmpty() -> titulo.error = "No puede estar vacío"
            ubicacionString.isNullOrEmpty() -> ubicacion.error = "No puede estar vacío"
            visibilidadString.isNullOrEmpty() -> visibilidad.error = "No puede estar vacío"
            else -> presentador.crearVideo(tituloString)
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
