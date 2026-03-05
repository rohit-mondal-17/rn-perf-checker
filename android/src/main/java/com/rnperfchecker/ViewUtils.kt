package com.rnperfchecker
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.facebook.react.ReactRootView

internal fun findReactRootView(activity: Activity): ReactRootView? {
  val content = activity.findViewById<ViewGroup>(android.R.id.content) ?: return null
  return findReactRootViewIn(content)
}

private fun findReactRootViewIn(view: View?): ReactRootView? {
  if (view == null) return null
  if (view is ReactRootView) return view
  if (view is ViewGroup) {
    for (i in 0 until view.childCount) {
      findReactRootViewIn(view.getChildAt(i))?.let { return it }
    }
  }
  return null
}