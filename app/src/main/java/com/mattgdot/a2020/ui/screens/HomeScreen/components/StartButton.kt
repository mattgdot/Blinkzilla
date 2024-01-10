package com.mattgdot.a2020.ui.screens.HomeScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mattgdot.a2020.R

@Composable
fun StartButton(breaksEnabled:Boolean,onClick:()->Unit) {
    if(breaksEnabled){
        OutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = { onClick() }
        ) {
            Text(stringResource(id = R.string.stop_breaks))
        }
    } else  {
        Button(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = { onClick() }
        ) {
            Text(stringResource(id = R.string.start_breaks))
        }
    }
}