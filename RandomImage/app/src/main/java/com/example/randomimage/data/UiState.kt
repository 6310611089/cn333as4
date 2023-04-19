package com.example.randomimage.data

import com.example.randomimage.ImageCategory

data class UiState (
    val selectedCategory: String = ImageCategory.ANIMAL.name,
    val width: Int = 0,
    val height: Int = 0
)