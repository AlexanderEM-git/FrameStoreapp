package com.alexapps.framestoreapp.data

import android.util.Log
import com.alexapps.framestoreapp.model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {

    // instancia para base de datos
    private  var db = Firebase.firestore

    //me conecto con firebase a la seccion autentificacion
    private var  auth: FirebaseAuth = Firebase.auth


    // Funcion de suspencion para corrutina asincrona
    suspend fun registerUser(email: String, password: String): ResourceRemote<String?> {
        //necesitamos conectarlo con el viewmodel
        //encapsulamos respuestas
        return try { // intentamos retornos de firebase
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            ResourceRemote.Success(data = result.user?.uid) //retornamos el id del usuario si es exitoso
        } catch (e: FirebaseAuthException) { //si falla muestra un error
            Log.e("Register", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

    // Funcion de suspencion para corrutina asincrona , crear usuario

    suspend fun createUser(user: User): ResourceRemote<String?> {
        return try { // intentamos retornos de firebase, ingreso de usuario con el id del registro
            val result = user.uid?.let { db.collection("users").document(it).set(user).await()}
            ResourceRemote.Success(data = user.uid)// devuelve el id del usuario al view model
        }
        catch (e: FirebaseAuthException){ //si falla muestra un error
            Log.e("Register", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            Log.e("RegisterNetwork", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

    // Funcion de suspencion para corrutina asincrona , login

    suspend fun loginUser(email: String, password: String): ResourceRemote<String?> {
        return try { // intentamos retornos de firebase, ingreso de usuario con el id del registro
            val result = auth.signInWithEmailAndPassword(email, password).await()
            ResourceRemote.Success(data = result.user?.uid) // devuelve el id del usuario al view model
        }
        catch (e: FirebaseAuthException){ //si falla muestra un error
            Log.e("Register", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            Log.e("RegisterNetwork", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

}