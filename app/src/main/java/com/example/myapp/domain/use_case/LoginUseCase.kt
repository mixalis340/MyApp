package com.example.myapp.domain.use_case

import com.example.myapp.SimpleResource
import com.example.myapp.domain.repository.AuthRepository

class LoginUseCase (
    private val repository: AuthRepository
    ) {

    suspend operator fun invoke(email: String, password: String): SimpleResource {

        return repository.login(email, password)
    }
            
            
}