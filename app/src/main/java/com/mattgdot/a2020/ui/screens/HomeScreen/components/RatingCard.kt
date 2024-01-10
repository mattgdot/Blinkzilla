package com.mattgdot.a2020.ui.screens.HomeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mattgdot.a2020.R

@Composable
fun RatingCard(overallRating:Int, totalRating:Int,completedRating:Int) {
    OutlinedCard(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(id = R.string.your_rating).uppercase(),
                fontWeight = FontWeight.W400,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = "$overallRating%",
                fontWeight = FontWeight.ExtraBold,
                fontSize = TextUnit(70f, TextUnitType.Sp)
            )
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Category(name = stringResource(id = R.string.total_breaks), value = totalRating)
                Category(name = stringResource(id = R.string.total_completed), value = completedRating)
                Category(name = stringResource(id = R.string.total_skipped), value = totalRating-completedRating)
            }
        }
    }
}