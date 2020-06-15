package com.taller2.chotuve.vista.perfil

interface VistaSubirVideo {
    fun setProgresoSubidaFirebase(porcentaje: Int)

    fun mostrarProgresoSubidaAppServer()
    fun ocultarProgresoSubidaAppServer()

    fun habilitarBotonSubidaAppServer()
    fun deshabilitarBotonSubidaAppServer()

    fun setError()
    fun setErrorRed()
    fun onSubidaAppServerExitosa()
}