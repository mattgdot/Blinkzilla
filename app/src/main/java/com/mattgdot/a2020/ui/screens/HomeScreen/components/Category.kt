package com.mattgdot.a2020.ui.screens.HomeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import java.util.Locale.Category

@Composable
fun Category(name:String, value:Int) {
   Column(
       modifier = Modifier.padding(10.dp),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
   ) {
       Text(
           text = name.uppercase(),
           fontWeight = FontWeight.W400 ,
           fontSize = TextUnit(15f, TextUnitType.Sp),
       )
       Text(
           text = value.toString(),
           fontWeight = FontWeight.W500,
           fontSize = TextUnit(40f, TextUnitType.Sp),
       )
   }
}