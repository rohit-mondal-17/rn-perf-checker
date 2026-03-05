package com.rnperfchecker
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.ReactRootView
import com.facebook.react.fabric.FabricUIManager
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.uimanager.events.EventDispatcherListener
import com.facebook.react.uimanager.events.BatchEventDispatchedListener

internal object FabricCommitMonitor {
  private var totalCommitTimeMs = 0.0
  private var commitCount = 0
  private var batchOpen = false
  private var batchStartNs = 0L
  private var surfaceId: Int? = null
  private var dispatcher: EventDispatcher? = null
  // Non-null, created in start()
  private lateinit var eventListener: EventDispatcherListener
  private lateinit var batchListener: BatchEventDispatchedListener
  fun start(ctx: ReactApplicationContext, surfaceIdFromJs: Int? = null): Boolean {
    if (dispatcher != null) return true
    val act = ctx.currentActivity ?: return false
    val root: ReactRootView = findReactRootView(act) ?: return false
    val sid = surfaceIdFromJs ?: UIManagerHelper.getSurfaceId(root)
    val uiManager = UIManagerHelper.getUIManager(ctx, sid)
    if (uiManager !is FabricUIManager) return false
    val ed = UIManagerHelper.getEventDispatcher(ctx, sid) ?: return false
    surfaceId = sid
    dispatcher = ed
    // Instantiate listeners
    eventListener = EventDispatcherListener { _: Event<*> ->
      if (!batchOpen) {
        batchOpen = true
        batchStartNs = System.nanoTime()
      }
    }
    batchListener = BatchEventDispatchedListener {
      if (batchOpen) {
        val end = System.nanoTime()
        totalCommitTimeMs += (end - batchStartNs) / 1_000_000.0
        commitCount++
        batchOpen = false
      }
    }
    // Register (non-null required)
    dispatcher?.addListener(eventListener)
    dispatcher?.addBatchEventDispatchedListener(batchListener)
    return true
  }
  
  fun stop(): Pair<Double, Int> {
    // Unregister (only if they were created)
    dispatcher?.let { d ->
      if (::eventListener.isInitialized) runCatching { d.removeListener(eventListener) }
      if (::batchListener.isInitialized)  runCatching { d.removeBatchEventDispatchedListener(batchListener) }
    }
    val avg = if (commitCount > 0) totalCommitTimeMs / commitCount else 0.0
    val res = avg to commitCount
    // Reset (do NOT assign null to the lateinit vars)
    totalCommitTimeMs = 0.0
    commitCount = 0
    batchOpen = false
    batchStartNs = 0L
    dispatcher = null
    surfaceId = null
    return res
  }
}