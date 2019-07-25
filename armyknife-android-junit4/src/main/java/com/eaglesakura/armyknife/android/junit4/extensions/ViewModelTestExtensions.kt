package com.eaglesakura.armyknife.android.junit4.extensions

import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * await Activity.onCreate() message.
 *
 * e.g.)
 * fun testFunction() = compatibleBlockingTest {
 *      val activity = makeActivity().awaitOnCreate()
 * }
 */
suspend fun <T : FragmentActivity> T.awaitOnCreate(): T {
    withContext(Dispatchers.Main) {
        withTimeout(TimeUnit.SECONDS.toMillis(1)) {
            while (!lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                delay(1)
            }
        }
    }
    return this
}

/**
 * await Fragment.onCreate() message.
 *
 * e.g.)
 * fun testFunction() = compatibleBlockingTest {
 *      val fragment = makeFragment(ExampleFragment::class).awaitOnCreate()
 * }
 */
suspend fun <T : Fragment> T.awaitOnCreate(): T {
    withContext(Dispatchers.Main) {
        withTimeout(TimeUnit.SECONDS.toMillis(1)) {
            while (!lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                delay(1)
            }
        }
    }
    return this
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <A : FragmentActivity, VM : ViewModel> makeActivityViewModel(
    activityClass: KClass<A>,
    factory: (activity: A) -> VM
): VM {
    val activity = makeActivity(activityClass)
    return withContext(Dispatchers.Main) {
        factory(activity)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <VM : ViewModel> makeActivityViewModel(
    factory: (activity: AppCompatActivity) -> VM
): VM {
    val activity = makeActivity(AppCompatActivity::class)
    return withContext(Dispatchers.Main) {
        factory(activity)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <VM : ViewModel> makeActivityViewModel(
    clazz: KClass<VM>
): VM {
    return makeActivityViewModel { activity ->
        ViewModelProviders.of(activity).get(clazz.java)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <VM : ViewModel> makeActivitySavedStateViewModel(
    clazz: KClass<VM>
): VM {
    return makeActivityViewModel { activity ->
        ViewModelProviders.of(activity, SavedStateViewModelFactory(activity)).get(clazz.java)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <A : FragmentActivity, F : Fragment, VM : ViewModel> makeFragmentViewModel(
    activityClass: KClass<A>,
    fragmentClass: KClass<F>,
    factory: (fragment: F) -> VM
): VM {
    val fragment = makeFragment(activityClass, fragmentClass)
    return withContext(Dispatchers.Main) {
        factory(fragment)
    }
}

/**
 * Make testing ViewModel with View.
 */
suspend fun <A : FragmentActivity, F : Fragment, VM : ViewModel> makeFragmentViewModel(
    activityClass: KClass<A>,
    @IdRes containerViewId: Int,
    fragmentClass: KClass<F>,
    factory: (fragment: F) -> VM
): VM {
    val fragment = makeFragment(activityClass, containerViewId, fragmentClass)
    return withContext(Dispatchers.Main) {
        factory(fragment)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <VM : ViewModel> makeFragmentViewModel(
    factory: (fragment: Fragment) -> VM
): VM {
    val fragment = makeFragment(AppCompatActivity::class, Fragment::class)
    return withContext(Dispatchers.Main) {
        factory(fragment)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <VM : ViewModel> makeFragmentViewModel(clazz: KClass<VM>): VM {
    return makeFragmentViewModel { fragment ->
        ViewModelProviders.of(fragment).get(clazz.java)
    }
}

/**
 * Make testing ViewModel without View.
 */
suspend fun <VM : ViewModel> makeFragmentSavedStateViewModel(clazz: KClass<VM>): VM {
    return makeFragmentViewModel { fragment ->
        ViewModelProviders.of(fragment, SavedStateViewModelFactory(fragment)).get(clazz.java)
    }
}

/**
 * ViewModel.LiveData<*> field are active.
 */
@UiThread
fun ViewModel.activeAllLiveDataForTest(lifecycleOwner: LifecycleOwner? = null) {
    val self = this
    self.javaClass.kotlin.declaredMemberProperties.forEach { prop ->
        prop.isAccessible = true
        (prop.getter.call(self) as? LiveData<*>)?.also { liveData ->
            Log.d("ViewModelTest", "${self.javaClass.simpleName}.${prop.name} is active")
            if (lifecycleOwner != null) {
                liveData.observe(lifecycleOwner, Observer { })
            } else {
                liveData.observeForever { }
            }
        }
    }
}