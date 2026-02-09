package com.example.credential.utils.extensions


fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()

fun String?.isValidEmail(): Boolean {
    if (this.isNullOrBlank()) return false
    return Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(this)
}