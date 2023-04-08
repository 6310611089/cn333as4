package com.example.randomimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.randomimage.data.UiState
import com.example.randomimage.ui.theme.RandomImageTheme


enum class Screen(val endpoint: String) {
    Home("home"),
    Image("show image"),
}

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
                    RandomImageScreen()
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
fun RandomImageScreen(
    imageViewModel: ImageViewModel = viewModel(),
){
    val navController = rememberNavController()
    val imageuiState by imageViewModel.uiState.collectAsState()
    NavHost(navController = navController, startDestination =  Screen.Home.name){
        composable(Screen.Home.name){
            RandomImage(navController = navController, imageViewModel)
        }
        composable(Screen.Image.name){
            ShowImage(imageuiState)
        }
    }
}

@Composable
fun RandomImage(
    navController: NavController,
    imageViewModel: ImageViewModel,
){
    var width by remember { mutableStateOf("500") }
    var height by remember { mutableStateOf("500") }
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
        if (width.isNotBlank() and height.isNotBlank()) {
            val imWidth = width.toInt()
            val imHeight = height.toInt()
            imageViewModel.searchImage(selectedCategory,imWidth,imHeight)
            Button(onClick = {
                navController.navigate(Screen.Image.name)
            }) {
                Text(text = "show image")
            }
        }
    }
}



@Composable
fun ShowImage(
    uiState: UiState,
){
    var src = "https://loremflickr.com/${uiState.width}/${uiState.height}"
    Text(text = src)
    AsyncImage(
        //model = "https://ichef.bbci.co.uk/news/999/cpsprodpb/15951/production/_117310488_16.jpg",
        //model = "https://api.lorem.space/image/car?w=150&h=150",
        model = ImageRequest.Builder(LocalContext.current)
            .data(src)
            .crossfade(true)
            .build(),
        contentDescription = "",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .height(419.dp)
//                .width(210.dp)
    )
}