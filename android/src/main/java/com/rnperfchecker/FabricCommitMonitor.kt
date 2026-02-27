package com.rnperfchecker

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.UIManager
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.fabric.FabricUIManager
import com.facebook.react.uimanager.UIManagerListener

object FabricCommitMonitor {

  private var totalCommitTimeMs = 0.0
  private var commitCount = 0
  private var startTimeNs = 0L
  private var listenerAdded = false

  fun start(reactContext: ReactApplicationContext) {

    if (listenerAdded) return

    val currentActivity = reactContext.currentActivity ?: return

    val rootView = currentActivity.findViewById<com.facebook.react.ReactRootView>(
      android.R.id.content
    ) ?: return

    val surfaceId = rootView.surfaceId // rootView.id

    val uiManager: UIManager? =
      UIManagerHelper.getUIManager(reactContext, surfaceId)

    if (uiManager !is FabricUIManager) {
      return
    }

    uiManager.addUIManagerEventListener(object : UIManagerListener {

      override fun willDispatchViewUpdates(uiManager: UIManager) {
        startTimeNs = System.nanoTime()
      }

      override fun didDispatchMountItems(uiManager: UIManager) {
        val end = System.nanoTime()
        val durationMs = (end - startTimeNs) / 1_000_000.0

        totalCommitTimeMs += durationMs
        commitCount++
      }
    })

    listenerAdded = true
  }

  fun stop(): Pair<Double, Int> {
    val avg = if (commitCount > 0)
      totalCommitTimeMs / commitCount
    else 0.0

    val result = Pair(avg, commitCount)

    totalCommitTimeMs = 0.0
    commitCount = 0
    listenerAdded = false

    return result
  }
}