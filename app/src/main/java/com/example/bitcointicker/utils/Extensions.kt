package com.example.bitcointicker.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bitcointicker.ui.MainActivity

fun View.gone() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.show() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun Fragment.navigateFragment(
    navDirection: NavDirections? = null,
    actionId: Int? = null,
    navOptions: NavOptions? = null,
) {
    if (navDirection == null && actionId != null) {
        findNavController().safeNavigate(actionId)
    } else {
        findNavController().safeNavigate(navDirection!!, navOptions)
    }

}

fun Fragment.goBack() {
    if (!findNavController().popBackStack()) {
        requireActivity().finish()
    } else {
        findNavController().navigateUp()
    }
}

fun NavController.safeNavigate(direction: NavDirections, navOptions: NavOptions?) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction, navOptions) }
}

fun NavController.safeNavigate(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}

fun Int?.orEmpty(default: Int = 0): Int {
    return this ?: default
}


fun ImageView.loadImagesWithGlide(url: String) {
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun Context.showToastMessage(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun <T> Fragment.handleResponseData(
    loadingState: () -> Unit,
    successState: (data:T?) -> Unit,
    errorState: (data:T?) -> Unit,
    liveData: LiveData<Resource<T>>
) {
    liveData.observe(viewLifecycleOwner) { response ->
        when (response.status) {
            Resource.Status.LOADING -> {
                (requireActivity() as MainActivity)
                loadingState()
            }
            Resource.Status.SUCCESS -> {
                successState(response.data)
            }
            Resource.Status.ERROR -> {
                requireContext().showToastMessage(response.message)
                errorState(response.data)
            }
        }
    }
}
