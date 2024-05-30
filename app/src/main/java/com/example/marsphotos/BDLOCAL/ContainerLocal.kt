package com.example.marsphotos.BDLOCAL

import android.content.Context
import com.example.marsphotos.BDLOCAL.BD.BD
import com.example.marsphotos.BDLOCAL.Repositorio.FuncionImplementacion
import com.example.marsphotos.BDLOCAL.Repositorio.FuncionSuspend


interface AppContainer {
    val REPOLOCAL: FuncionSuspend
}
class ContainerLocal(private val context: Context) : AppContainer {

    override val REPOLOCAL: FuncionSuspend  by lazy {
        FuncionImplementacion(BD.getDatabase(context).DAO())
    }

}