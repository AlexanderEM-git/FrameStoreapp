package com.alexapps.framestoreapp.model

// Clase generica para los datos del Usuario
data class User(
    var uid:String? = null,
    var name: String? = null,
    var email: String? = null,
    var gender: String? = null,
    var address: String? = null
)
