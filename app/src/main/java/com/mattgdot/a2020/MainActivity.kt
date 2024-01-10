package com.mattgdot.a2020

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mattgdot.a2020.ui.screens.HomeScreen.HomeScreen
import com.mattgdot.a2020.ui.theme.BlinkzillaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlinkzillaTheme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier.padding(
                        PaddingValues(
                            0.dp,
                            innerPadding.calculateTopPadding(),
                            0.dp,
                            innerPadding.calculateBottomPadding()
                        )
                    )) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}