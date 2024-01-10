package com.mattgdot.a2020.ui.screens.HomeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mattgdot.a2020.R

@Composable
fun TimeSelector(timeUnit:String,type:String, sliderInitPosition:Float, onValueChange:(Float)->Unit) {
    var sliderPosition by remember { mutableFloatStateOf(sliderInitPosition) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = (sliderPosition.toInt()*5).toString(),
                fontWeight = FontWeight.W500,
                fontSize = TextUnit(30f, TextUnitType.Sp),
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            )
            Column(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp).fillMaxWidth()
            ) {
                Text(
                    text = timeUnit.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(17f, TextUnitType.Sp),
                )
                Text(
                    text = type.lowercase(),
                    fontWeight = FontWeight.W300,
                    fontSize = TextUnit(14f, TextUnitType.Sp),
                )
            }
        }
        Slider(
            value = sliderPosition,
            onValueChange = {
                 sliderPosition = it
                 onValueChange(it)
            },
            steps = 10,
            valueRange = 1f..12f
        )
    }
}