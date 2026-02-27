package com.rnperfchecker

import com.facebook.react.bridge.ReactApplicationContext

class RnPerfCheckerModule(reactContext: ReactApplicationContext) :
  NativeRnPerfCheckerSpec(reactContext) {

  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }

  companion object {
    const val NAME = NativeRnPerfCheckerSpec.NAME
  }
}
