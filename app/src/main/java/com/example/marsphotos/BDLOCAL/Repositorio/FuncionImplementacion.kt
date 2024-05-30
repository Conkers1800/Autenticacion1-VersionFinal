package com.example.marsphotos.BDLOCAL.Repositorio

import com.example.marsphotos.BDLOCAL.BD.DAO
import com.example.marsphotos.BDLOCAL.BD.EntityDetalles

class FuncionImplementacion (private val DAO: DAO): FuncionSuspend{
    override suspend fun InsertarDetalles(item: EntityDetalles) = DAO.InsertarDetalles(item)

    override suspend fun deleteItem(item: EntityDetalles) = DAO.delete(item)

    override suspend fun ActualizarDetalles(item: EntityDetalles) = DAO.ActualizarDetalles(item)

    override suspend fun ObtenerDetalles(ID:Int): EntityDetalles = DAO.ObtenerDetalles(ID)
}