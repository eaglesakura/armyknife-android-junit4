package com.eaglesakura.armyknife.android.junit4

import com.eaglesakura.armyknife.android.junit4.extensions.ROBOLECTRIC
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Dispatchers for UnitTest.
 */
object TestDispatchers {

    /**
     * always use Dispatcher.Default
     */
    val Default = Dispatchers.Default

    /**
     * always use Dispatcher.Main
     */
    val Main: CoroutineDispatcher = Dispatchers.Main

    /**
     * Delegate to dispatcher for launch in ActivityTestRule.
     * in Instrumentation test, then returns the Default dispatcher.
     * in Local Unit Test, then returns the Main dispatcher.
     */
    val ActivityLaunchRule: CoroutineDispatcher by lazy {
        if (ROBOLECTRIC) {
            Dispatchers.Main
        } else {
            Dispatchers.Default
        }
    }
}
