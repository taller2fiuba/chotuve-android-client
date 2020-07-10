package com.taller2.chotuve.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.taller2.chotuve.modelo.Usuario


fun cargarImagen(usuario: Usuario, imageview: ImageView, view: View) {
    cargarImagen(usuario.fotoPerfilUri, imageview, Glide.with(view))
}

fun cargarImagen(usuario: Usuario, imageview: ImageView, fragment: Fragment) {
    cargarImagen(usuario.fotoPerfilUri, imageview, Glide.with(fragment))
}

fun cargarImagen(uri: Uri?, imageview: ImageView, requestManager: RequestManager) {
    if (uri != null) {
        requestManager
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(CenterCrop(), RoundedCorners(25))
            .into(imageview)
    }
}