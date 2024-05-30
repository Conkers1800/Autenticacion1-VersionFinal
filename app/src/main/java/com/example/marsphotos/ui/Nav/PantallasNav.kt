package com.example.marsphotos.ui.Nav


sealed class PantallasNav (val route: String){
    object Login: PantallasNav("Login")
    object Session: PantallasNav("Session")
    object Carga: PantallasNav("Carga")
    object Calificacion: PantallasNav("Calificacion")

}