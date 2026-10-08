package cl.duoc.aquacheck.util

// ARCHIVO: Guarda y copia las fotos de evidencia en el almacenamiento privado de la app.

import android.content.Context
import android.net.Uri
import java.io.File

/** Manejo de las fotos de evidencia: se guardan en filesDir/evidencias (privado de la app). */
object Almacenamiento {

    fun nuevoArchivo(context: Context): File {
        val carpeta = File(context.filesDir, "evidencias").apply { mkdirs() }
        return File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    }

    fun comoTexto(archivo: File): String = Uri.fromFile(archivo).toString()

    /** Copia una imagen elegida de la galería a nuestro almacenamiento (el permiso de la galería es temporal). */
    fun copiarDesdeGaleria(context: Context, origen: Uri): String? {
        val destino = nuevoArchivo(context)
        val entrada = context.contentResolver.openInputStream(origen) ?: return null
        entrada.use { input -> destino.outputStream().use { input.copyTo(it) } }
        return comoTexto(destino)
    }
}
