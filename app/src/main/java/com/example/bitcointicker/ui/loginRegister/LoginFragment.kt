package com.example.bitcointicker.ui.loginRegister

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bitcointicker.R
import com.example.bitcointicker.databinding.FragmentLoginBinding
import com.example.bitcointicker.ui.MainActivity
import com.example.bitcointicker.ui.base.BaseFragment
import com.example.bitcointicker.utils.goBack
import com.example.bitcointicker.utils.handleResponseData
import com.example.bitcointicker.utils.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

   private val viewModel: LoginRegisterViewModel by activityViewModels()


    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onFragmentCreate() {
        binding.apply {
            registerButton.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            loginButton.setOnClickListener {
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
                        }, viewModel.firebaseLogin(binding.emailEditText.text.toString(),binding.passwordEditText.text.toString())
                    )
                }else{
                    requireContext().showToastMessage("please enter information")
                }
            }

        }
    }

}