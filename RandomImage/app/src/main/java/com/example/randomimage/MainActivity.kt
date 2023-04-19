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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
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
                    color = Color(0xFFFFC0CB)
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
        Text(stringResource(R.string.image_size),
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp))
        OutlinedTextField(
            value = width,
            onValueChange = { width = it },
            label = { Text(stringResource(R.string.input_width)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
        )
        if (width.isBlank()){
            Text(stringResource(R.string.warn))
        } else {
            if (width.isDigitsOnly()){
                val width_ = width.toInt()
                if (width_ <= 0){
                    Text(stringResource(R.string.invalid))
                }
            } else {
                Text(stringResource(R.string.invalid))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text(stringResource(R.string.input_height)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
        )
        if (height.isBlank()){
            Text(stringResource(R.string.warn))
        } else {
            if (height.isDigitsOnly()){
                val height_ = height.toInt()
                if (height_ <= 0){
                    Text(stringResource(R.string.invalid))
                }
            } else {
                Text(stringResource(R.string.invalid))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.select_category),
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp))
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
                                    label = { Text(stringResource(R.string.input_category)) },
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
        if (width.isNotBlank() and width.isDigitsOnly() and height.isNotBlank() and height.isDigitsOnly() and ctg.isNotBlank()) {
            val imWidth = width.toInt()
            val imHeight = height.toInt()
            imageViewModel.searchImage(ctg,imWidth,imHeight)
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                Button(
                    onClick = { navController.navigate(Screen.Image.name) },
                    modifier = Modifier.align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ){
                    Text(
                        text = stringResource(R.string.show_image),
                        style = TextStyle(fontSize = 20.sp),
                        color = Color.White
                    )
                }
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
        Text(
            text = stringResource(R.string.result),
            style = TextStyle(fontSize = 26.sp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(src)
                .crossfade(true)
                .build(),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ){
            Text(
                text = stringResource(R.string.go_back),
                style = TextStyle(fontSize = 20.sp),
                color = Color.White
            )
        }
    }
}