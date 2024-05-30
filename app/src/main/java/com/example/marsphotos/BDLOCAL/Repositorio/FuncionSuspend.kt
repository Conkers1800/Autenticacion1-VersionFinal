package com.example.marsphotos.BDLOCAL.Repositorio

import com.example.marsphotos.BDLOCAL.BD.EntityDetalles

interface FuncionSuspend{
    suspend fun InsertarDetalles(item: EntityDetalles)
    suspend fun deleteItem(item: EntityDetalles)
    suspend fun ActualizarDetalles(item: EntityDetalles)

    suspend fun ObtenerDetalles(ID:Int):EntityDetalles
}