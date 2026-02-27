package com.rnperfchecker

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = RNPerfCheckerModule.NAME)
class RNPerfCheckerModule(
  reactContext: ReactApplicationContext
) : NativeRNPerfCheckerSpec(reactContext) {

  companion object {
    const val NAME = "RNPerfChecker"

    init {
      System.loadLibrary("rnperfchecker")
    }
  }

  override fun startProfiling() {
    FabricCommitMonitor.start(reactApplicationContext)
    RNPerfCheckerBridge.start()
  }

  override fun stopProfiling(promise: Promise) {
    promise.resolve(RNPerfCheckerBridge.stop())
  }

  override fun getCurrentMetrics(): WritableMap {
    return RNPerfCheckerBridge.current()
  }
}