package com.allonapps.superemail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.allonapps.superemail.ui.screens.EmailScreen
import com.allonapps.superemail.ui.screens.ListViewModel
import com.allonapps.superemail.ui.theme.SuperEmailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ListViewModel()
        setContent {
            SuperEmailTheme {
//                val navController = rememberNavController()
                EmailScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    acceptIntent = viewModel::acceptIntent
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SuperEmailTheme {
        Greeting("Android")
    }
}