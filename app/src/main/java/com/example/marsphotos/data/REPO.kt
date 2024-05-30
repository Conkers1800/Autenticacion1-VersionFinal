package com.example.marsphotos.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.marsphotos.model.ALUMNO
import com.example.marsphotos.model.Calificaciones
import com.example.marsphotos.model.CargaAcademicaItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface REPOSICE{
    suspend fun Login(matricula: String, password: String): ALUMNO
    suspend fun CargaAcademicaByAlumno(): String// List<CargaAcademicaItem>
    suspend fun Calificaciones(): List<Calificaciones>
}
class Iniciar(
    val Login : InterLogin, val CARGA : CargaAcademicaByAlumno, val Cali : Calificacionesin,
      context: Context,
    var XMLA : String = XMLAcceso.trimIndent(), var XMLC : String = XMLCARGA.trimIndent(),
    val XMLCAL :String = XMLCALIFI.trimIndent()
) : REPOSICE
{
    override suspend fun Login(matricula: String, password: String): ALUMNO {
        try {
            val PETICION = XMLA.format(matricula, password).toRequestBody()
            Login.Cookies()
            var respuesta = Login.Login(PETICION).string().split("{", "}")
            Log.d("RESPUESTA", respuesta.toString())
            if (respuesta != null) {
                val result = Gson().fromJson("{" + respuesta[1] + "}", ALUMNO::class.java)
              Log.d("RESPUESTA", result.toString())
                return result
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Error en el proceso de login: ${e.message}", e)
        }
        return ALUMNO(0,"", "", "", "")
    }
    override suspend fun CargaAcademicaByAlumno(): String { // List<CargaAcademicaItem> {
        try {
            val PETICION = XMLC.toRequestBody()
            var lista = mutableListOf<CargaAcademicaItem>()
            var respuesta = CARGA.CARGA(PETICION).string().split("{", "}")
            if (respuesta != null) {
            return respuesta.toString()
            }
        } catch (e: Exception) {
            // Manejo de la excepción. Puedes imprimir un mensaje de registro o realizar otras acciones.
            Log.e("ERROR", "Error durante el proceso de carga académica: ${e.message}", e)
        }
        return "listOf"
    }
    override suspend fun Calificaciones(): List<Calificaciones> {
        try {
            val PETICION = XMLCAL.toRequestBody()
            var lista = mutableListOf<Calificaciones>()
            var respuesta = Cali.Cali(PETICION).string().split("{", "}")
            Log.d("RESPUESTA", respuesta.toString())

            if (respuesta != null) {
                var Tamres = respuesta.toString().length
                var count =  1
                while (count < Tamres-1)
                {
                    val objetoA = desCali(respuesta.toString().substring(count,Tamres-1))
                    count = count + objetoA.INDEX
                    if(objetoA.CALI.Materia!="")
                    {
                        lista.add(objetoA.CALI)
                    }
                }
                Log.d("LISTA", lista.toString())

                return lista
            }
        } catch (e: Exception) {
            // Manejo de la excepción. Puedes imprimir un mensaje de registro o realizar otras acciones.
            Log.e("ERROR", "Error en el proceso de carga académica: ${e.message}", e)
        }
        return listOf()
    }

}
//------------------------------------------METODOS------------------------------------------
data class OBJ(val CARGA: CargaAcademicaItem, val INDEX: Int)
data class OBJCALI(val CALI: Calificaciones, val INDEX: Int)
fun des(Respuesta: String):OBJ {
    try {
        val OBJ = Respuesta.substring(Respuesta.indexOf("Semipresencial"), Respuesta.indexOf(", ,"))
        val result = Gson().fromJson("{\""+OBJ+"}", CargaAcademicaItem::class.java)
        return OBJ(result ,Respuesta.indexOf(", ,")+2)
    } catch (e: Exception) {
        Log.e("RESPUESTA DES", "Error al convertir JSON: ${e.message}")
        return OBJ(CargaAcademicaItem(),5000)
    }
}
fun desCali(Respuesta: String):OBJCALI {
    try {
        val OBJ = Respuesta.substring(Respuesta.indexOf("Observaciones"), Respuesta.indexOf(", ,"))
        val result = Gson().fromJson("{\""+OBJ+"}", Calificaciones::class.java)
        return OBJCALI(result ,Respuesta.indexOf(", ,")+2)
    } catch (e: Exception) {
        Log.e("RESPUESTA DES", "Error al convertir JSON: ${e.message}")
        return OBJCALI(Calificaciones(),5000)
    }

}
//XML Soap
val XMLAcceso = """
    <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
      <soap:Body>
        <accesoLogin xmlns="http://tempuri.org/">
          <strMatricula>%s</strMatricula>
          <strContrasenia>%s</strContrasenia>
          <tipoUsuario>ALUMNO</tipoUsuario>
        </accesoLogin>
      </soap:Body>
    </soap:Envelope>
"""
val XMLCARGA =""" 
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getCargaAcademicaByAlumno xmlns="http://tempuri.org/" />
  </soap:Body>
</soap:Envelope>"""
val XMLCALIFI = """<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getCalifUnidadesByAlumno xmlns="http://tempuri.org/" />
  </soap:Body>
</soap:Envelope>"""
//INTERFAZ
interface InterLogin {
    @Headers(
        "Content-Type: text/xml",
        "SOAPAction: \"http://tempuri.org/accesoLogin\"",
        )
    @POST("ws/wsalumnos.asmx")
  suspend fun Login(@Body requestBody: RequestBody): ResponseBody

    @GET("ws/wsalumnos.asmx")
    suspend fun Cookies(): ResponseBody
}
interface CargaAcademicaByAlumno
{
    @Headers(  "Content-Type: text/xml",
        "SOAPAction: \"http://tempuri.org/getCargaAcademicaByAlumno\"")

    @POST("ws/wsalumnos.asmx")
    suspend fun CARGA(@Body requestBody: RequestBody): ResponseBody
}
interface Calificacionesin
{
    @Headers(  "Content-Type: text/xml",
        "SOAPAction: \"http://tempuri.org/getCalifUnidadesByAlumno\"")

    @POST("ws/wsalumnos.asmx")
    suspend fun Cali(@Body requestBody: RequestBody): ResponseBody
}