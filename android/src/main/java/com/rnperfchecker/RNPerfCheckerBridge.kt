package com.rnperfchecker

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

object RNPerfCheckerBridge {

  private var startTime = 0L
  private var longTasks = 0
  private var frames = 0
  private var dropped = 0
  private var avgCommitMs = 0.0
  private var commitCount = 0

  fun start() {
    startTime = System.currentTimeMillis()
    UIFPSMonitor.start()
    JSLongTaskMonitor.start()
  }

  fun stop(): WritableMap {
    val duration = System.currentTimeMillis() - startTime

    val fabricData = FabricCommitMonitor.stop()
    avgCommitMs = fabricData.first
    commitCount = fabricData.second

    val uiData = UIFPSMonitor.stop()
    frames = uiData.first
    dropped = uiData.second

    longTasks = JSLongTaskMonitor.stop()

    val nativeHeap = MemoryMonitor.getNativeHeap()
    val javaHeap = MemoryMonitor.getJavaHeap()
    val hermesHeap = RNPerfCheckerJSI.getHermesHeap()
    val hostObjects = RNPerfCheckerJSI.getHostObjectCount()

    val fps = if (duration > 0) frames * 1000.0 / duration else 0.0

    val score = computeScore(fps, dropped, longTasks, nativeHeap)

    val result = Arguments.createMap()

    val js = Arguments.createMap()
    js.putInt("longTasks", longTasks)

    val ui = Arguments.createMap()
    ui.putDouble("fps", fps)
    ui.putInt("droppedFrames", dropped)

    val memory = Arguments.createMap()
    memory.putDouble("nativeHeapMB", nativeHeap / 1024.0 / 1024.0)
    memory.putDouble("javaHeapMB", javaHeap / 1024.0 / 1024.0)
    memory.putDouble("hermesHeapMB", hermesHeap / 1024.0 / 1024.0)
    memory.putInt("hostObjectCount", hostObjects)

    val fabric = Arguments.createMap()
    fabric.putDouble("avgCommitMs", avgCommitMs)
    fabric.putInt("commitCount", commitCount)

    result.putDouble("score", score)
    result.putMap("js", js)
    result.putMap("ui", ui)
    result.putMap("memory", memory)
    result.putMap("fabric", fabric)

    return result
  }

  fun current(): WritableMap {
    return stop()
  }

  private fun computeScore(
    fps: Double,
    dropped: Int,
    longTasks: Int,
    heap: Long
  ): Double {
    var score = 100.0
    if (fps < 55) score -= 20
    if (dropped > 5) score -= 15
    if (longTasks > 3) score -= 15
    if (heap > 150 * 1024 * 1024) score -= 20
    if (avgCommitMs > 8) score -= 20
    return score.coerceAtLeast(0.0)
  }
}