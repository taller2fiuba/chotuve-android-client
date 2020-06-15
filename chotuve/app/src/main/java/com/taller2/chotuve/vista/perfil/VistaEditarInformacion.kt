package com.taller2.chotuve.vista.perfil

interface VistaEditarInformacion {
    //fun setProgresoSubidaFirebase(porcentaje: Int)

    fun habilitarBotonEdicion()
    fun deshabilitarBotonEdicion()

    fun setError()
    fun setErrorRed()
    fun onEdicionExitosa()
}