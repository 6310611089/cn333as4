package com.example.randomimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.randomimage.ui.theme.RandomImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomImageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RandomImage()
                }
            }
        }
    }
}

enum class ImageCategory(val endpoint: String) {
    MOVIE("movie"),
    GAME("game"),
    ALBUM("album"),
    BOOK("book"),
    FACE("face"),
    FASHION("fashion"),
    SHOES("shoes"),
    WATCH("watch"),
    FURNITURE("furniture")
}

@Composable
fun RandomImage() {
    var width by remember { mutableStateOf("150") }
    var height by remember { mutableStateOf("220") }
    var selectedCategory by remember { mutableStateOf(ImageCategory.MOVIE) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Input image size:", modifier = Modifier.padding(16.dp))
        OutlinedTextField(
            value = width,
            onValueChange = { width = it },
            label = { Text("Width") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Select image category:", modifier = Modifier.padding(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            ImageCategory.values().forEach { category ->
                RadioButton(
                    selected = category == selectedCategory,
                    onClick = { selectedCategory = category }
                )
                Text(category.name)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val imageUrl = "https://api.lorem.space/image/${selectedCategory.endpoint}?w=$width&h=$height"
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Random image",
            modifier = Modifier.fillMaxWidth()
        )
    }
}