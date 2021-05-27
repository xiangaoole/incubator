package com.xiangaoole.android.wanandroid.util

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import timber.log.Timber
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@MainThread
inline fun <reified VB : ViewBinding> Fragment.bindView(
    noinline viewBinder: (View) -> VB
) = FragmentViewBindingDelegate<Fragment, VB>(viewBinder)

class FragmentViewBindingDelegate<in F : Fragment, out VB : ViewBinding>(
    private val viewBinder: (View) -> VB
) : ReadOnlyProperty<F, VB> {

    private var binding: VB? = null

    @MainThread
    override fun getValue(thisRef: F, property: KProperty<*>): VB {
        if (binding != null) return binding!!

        val view = thisRef.requireView()
        val binding = viewBinder(view)

        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Timber.w("Access to ViewBinding in DESTROYED state")
            // Don't save
        } else {
            lifecycle.addObserver(ClearOnDestroyView())
            this.binding = binding
        }

        return binding
    }

    private inner class ClearOnDestroyView : LifecycleEventObserver {

        private val mainHandler = Handler(Looper.getMainLooper())

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
                // To run after [Fragment#onDestroyView]
                mainHandler.post { binding = null }
            }
        }

    }
}
