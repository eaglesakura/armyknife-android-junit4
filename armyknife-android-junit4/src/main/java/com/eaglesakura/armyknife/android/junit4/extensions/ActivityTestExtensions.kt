package com.eaglesakura.armyknife.android.junit4.extensions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ActivityScenario
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

/**
 * Make testing activity.
 */
suspend fun <T : FragmentActivity> makeActivity(intent: Intent): T {
    return withContext(TestDispatchers.ActivityLaunchRule) {
        val scenario = ActivityScenario.launch<T>(intent)
        val channel = Channel<T>()
        scenario.onActivity { activity ->
            GlobalScope.launch { channel.send(activity) }
        }
        channel.receive()
    }
}

/**
 * Make testing activity.
 */
suspend fun <T : FragmentActivity> makeActivity(clazz: KClass<T>): T {
    return withContext(TestDispatchers.ActivityLaunchRule) {
        val scenario = ActivityScenario.launch(clazz.java)
        val channel = Channel<T>()
        scenario.onActivity { activity ->
            GlobalScope.launch { channel.send(activity) }
        }
        channel.receive()
    }
}

/**
 * Make testing activity.
 */
suspend fun makeActivity(): AppCompatActivity = makeActivity(AppCompatActivity::class)
