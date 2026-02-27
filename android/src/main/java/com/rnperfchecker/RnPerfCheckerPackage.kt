package com.rnperfchecker

import com.facebook.react.*
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class RNPerfCheckerPackage : TurboReactPackage() {

  override fun getModule(name: String, reactContext: ReactApplicationContext)
      : NativeModule? {
    return if (name == RNPerfCheckerModule.NAME) {
      RNPerfCheckerModule(reactContext)
    } else null
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    return ReactModuleInfoProvider {
      mapOf(
        RNPerfCheckerModule.NAME to ReactModuleInfo(
          RNPerfCheckerModule.NAME,
          RNPerfCheckerModule.NAME,
          false,
          false,
          true,
          false,
          true
        )
      )
    }
  }
}