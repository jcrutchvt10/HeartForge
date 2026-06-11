package com.heartforge.app

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.heartforge.app.core.util.AppForegroundState
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HeartForgeApplication : Application() {

    @Inject
    lateinit var foregroundState: AppForegroundState

    override fun onCreate() {
        super.onCreate()

        // Track foreground/background via ProcessLifecycleOwner
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> foregroundState.onForeground()
                    Lifecycle.Event.ON_STOP -> foregroundState.onBackground()
                    else -> {}
                }
            }
        )
    }
}
