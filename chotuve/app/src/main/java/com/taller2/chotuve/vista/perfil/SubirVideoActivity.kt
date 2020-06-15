package com.taller2.chotuve.vista.perfil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.taller2.chotuve.Chotuve.Companion.context
import com.taller2.chotuve.R
import com.taller2.chotuve.modelo.interactor.InteractorSubirVideo
import com.taller2.chotuve.presentador.PresentadorSubirVideo
import com.taller2.chotuve.util.obtenerDuracionVideo
import com.taller2.chotuve.vista.componentes.VideoPortada
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
            visibilidad.setOnClickListener {
                PopupMenu(context, visibilidad).apply {
                    menuInflater.inflate(R.menu.menu_visibilidades, menu)
                    setOnMenuItemClickListener { item ->
                        visibilidad.setText(item.title)
                        true
                    }
                    show()
                }
            }

            val uri = data!!.data!!
            val portadaVideo = findViewById<View>(R.id.portada_video) as VideoPortada
            val duracion = obtenerDuracionVideo(uri) / 1000
            portadaVideo.setUri(uri, duracion)

            presentador.elegirVideo(uri)
        }
    }

    fun clickCrearVideo(view: View) {
        val tituloString = titulo.editText?.text?.toString()
        val ubicacionString = ubicacion.editText?.text?.toString()
        val descripcionString = descripcion.editText?.text?.toString()
        val visibilidadString = visibilidad.text?.toString()
        when {
            tituloString.isNullOrEmpty() -> titulo.error = "No puede estar vacío"
            ubicacionString.isNullOrEmpty() -> ubicacion.error = "No puede estar vacío"
            visibilidadString.isNullOrEmpty() -> visibilidad_text_field.error = "No puede estar vacío"
            else -> presentador.crearVideo(tituloString, ubicacionString, descripcionString, visibilidadString)
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

    override fun setError() {
        Toast.makeText(this, "Error en el formulario", Toast.LENGTH_LONG).show()
    }

    override fun setErrorRed() {
        Toast.makeText(this, "Error de comunicación", Toast.LENGTH_LONG).show()
    }

    override fun onSubidaAppServerExitosa() {
        Log.d("vista", "Subido con éxito: terminando actividad")
        setResult(RESULT_OK)
        finish()
    }
}
