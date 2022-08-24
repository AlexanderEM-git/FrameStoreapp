package com.alexapps.framestoreapp.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alexapps.framestoreapp.R
import com.alexapps.framestoreapp.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController


class LoginFragment : Fragment() {

    // Bindign y View model
    private  lateinit var loginBinding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Asignacion de los Bindign y ViewModel
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        //Mensaje Error de datos al del ingreso
        loginViewModel.showMsg.observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                showErrorMessage(msg)
            }
        }

        // Si el inicio de es correcto de va al home
        loginViewModel.loginSuccess.observe(viewLifecycleOwner){
            goToHome()
        }

        with(loginBinding){

            //Direciones de navegacion en los botones
            loginButton.setOnClickListener {
                loginViewModel.validateFields(emailEditText.text.toString(),passwordEditText.text.toString())
            }
            sinupTextView.setOnClickListener{
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }

        }
        return loginBinding.root
    }

    //quitamos el app barr
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    // funcion para entrar a la app despues de login
    fun goToHome(){
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNavigationHome())
    }

    // funcion para mostrar un mensaje de error
    private fun showErrorMessage(msg: String) {
        //Snackbar.make(,msg,Snackbar.LENGTH_INDEFINITE).show()
        Toast.makeText(requireActivity(),msg, Toast.LENGTH_LONG).show()
    }

}