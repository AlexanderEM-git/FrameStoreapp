package com.alexapps.framestoreapp.ui.userdata

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexapps.framestoreapp.R

class UserDataFragment : Fragment() {

    companion object {
        fun newInstance() = UserDataFragment()
    }

    private lateinit var viewModel: UserDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_data, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

}