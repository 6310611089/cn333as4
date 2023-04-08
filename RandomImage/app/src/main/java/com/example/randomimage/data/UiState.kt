package com.example.randomimage.data

import com.example.randomimage.ImageCategory

data class UiState (
    val selectedCategory: ImageCategory = ImageCategory.MOVIE,
    val width: Int = 0,
    val height: Int = 0
)