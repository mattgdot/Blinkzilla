package com.mattgdot.a2020.ui.screens.BreakScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.AdSize
import com.mattgdot.a2020.R
import com.mattgdot.a2020.ui.components.AdaptiveBanner
import com.mattgdot.a2020.utils.rememberLifecycleEvent
import kotlinx.coroutines.delay
import java.time.format.TextStyle
import kotlin.random.Random

@Preview
@Composable
fun BreakScreen(breakViewModel: BreakViewModel = viewModel()) {

    val activity = (LocalContext.current as? Activity)
    val lifecycleEvent = rememberLifecycleEvent()

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE || lifecycleEvent == Lifecycle.Event.ON_DESTROY) {
            activity?.finishAndRemoveTask()
            try{
                breakViewModel.timer.cancel()
            } catch (_:Exception){

            }
        }
    }

    BackHandler {
        activity?.finishAndRemoveTask()
        try{
            breakViewModel.timer.cancel()
        } catch (_:Exception){

        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text =  if(!breakViewModel.timerFinished) breakViewModel.message else stringResource(
                id = R.string.well_done
            ),
            fontSize = TextUnit(23f, TextUnitType.Sp),
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        if(!breakViewModel.timerFinished) {
            LinearProgressIndicator(
                breakViewModel.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            )
        } else {
            Button(
                onClick = {
                    activity?.finishAndRemoveTask()
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.close)
                )
            }
        }

        Spacer(modifier = Modifier.padding(20.dp))

        AdaptiveBanner(Modifier,AdSize.MEDIUM_RECTANGLE)
    }
}