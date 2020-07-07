package com.taller2.chotuve.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.taller2.chotuve.modelo.Usuario


fun cargarImagen(usuario: Usuario, imageview: ImageView, view: View) {
    cargarImagen(usuario.fotoPerfilUri, imageview, view)
}

fun cargarImagen(uri: Uri?, imageview: ImageView, view: View) {
    if (uri != null) {
        Glide
            .with(view)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform( CenterCrop(), RoundedCorners(25))
            .into(imageview)
    }
}