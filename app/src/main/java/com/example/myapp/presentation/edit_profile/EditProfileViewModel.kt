package com.example.myapp.presentation.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.myapp.presentation.UiText
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.myapp.Constants
import com.example.myapp.R
import com.example.myapp.Resource
import com.example.myapp.data.request.UpdateProfileData
import com.example.myapp.domain.use_case.*
import com.example.myapp.presentation.login.LoginViewModel
import com.example.myapp.presentation.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    savedStateHandle: SavedStateHandle,//to get current userId
): ViewModel() {


    var state by mutableStateOf(EditProfileState())
    var profileState by mutableStateOf(ProfileState())

    init {
        savedStateHandle.get<String>("userId")?.let { userId ->
            getProfile(userId)
        }
    }
    //It's purpose is to send events from the ViewModel to UI
    private val _eventFlow = MutableSharedFlow<EditProfileViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun getProfile(userId: String) {
        profileState = profileState.copy(isLoading = true)
        viewModelScope.launch {
            val result = profileUseCases.getProfile(userId)
            when(result) {
                is Resource.Success -> {
                    val profile = result.data ?: kotlin.run {
                        _eventFlow.emit(UiEvent.SnackbarEvent(
                            UiText.StringResource(R.string.error_couldnt_load_profile)
                        )
                        )
                        return@launch
                    }
                    state = state.copy(username = profile.username)
                    state = state.copy(bio = profile.bio)
                    profileState = profileState.copy(
                        profile = profile,
                        isLoading = false)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.SnackbarEvent(
                        result.uiText ?: UiText.StringResource(R.string.error_unkown)
                    )
                    )
                    return@launch
                }
            }
        }
    }
    //Receives events from our screen
    fun onEvent(event: EditProfileEvent) {
        when(event) {
            is EditProfileEvent.UsernameChanged -> {
                state = state.copy(username = event.username)
            }
            is EditProfileEvent.BioChanged -> {
                state = state.copy(bio = event.bio)
            }
            is EditProfileEvent.CropProfilePicture -> {
                state = state.copy(profilePictureUri = event.uri)
            }

            is EditProfileEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(1000L)
            val result = profileUseCases.updateProfile(
                updateProfileData = UpdateProfileData(
                    username = state.username,
                    bio = state.bio
                ),
                profilePictureUri = state.profilePictureUri
            )
            state = state.copy(isLoading = false)
            when(result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            UiText.StringResource(R.string.updated_profile)
                        )
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            result.uiText ?: UiText.StringResource(R.string.error_unkown)
                        )
                    )
                }
            }
        }
    }


    sealed class UiEvent {
        data class SnackbarEvent(val uiText: UiText): UiEvent()
        data class Navigate(val route: String): UiEvent()
    }
}