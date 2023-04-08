package com.example.randomimage

import androidx.lifecycle.ViewModel
import com.example.randomimage.data.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun searchImage(selectedCategory: ImageCategory, width: Int, height: Int){
        _uiState.update { currentState -> currentState.copy(
            selectedCategory = selectedCategory,
            width = width,
            height = height,
        ) }
    }
}