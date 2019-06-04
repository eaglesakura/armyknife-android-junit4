@file:Suppress("unused")

package com.eaglesakura.armyknife.android.junit4.extensions

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowLooper

/**
 * Robolectric runtime is true.
 */
val ROBOLECTRIC: Boolean = try {
    Class.forName("org.robolectric.Robolectric")
    true
} catch (err: ClassNotFoundException) {
    false
}

private const val TAG = "JUnit4"

/**
 * This instance is a test-target application.
 * Application instance by app.apk
 */
val targetApplication: Application
    get() = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application

/**
 * This instance is a test-target context.
 * Context instance by app.apk
 */
val targetContext: Context
    get() = InstrumentationRegistry.getInstrumentation().targetContext

/**
 * This instance is a test context.
 * Context instance by app-test.apk
 */
val testContext: Context
    get() = InstrumentationRegistry.getInstrumentation().context

private fun beforeRobolectricTest() {
    if (!ROBOLECTRIC) {
        return
    }

    ShadowLog.stream = System.out
}

/**
 * Run block in Local Unit Test only.
 */
fun inLocalTest(block: () -> Unit) {
    if (ROBOLECTRIC) {
        block()
    }
}

/**
 * Run block in Instrumentation test only.
 */
fun inInstrumentationTest(block: () -> Unit) {
    if (!ROBOLECTRIC) {
        block()
    }
}

/**
 * Test JVM only.
 */
fun localTest(action: () -> Unit) {
    beforeRobolectricTest()

    if (ROBOLECTRIC) {
        action()
    } else {
        Log.i(TAG, "skip InstrumentationTest")
    }
}

/**
 * Test JVM only with Coroutines.
 */
fun localBlockingTest(
    dispatcher: CoroutineDispatcher = TestDispatchers.Default,
    action: suspend CoroutineScope.() -> Unit
) {
    beforeRobolectricTest()

    if (ROBOLECTRIC) {
        compatibleBlockingTest(dispatcher, action)
    } else {
        Log.i(TAG, "skip in InstrumentationTest")
    }
}

/**
 * Test Android Device(or Emulator) only.
 */
fun instrumentationTest(action: () -> Unit) {
    beforeRobolectricTest()

    if (ROBOLECTRIC) {
        Log.i(TAG, "skip in LocalUnitTest")
    } else {
        action()
    }
}

/**
 * Test Android Device(or Emulator) only with Coroutines.
 */
fun instrumentationBlockingTest(
    dispatcher: CoroutineDispatcher = TestDispatchers.Default,
    action: suspend CoroutineScope.() -> Unit
) {
    beforeRobolectricTest()

    if (ROBOLECTRIC) {
        Log.i(TAG, "skip in LocalUnitTest")
    } else {
        compatibleBlockingTest(dispatcher, action)
    }
}

/**
 * Test with suspend.
 *
 * Architecture template created by @eaglesakura
 */
fun compatibleTest(action: () -> Unit) {
    beforeRobolectricTest()
    action()
}

/**
 * Test with suspend.
 *
 * Architecture template created by @eaglesakura
 */
fun compatibleBlockingTest(
    dispatcher: CoroutineDispatcher = TestDispatchers.Default,
    action: suspend CoroutineScope.() -> Unit
) {
    beforeRobolectricTest()

    val job = GlobalScope.async(dispatcher) {
        try {
            action(this)
            null
        } catch (e: Throwable) {
            e
        }
    }

    do {
        if (ROBOLECTRIC) {
            ShadowLooper.runMainLooperToNextTask()
        }
    } while (!job.isCompleted)

    job.getCompleted()?.also {
        throw it
    }
}
