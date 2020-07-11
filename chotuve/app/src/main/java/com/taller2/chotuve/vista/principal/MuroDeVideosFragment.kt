package com.taller2.chotuve.vista.principal

import com.taller2.chotuve.presentador.PresentadorPrincipal

class MuroDeVideosFragment : VideosFragment() {
    override val presentador = PresentadorPrincipal(this)
}
