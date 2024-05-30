package com.example.marsphotos.Workers.BaseDatos

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.marsphotos.BDLOCAL.BD.EntityDetalles
import com.example.marsphotos.MarsPhotosApplication

private const val TAG = "WorkerDeLogin-BD"

class WorkerDeLogin_BaseDatos(ctx: Context, params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    val para = params.inputData
    var RepoLocal = (ctx.applicationContext as MarsPhotosApplication).container2.REPOLOCAL
    override suspend fun doWork(): Result {
        return try {
            var a = EntityDetalles(1,para.getString("M") ?: "",para.getString("C") ?: "",
                  para.getString("E") ?: "",para.getString("A") ?: "")
            Log.d("Worker_BaseDatos ALUMNO", a.toString())
             val alumno = RepoLocal.InsertarDetalles(a) // Llamar a la función Login de REPOSICE
            Log.d("Worker_BaseDatos ALUMNO", alumno.toString())
            if (alumno != null) {
                Result.success()
            } else {
                // Error de inicio de sesión: manejar el error de forma adecuada
                Log.e(TAG, "Error de conexión con la base de datos")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error de conexión con la base de datos:${e.message}", e)
            Result.failure()
        }
    }
}
