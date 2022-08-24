package com.alexapps.framestoreapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexapps.framestoreapp.data.ResourceRemote
import com.alexapps.framestoreapp.data.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Repositorio de firebase para usuarios
    private val userRepository = UserRepository()

    private val _showMsg: MutableLiveData<String?> = MutableLiveData()
    val showMsg: LiveData<String?> = _showMsg

    private val _loginSuccess: MutableLiveData<String?> = MutableLiveData()
    val loginSuccess: LiveData<String?> = _loginSuccess


    fun validateFields(email: String, password: String) {
        if(email.isEmpty() || password.isEmpty()){
            _showMsg.value = "Debe digitar todos los campos"
        }
        else{
            if(password.length < 6) {
                _showMsg.value = "La contrasena debe tener minimo 6 caracteres"
            }else{
                // tarea asincrona por corrutinas IO input ouput
                //GlobalScope.launch(Dispatchers.IO){ // no se usa por problemas con seguridad
                viewModelScope.launch {
                    //userRepository.registerUser(email,password)
                    val result =
                        userRepository.loginUser(email, password) //intancia de ResouseRemote
                    result.let { resourceRemote ->
                        when (resourceRemote) {
                            // Si es exitoso
                            is ResourceRemote.Success -> {
                                _loginSuccess.postValue(result.data)
                                _showMsg.postValue("Bienvenido")
                            }
                            // si es erroneo
                            is ResourceRemote.Error -> {
                                var msg = result.message
                                result.message?.let { Log.e("login", it) }// mostramos el mensaje de erro para traducir
                                when (result.message) { // para traducir
                                    "The email address is badly formatted." -> msg = "El email esta mal escrito"
                                    "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su coneccion de internet"
                                    "There is no user record corresponding to this identifier. The user may have been deleted." -> msg = "No exite cuenta con ese correo electronico"
                                    "The password is invalid or the user does not have a password." -> msg = "Contrasena es invalida"
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