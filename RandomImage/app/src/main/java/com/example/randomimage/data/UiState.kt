package com.example.randomimage.data

import com.example.randomimage.ImageCategory

data class UiState (
    val selectedCategory: ImageCategory = ImageCategory.MOVIE,
    val width: Double = 0.0,
    val height: Double = 0.0
)