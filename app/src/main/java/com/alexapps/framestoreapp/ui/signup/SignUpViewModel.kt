package com.alexapps.framestoreapp.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.alexapps.framestoreapp.data.ResourceRemote
import com.alexapps.framestoreapp.data.UserRepository
import com.alexapps.framestoreapp.model.User


class SignUpViewModel : ViewModel() {
    private lateinit var user: User

    // Repositorio de firebase
    private val userRepository = UserRepository()

    private val _showMsg: MutableLiveData<String?> = MutableLiveData()
    val showMsg: LiveData<String?> = _showMsg

    private val _registerSuccess: MutableLiveData<String?> = MutableLiveData()
    val registerSuccess: LiveData<String?> = _registerSuccess

    fun validateFields(
        email: String,
        password: String,
        repPassword: String,
        name: String
    ) {
        if(email.isEmpty() || password.isEmpty() || repPassword.isEmpty() || name.isEmpty()){
            _showMsg.value = "Debe digitar todos los campos"
        }
        else{
            if (password != repPassword) {
                _showMsg.value = "Las contrasenas deben ser iguales"
            }
            else{
                if(password.length < 6) {
                    _showMsg.value = "La contrasena debe tener minimo 6 caracteres"
                }
                else {
                    // tarea asincrona por corrutinas IO input ouput
                    //GlobalScope.launch(Dispatchers.IO){ // no se usa por problemas con seguridad
                    viewModelScope.launch {
                        val result =
                            userRepository.registerUser(email, password) //intancia de ResouseRemote
                        result.let { resourceRemote ->
                            when (resourceRemote) {
                                is ResourceRemote.Success -> {

                                    user = User(result.data, name, email, "Sin definir","Sin definir") // obtengo los datos y creo una clase de usuario
                                    createUser(user) //Creo el usuario

                                }
                                is ResourceRemote.Error -> {
                                    var msg = result.message
                                    result.message?.let { Log.e("registro", it) }// mostramos el mensaje de erro para traducir
                                    when (result.message) { // para traducir
                                        "The email address is already in use by another account." -> msg = "ya existe una cuenta con ese correo electronico"
                                        "The email address is badly formatted." -> msg = "El email esta mal escrito"
                                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su coneccion de internet"
                                    }
                                    _showMsg.postValue(msg)
                                }
                                else -> {
                                    //don't use
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //crear usuario para los datos en firebase
    private fun createUser(user:User) {
        viewModelScope.launch{
            val result = userRepository.createUser(user)
            result.let { resourceRemote ->
                sequenceOf(
                    when (resourceRemote){
                        is ResourceRemote.Success ->{
                            _registerSuccess.postValue(result.data)
                            _showMsg.postValue("Registro Exitoso")
                        }
                        is ResourceRemote.Error ->{
                            val msg = result.message
                            _showMsg.postValue(msg)
                        }
                        else -> {
                            // dont't use
                        }
                    }
                )
            }
        }
    }
}