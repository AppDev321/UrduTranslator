package com.core.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController


private const val INVALID_DESTINATION = -1


fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId ?: INVALID_DESTINATION
    currentDestination?.takeIf { destinationId != INVALID_DESTINATION }?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
    }
}

fun Fragment.mayNavigate(destinationIdOfThisFragment: Int): Boolean {

    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id

    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
    return destinationIdInNavController == destinationIdOfThisFragment

}


fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(key: String, value: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

