package com.heartforge.app

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.heartforge.app.core.util.AppForegroundState
import com.heartforge.app.core.worker.ProactiveNudgeWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class HeartForgeApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var foregroundState: AppForegroundState

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        setupProactiveNudges()

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

    private fun setupProactiveNudges() {
        val workRequest = PeriodicWorkRequestBuilder<ProactiveNudgeWorker>(4, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .addTag("proactive_nudge")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "proactive_nudge_work",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
