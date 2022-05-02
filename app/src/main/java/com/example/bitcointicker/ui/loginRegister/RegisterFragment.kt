package com.example.bitcointicker.ui.loginRegister

import androidx.fragment.app.activityViewModels
import com.example.bitcointicker.databinding.FragmentRegisterBinding
import com.example.bitcointicker.ui.MainActivity
import com.example.bitcointicker.ui.base.BaseFragment
import com.example.bitcointicker.utils.goBack
import com.example.bitcointicker.utils.handleResponseData
import com.example.bitcointicker.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: LoginRegisterViewModel by activityViewModels()


    override fun getViewBinding(): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(layoutInflater)
    }

    override fun onFragmentCreate() {
        binding.registerButton.setOnClickListener {
           if(!binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()){
               handleResponseData(
                   loadingState = {
                       (requireActivity() as MainActivity).showLoading()
                   },
                   successState = {firebaseUser->
                       (requireActivity() as MainActivity).stopLoading()
                       if(firebaseUser?.uid != null){
                           viewModel.saveIsAuthUser(true)
                           requireContext().showToastMessage("Register Success")
                           goBack()
                       }else{
                           requireContext().showToastMessage("Firebase auth error")
                       }
                   },
                   errorState = {
                       (requireActivity() as MainActivity).stopLoading()
                       requireContext().showToastMessage("Firebase auth error")
                   }, viewModel.firebaseRegister(binding.emailEditText.text.toString(),binding.passwordEditText.text.toString())
               )
           }else{
               requireContext().showToastMessage("please enter information")
           }
        }
    }

}