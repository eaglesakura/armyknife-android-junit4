package com.eaglesakura.armyknife.android.junit4.extensions

import android.app.Activity
import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import kotlinx.coroutines.withContext

/**
 * launchOrGet Activity with after Launch(or cached).
 */
suspend fun <T : Activity> ActivityTestRule<T>.launchOrGet(startIntent: Intent? = null): T {
    return withContext(TestDispatchers.ActivityLaunchRule) {
        activity ?: launchActivity(startIntent)
    }
}
