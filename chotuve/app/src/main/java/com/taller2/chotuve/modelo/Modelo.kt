package com.taller2.chotuve.modelo

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.taller2.chotuve.modelo.data.InfoInicioSesion
import org.json.JSONObject;
import com.taller2.chotuve.modelo.data.InfoRegistro
import com.taller2.chotuve.modelo.data.Video
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class Modelo private constructor () {
    // Singleton
    companion object {
        val instance = Modelo()
    }

    private val appServerService = AppServerService.create()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private var userToken: String? = null;

    fun estaLogueado() : Boolean = userToken != null

    fun registrarUsuario(email: String, clave: String, callbackRegistro: CallbackRegistro) {
        Log.d("modelo", "Registrando usuario " + email)
        appServerService.registrarUsuario(
            InfoRegistro(
                email,
                clave
            )
        ).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("modelo", "Respuesta obtenida: " + response.code())
                    if (response.code() == 201) {
                        val json = JSONObject(response.body()!!.string())
                        if (!json.has("auth_token")) {
                            Log.d("modelo", "No hay auth_token")
                            callbackRegistro.onErrorRed("Error desconocido: no hay auth_token")
                        } else {
                            Log.d("modelo", "Token obtenido")
                            userToken = json.getString("auth_token")
                            callbackRegistro.onExito()
                        }
                    } else if (response.code() == 400) {
                        Log.d("modelo", "Usuario ya registrado")
                        callbackRegistro.onYaEstaRegistrado()
                    } else {
                        Log.d("modelo", "Error desconocido " + response.code())
                        callbackRegistro.onErrorRed("Error desconocido: " + response.code())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error: " + t.message)
                    callbackRegistro.onErrorRed(t.message)
                }
            }
        )
    }

    fun iniciarSesion(email: String, clave: String, callbackInicioSesion: CallbackInicioSesion) {
        Log.d("modelo", "Iniciando sesión de usuario " + email)
        appServerService.iniciarSesion(
            InfoInicioSesion(
                email,
                clave
            )
        ).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("modelo", "Respuesta obtenida: " + response.code())
                    if (response.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        if (!json.has("auth_token")) {
                            Log.d("modelo", "No hay auth_token")
                            callbackInicioSesion.onErrorRed("Error desconocido: no hay auth_token")
                        } else {
                            Log.d("modelo", "Token obtenido")
                            userToken = json.getString("auth_token")
                            callbackInicioSesion.onExito()
                        }
                    } else if (response.code() == 400) {
                        Log.d("modelo", "Usuario ya registrado")
                        callbackInicioSesion.onUsuarioOClaveIncorrecta()
                    } else {
                        Log.d("modelo", "Error desconocido " + response.code())
                        callbackInicioSesion.onErrorRed("Error desconocido: " + response.code())
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("modelo", "Error: " + t.message)
                    callbackInicioSesion.onErrorRed(t.message)
                }
            }
        )
    }

    fun subirVideo(context: Context, titulo: String, uri: Uri, callbackSubirVideo: CallbackSubirVideo) {
        Log.d("modelo", "Subiendo video $titulo con nombre " + getFilenameFromUri(context, uri))
        val firebaseFileReference = firebaseStorage.child(getFilenameFromUri(context, uri))
        firebaseFileReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("modelo", "Subido a firebase")
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri: Uri? ->
                    Log.d("modelo", "Subiendo al app server")
                    appServerService.crearVideo("Bearer $userToken", Video(titulo, downloadUri.toString()))
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                val responseCode = response.code()
                                if (responseCode == 200) {
                                    Log.d("modelo", "Subido al app server")
                                    callbackSubirVideo.onExito(downloadUri.toString())
                                } else {
                                    Log.d("modelo", "Error: " + response.body()!!.string())
                                    callbackSubirVideo.onErrorRed(response.body()!!.string())
                                }
                            }
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.d("modelo", "Error al contactar al app server: " + t.message)
                                callbackSubirVideo.onErrorRed(t.message)
                            }
                        })
                }
            }
            .addOnFailureListener { e ->
                Log.d("modelo", "Error al contactar a Firebase: " + e.message)
                callbackSubirVideo.onErrorRed(e.message)
            }
    }

    // TODO: Esto debería estar en algun paquete `util` o algo así.
    fun getFilenameFromUri(context: Context, uri: Uri) : String {
        // https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
        var result = null as String?
        if (uri.scheme.equals("content")) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }

        if (result == null) {
            result = uri.getPath();
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }
}