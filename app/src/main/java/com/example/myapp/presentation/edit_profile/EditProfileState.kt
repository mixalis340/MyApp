package com.example.myapp.presentation.edit_profile

import com.example.myapp.presentation.UiText

data class EditProfileState(
    //When user opens the screen, this is the state that reflects what he should see first
    val email: String = "",
    val emailError: UiText? = null,
    val username: String = "",
    val usernameError: UiText? = null,
    val bio: String = "",
    val bioError: UiText? = null,
)

