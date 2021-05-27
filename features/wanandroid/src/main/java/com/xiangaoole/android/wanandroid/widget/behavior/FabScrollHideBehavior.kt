package com.xiangaoole.android.wanandroid.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * FAB button hide and show behavior when scroll up and down.
 */
class FabScrollHideBehavior : FloatingActionButton.Behavior {

    constructor() : super()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // Ensure we react to vertical scroll
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout,
                    child,
                    directTargetChild,
                    target,
                    axes,
                    type
                )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )

        if (dyConsumed > 0 && child.isVisible) {
            child.visibility = View.INVISIBLE
        } else if (dyConsumed < 0 && !child.isVisible) {
            child.show()
        }
    }
}