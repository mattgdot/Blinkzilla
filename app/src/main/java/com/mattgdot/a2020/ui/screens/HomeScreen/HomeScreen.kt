package com.mattgdot.a2020.ui.screens.HomeScreen


import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.ads.AdSize
import com.mattgdot.a2020.R
import com.mattgdot.a2020.ui.components.AdaptiveBanner
import com.mattgdot.a2020.ui.screens.HomeScreen.components.BreakCard
import com.mattgdot.a2020.ui.screens.HomeScreen.components.RatingCard
import com.mattgdot.a2020.ui.screens.HomeScreen.components.SectionTitle
import com.mattgdot.a2020.ui.screens.HomeScreen.components.StartButton
import com.mattgdot.a2020.utils.rememberLifecycleEvent

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val lifecycleEvent = rememberLifecycleEvent()

    val notificationsPermissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    var days by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(notificationsPermissionState) {
        if (!notificationsPermissionState.status.isGranted) {
            notificationsPermissionState.launchPermissionRequest()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (!Settings.canDrawOverlays(context)) {
            Toast.makeText(
                context,
                context.getString(R.string.permission_overlay),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            homeViewModel.getData()
            days = homeViewModel.userRating
            homeViewModel.checkIfRunning()
        }
    }

    val userPerf by animateIntAsState(
        targetValue = days,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(Unit) {
        days = homeViewModel.userRating
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SectionTitle(text = stringResource(id = R.string.your_performance))

        RatingCard(
            userPerf,
            homeViewModel.totalBreaks,
            homeViewModel.totalCompleted
        )

        SectionTitle(text = stringResource(id = R.string.modify_breaks))

        BreakCard(homeViewModel.breakDuration, homeViewModel.workDuration, onBreakUpdate = {
            homeViewModel.updateBreak(it)
        }, onWorkUpdate = {
            homeViewModel.updateWork(it)
        })

        StartButton(homeViewModel.breakEnabled, onClick = {
            if (!homeViewModel.breakEnabled) {
                if (!Settings.canDrawOverlays(context)) {
                    openAlertDialog = true
                } else {
                    homeViewModel.setBreakEnabled()
                }
            } else {
                homeViewModel.setBreakEnabled()
            }
        })

        Spacer(modifier = Modifier.padding(5.dp))

        AdaptiveBanner(Modifier.padding(5.dp), AdSize.BANNER)
    }

    if (openAlertDialog) {
        com.mattgdot.a2020.ui.components.AlertDialog(
            onDismissRequest = {
                openAlertDialog = false
            },
            onConfirmation = {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
                launcher.launch(intent)
                openAlertDialog = false
            },
            dialogTitle = stringResource(id = R.string.permission_required),
            dialogText = stringResource(id = R.string.permission_overlay),
            icon = Icons.Default.Info
        )
    }
}
