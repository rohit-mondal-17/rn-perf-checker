import UIKit

class UIFPSMonitor {

  static let shared = UIFPSMonitor()

  private var displayLink: CADisplayLink?
  private var lastTimestamp: CFTimeInterval = 0
  private(set) var frameCount = 0
  private(set) var droppedFrames = 0

  func start() {
    frameCount = 0
    droppedFrames = 0
    lastTimestamp = 0

    displayLink = CADisplayLink(
      target: self,
      selector: #selector(tick)
    )
    displayLink?.add(to: .main, forMode: .common)
  }

  func stop() -> (frameCount: Double, droppedFrames: Int) {
    displayLink?.invalidate()
    displayLink = nil
    return (Double(frameCount), droppedFrames)
  }

  @objc private func tick(link: CADisplayLink) {
    if lastTimestamp != 0 {
      let delta = link.timestamp - lastTimestamp
      if delta > (1.0 / 60.0) * 1.5 {
        droppedFrames += 1
      }
    }
    frameCount += 1
    lastTimestamp = link.timestamp
  }
}