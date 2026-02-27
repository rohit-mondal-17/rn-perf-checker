import Foundation
import React

@objc(RNPerf)
class RNPerf: NSObject {

  @objc
  func startProfiling() {
    RNPerfBridge.shared.start()
  }

  @objc
  func stopProfiling(
    _ resolve: RCTPromiseResolveBlock,
    rejecter reject: RCTPromiseRejectBlock
  ) {
    let result = RNPerfBridge.shared.stop()
    resolve(result)
  }

  @objc
  func getCurrentMetrics() -> NSDictionary {
    return RNPerfBridge.shared.current()
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return false
  }
}