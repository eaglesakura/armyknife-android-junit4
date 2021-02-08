package com.eaglesakura.armyknife.android.junit4.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import androidx.test.core.app.ActivityScenario
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import kotlin.reflect.KClass
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Make testing fragment.
 */
suspend fun <A : FragmentActivity, F : Fragment> makeFragment(
    activityClass: KClass<A>,
    @IdRes containerViewId: Int,
    factory: (activity: A) -> F
): F {
    return withContext(TestDispatchers.ActivityLaunchRule) {
        val scenario = ActivityScenario.launch(activityClass.java)
        val channel = Channel<F>()
        scenario.onActivity { activity ->
            val fragment = factory(activity)
            activity.supportFragmentManager.transaction {
                if (containerViewId == 0) {
                    add(fragment, fragment.javaClass.name)
                } else {
                    add(containerViewId, fragment, fragment.javaClass.name)
                }
            }
            GlobalScope.launch { channel.send(fragment) }
        }
        channel.receive()
    }.also { fragment ->
        // await attach to Activity.
        withContext(TestDispatchers.Main) {
            while (fragment.activity == null) {
                delay(1)
            }
        }
    }
}

/**
 * Make testing fragment without View.
 */
suspend fun <A : FragmentActivity, F : Fragment> makeFragment(
    activityClass: KClass<A>,
    factory: (activity: A) -> F
): F {
    return makeFragment(activityClass, 0x00, factory)
}

/**
 * Make testing fragment with View.
 */
suspend fun <A : FragmentActivity, F : Fragment> makeFragment(
    activityClass: KClass<A>,
    @IdRes containerViewId: Int,
    fragmentClass: KClass<F>
): F {
    @Suppress("MoveLambdaOutsideParentheses")
    return makeFragment(
        activityClass, 0x00,
        {
            fragmentClass.java.newInstance()
        }
    )
}

/**
 * Make testing fragment without View.
 */
suspend fun <A : FragmentActivity, F : Fragment> makeFragment(
    activityClass: KClass<A>,
    fragmentClass: KClass<F>
): F {
    @Suppress("MoveLambdaOutsideParentheses")
    return makeFragment(
        activityClass, 0x00,
        {
            fragmentClass.java.newInstance()
        }
    )
}

/**
 * Make testing fragment with View.
 */
suspend fun <F : Fragment> makeFragment(@IdRes containerViewId: Int, factory: (activity: AppCompatActivity) -> F): F {
    return makeFragment(AppCompatActivity::class, containerViewId, factory)
}

/**
 * Make testing fragment without View.
 */
suspend fun <F : Fragment> makeFragment(factory: (activity: AppCompatActivity) -> F): F {
    return makeFragment(AppCompatActivity::class, 0x00, factory)
}

/**
 * Make testing fragment without View.
 */
suspend fun <F : Fragment> makeFragment(fragmentClass: KClass<F>): F {
    @Suppress("MoveLambdaOutsideParentheses")
    return makeFragment(AppCompatActivity::class, 0x00, { fragmentClass.java.newInstance() })
}
