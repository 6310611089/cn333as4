package com.example.randomimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    ANIMAL("animal"),
    PLACE("place"),
    TRANSPORT("transport"),
    NATURE("nature"),
    OTHER("other"),
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
            ShowImage(imageuiState, onClick = {navController.popBackStack()})
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
    var selectedCategory by remember { mutableStateOf(ImageCategory.ANIMAL) }
    var ctg by remember { mutableStateOf(selectedCategory.name.lowercase()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Input image size:", modifier = Modifier.padding(16.dp))
        OutlinedTextField(
            value = width,
            onValueChange = { width = it },
            label = { Text("Width") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
        )
        if (width.isBlank()){
            Text(stringResource(R.string.warn))
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
        )
        if (height.isBlank()){
            Text(stringResource(R.string.warn))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Select image category:", modifier = Modifier.padding(16.dp))
        Column(modifier = Modifier.padding(0.dp)) {
            ImageCategory.values().forEach { category ->
                Row(modifier = Modifier.padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                    Column() {
                        Text(category.name)
                        if (selectedCategory.name == "OTHER"){
                            if (category.name == "OTHER"){
                                OutlinedTextField(
                                value = ctg,
                                onValueChange = {ctg = it},
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth(),
                                )
                                if (ctg.isBlank()){
                                    Text(stringResource(R.string.warn))
                                }
                            }

                        } else {
                            ctg = selectedCategory.name.lowercase()
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (width.isNotBlank() and height.isNotBlank() and ctg.isNotBlank()) {
            val imWidth = width.toInt()
            val imHeight = height.toInt()
            imageViewModel.searchImage(ctg,imWidth,imHeight)
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
    onClick: () -> Unit
){
    var src = "https://loremflickr.com/${uiState.width}/${uiState.height}/${uiState.selectedCategory}"
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Here is the result.")
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
        Button(onClick = onClick) {
            Text(text = "Go back")
        }
    }

}