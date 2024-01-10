package com.mattgdot.a2020.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.mattgdot.a2020.R

@Composable
fun AdaptiveBanner(
    modifier: Modifier = Modifier,
    adSize:AdSize
) {
    val applicationContext = LocalContext.current.applicationContext

    Column(
        modifier = modifier.height(adSize.height.dp).fillMaxWidth()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                AdView(applicationContext).apply {
                    setAdSize(
                        adSize
                    )
                    adUnitId = applicationContext.getString((R.string.AD_BANNER_ID))
                    loadAd(AdRequest.Builder().build())
                }
            },
        )
    }
}