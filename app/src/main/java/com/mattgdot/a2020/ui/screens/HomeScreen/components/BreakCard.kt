package com.mattgdot.a2020.ui.screens.HomeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mattgdot.a2020.R


@Composable
fun BreakCard(breakDuration:Float,workDuration:Float,onBreakUpdate:(Float)->Unit,onWorkUpdate:(Float)->Unit) {
    OutlinedCard(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeSelector(stringResource(id = R.string.seconds),stringResource(id = R.string.duration_break),breakDuration, onValueChange = onBreakUpdate)
            TimeSelector(stringResource(id = R.string.minutes),stringResource(id = R.string.duration_work),workDuration, onValueChange = onWorkUpdate)
        }
    }
}