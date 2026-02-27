import Foundation

class RNPerfBridge {

  static let shared = RNPerfBridge()

  private var startTime: TimeInterval = 0

  func start() {
    startTime = Date().timeIntervalSince1970
    UIFPSMonitor.shared.start()
    JSLongTaskMonitor.start()
  }

  func stop() -> NSDictionary {
    let duration = Date().timeIntervalSince1970 - startTime

    let uiStats = UIFPSMonitor.shared.stop()
    let longTasks = JSLongTaskMonitor.stop()

    let memoryStats = MemoryMonitor.getStats()
    let hermesStats = HermesPerfBridge.getHermesStats()

    let fps = uiStats.frameCount / duration

    let score = computeScore(
      fps: fps,
      dropped: uiStats.droppedFrames,
      longTasks: longTasks,
      memoryMB: memoryStats.nativeHeapMB
    )

    return [
      "score": score,
      "js": [
        "longTasks": longTasks
      ],
      "ui": [
        "fps": fps,
        "droppedFrames": uiStats.droppedFrames
      ],
      "memory": memoryStats,
      "hermes": hermesStats
    ]
  }

  func current() -> NSDictionary {
    return stop()
  }

  private func computeScore(
    fps: Double,
    dropped: Int,
    longTasks: Int,
    memoryMB: Double
  ) -> Double {
    var score = 100.0

    if fps < 55 { score -= 20 }
    if dropped > 5 { score -= 15 }
    if longTasks > 3 { score -= 15 }
    if memoryMB > 150 { score -= 20 }

    return max(score, 0)
  }
}