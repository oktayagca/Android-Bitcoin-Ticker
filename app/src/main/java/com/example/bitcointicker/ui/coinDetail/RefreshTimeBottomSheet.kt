package com.example.bitcointicker.ui.coinDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bitcointicker.databinding.BottomSheetRefreshTimeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RefreshTimeBottomSheet : BottomSheetDialogFragment() {
    private var binding:BottomSheetRefreshTimeBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetRefreshTimeBinding.inflate(layoutInflater)
        return binding!!.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        super.onStart()
        binding?.apply {
            saveButton.setOnClickListener{
                if(refreshTimeEditText.text.toString() == "" || refreshTimeEditText.text.toString().toInt()<5){
                    Toast.makeText(requireContext(),"Minimum refresh time must be 5 seconds",Toast.LENGTH_SHORT).show()
                }else{
                    navigateBackWithRefreshTime(refreshTimeEditText.text.toString())
                }
            }
        }
    }

    private fun navigateBackWithRefreshTime(time: String) = with(findNavController()) {
        previousBackStackEntry?.savedStateHandle?.set("time", (time.toInt()*1000).toString())
        popBackStack()
    }
}