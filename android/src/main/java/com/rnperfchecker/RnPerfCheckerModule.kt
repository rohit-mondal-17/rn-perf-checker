package com.rnperfchecker
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = RNPerfCheckerModule.NAME)

class RNPerfCheckerModule(
  reactContext: ReactApplicationContext
) : NativeRNPerfCheckerSpec(reactContext) {

  companion object {
    const val NAME = "RNPerfChecker"
    init { System.loadLibrary("rnperfchecker") }
  }

  override fun startProfiling(surfaceIdFromJs: Double?) {
    // Start Fabric monitor; scope to provided surface when available
    FabricCommitMonitor.start(
      reactApplicationContext,
      surfaceIdFromJs?.toInt()
    )
    // Start existing monitors (FPS, JS long tasks, etc.)
    RNPerfCheckerBridge.start()
  }

  override fun stopProfiling(promise: Promise) {
    try {
      // Bridge.stop() already calls FabricCommitMonitor.stop() and merges its metrics
      val result = RNPerfCheckerBridge.stop()
      promise.resolve(result)
    } catch (t: Throwable) {
      promise.reject("RN_PERF_STOP", t)
    }
  }
  
  override fun getCurrentMetrics(): WritableMap {
    return RNPerfCheckerBridge.current()
  }
}