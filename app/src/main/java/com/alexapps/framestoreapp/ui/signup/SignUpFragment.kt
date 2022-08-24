package com.alexapps.framestoreapp.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.alexapps.framestoreapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var signUpBinding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        signUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        //Mensaje Error de datos
        signUpViewModel.showMsg.observe(viewLifecycleOwner){ msg ->
            if (msg != null) {
                showErrorMessage(msg)
            }
        }

        signUpViewModel.registerSuccess.observe(viewLifecycleOwner){
            goToLogin()
        }

        with(signUpBinding) {

            //direcciones de navegacion
            //signUpBinding.signupButton.setOnClickListener {
            signupButton.setOnClickListener {
                signUpViewModel.validateFields(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    repPasswordEditText.text.toString(),
                    nameEditText.text.toString()
                )
            }
        }

        return signUpBinding.root
    }

    //quitamos el app barr
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    //usuario correcto registrado, voy a login
    private fun goToLogin() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
    }

    // funcion para mostrar mensages
    private fun showErrorMessage(msg: String) {
        //Snackbar.make(,msg,Snackbar.LENGTH_INDEFINITE).show()
        Toast.makeText(requireActivity(),msg, Toast.LENGTH_LONG).show()
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        signUpBinding = null
    }*/

}