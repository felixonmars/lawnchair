package app.lawnchair.ui.preferences.components

import android.view.View
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import app.lawnchair.LauncherPreviewManager
import app.lawnchair.util.lifecycleState
import com.android.launcher3.InvariantDeviceProfile
import com.android.launcher3.LauncherAppState

@Composable
fun DummyLauncherBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val idp = remember { InvariantDeviceProfile.INSTANCE.get(context) }
    val dp = idp.getDeviceProfile(context)
    val ratio = dp.widthPx.toFloat() / dp.heightPx.toFloat()

    Box(
        modifier = modifier
            .aspectRatio(ratio, matchHeightConstraintsFirst = true)
            .background(Color.Black)
    ) {
        content()
    }
}

@Composable
fun DummyLauncherLayout(
    idp: InvariantDeviceProfile,
    modifier: Modifier = Modifier,
) {
    val previewView = createPreviewView(idp)
    Crossfade(targetState = previewView) {
        val view = it
        AndroidView(
            factory = { context ->
                view ?: View(context)
            },
            modifier = modifier
        )
    }
}

@Composable
fun invariantDeviceProfile(): InvariantDeviceProfile {
    val context = LocalContext.current
    return LauncherAppState.getIDP(context)
}

@Composable
fun createPreviewView(idp: InvariantDeviceProfile = invariantDeviceProfile()): View? {
    val context = LocalContext.current
    val lifecycleState = lifecycleState()
    if (!lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
        return null
    }
    val previewManager = remember { LauncherPreviewManager(context) }
    return remember(idp) { previewManager.createPreviewView(idp) }
}
