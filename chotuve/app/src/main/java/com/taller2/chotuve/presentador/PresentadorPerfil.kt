package com.taller2.chotuve.presentador

import android.os.Handler
import com.taller2.chotuve.modelo.Usuario
import com.taller2.chotuve.vista.perfil.VistaInformacion


class PresentadorPerfil (private val vista: VistaInformacion) {
    fun onDestroy() {

    }

    fun obtenerInformacion() {
        val handler = Handler()
        handler.postDelayed(
            Runnable {  vista.mostrarPerfil(Usuario(1, "Lionel", "Messi", "franco.liberali@gmail.com", "1144162505", "Calle falsa 123", "https://www.mundodeportivo.com/r/GODO/MD/p5/MasQueDeporte/Imagenes/2018/10/24/Recortada/img_femartinez_20181010-125104_imagenes_md_otras_fuentes_captura-kcOG-U452531892714hYG-980x554@MundoDeportivo-Web.JPG")) },
            1000
        )
    }
}