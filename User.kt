package com.example.safezone

data class User(
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var birthdate: String? = null,
    var birthDate: String? = null,  // Add this line
    var role: String? = null
)
