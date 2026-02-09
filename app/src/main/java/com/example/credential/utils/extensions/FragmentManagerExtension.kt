package com.example.credential.utils.extensions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.Fragment

inline fun FragmentManager.transaction(function : FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().function().commit()
}

fun FragmentManager.addFragment(fragment : Fragment, fragId : Int) {
    transaction { add(fragId, fragment) }
}

fun FragmentManager.replaceFragment(fragment : Fragment, fragId : Int) {
    transaction { replace(fragId, fragment) }
}

fun FragmentManager.replaceFragment(fragment: Fragment, fragId: Int, addToStack: Boolean) {
    transaction {
        if (addToStack) replace(fragId, fragment, fragment.javaClass.canonicalName)
            .addToBackStack(fragment.javaClass.canonicalName)
        else
            replace(fragId, fragment, fragment.javaClass.canonicalName)
    }
}

fun FragmentManager.removeFragment(fragment : Fragment) {
    transaction { remove(fragment) }
}