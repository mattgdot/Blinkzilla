package com.mattgdot.a2020.ui.screens.HomeScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(text:String) {
    Text(
        text = text.uppercase(),
        fontWeight = FontWeight.W600,
        fontSize = TextUnit(18f, TextUnitType.Sp),
        modifier = Modifier.padding(10.dp)
    )
}