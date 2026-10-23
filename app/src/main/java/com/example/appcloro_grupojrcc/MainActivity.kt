package com.example.appcloro_grupojrcc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appcloro_grupojrcc.ui.navigation.AppNavigation
import com.example.appcloro_grupojrcc.ui.screen.PuntosScreen
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppCloro_GrupoJRCCTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PuntosPreview() {
    AppCloro_GrupoJRCCTheme {
        PuntosScreen()
    }
}