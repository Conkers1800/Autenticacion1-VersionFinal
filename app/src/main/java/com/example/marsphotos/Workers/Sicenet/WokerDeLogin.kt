package com.example.marsphotos.Workers.Sicenet

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.marsphotos.MarsPhotosApplication

private const val TAG = "WorkerDeLogin"

class WorkerLOGIN(ctx: Context, params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    val para = params.inputData
    val context = ctx
    var repoSice = (ctx.applicationContext as MarsPhotosApplication).container.REP
    override suspend fun doWork(): Result {
        return try {
                val username = para.getString("username") ?: "" // Obtener nombre de usuario de WorkerParameters
                val password = para.getString("password") ?: "" // Obtener contraseña de WorkerParameters

                val alumno = repoSice.Login(username, password) // Llamar a la función Login de REPOSICE

            Log.e("WORKER ALUMNO", alumno.toString())
                if (alumno != null && alumno.Id != 0) {
                    // Inicio de sesión correcto: realizar las tareas en segundo plano necesarias según la lógica de su aplicación
                    Log.d(TAG, "Inicio de sesión correcto para el usuario: ${alumno.matricula}")
                    var Al = workDataOf(
                        "matricula" to alumno.matricula,
                        "contrasenia" to alumno.contrasenia,
                        "acceso" to alumno.acceso,
                        "estatus" to alumno.estatus,)
                    Result.success(Al)
                } else {
                    // Error de inicio de sesión: manejar el error de forma adecuada
                    Log.e(TAG, "Error de inicio de sesión para el nombre de usuario: $username")
                    Result.failure()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error durante el inicio de sesión: ${e.message}", e)
                Result.failure()
            }
        }
}
