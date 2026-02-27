package com.rnperfchecker

import android.view.Choreographer

object UIFPSMonitor {

  private var frameCount = 0
  private var droppedFrames = 0
  private var lastFrameTime = 0L
  private var running = false

  private val callback = object : Choreographer.FrameCallback {
    override fun doFrame(frameTimeNanos: Long) {
      if (!running) return

      if (lastFrameTime != 0L) {
        val deltaMs = (frameTimeNanos - lastFrameTime) / 1_000_000.0
        if (deltaMs > 16.6) droppedFrames++
      }

      frameCount++
      lastFrameTime = frameTimeNanos
      Choreographer.getInstance().postFrameCallback(this)
    }
  }

  fun start() {
    running = true
    frameCount = 0
    droppedFrames = 0
    lastFrameTime = 0
    Choreographer.getInstance().postFrameCallback(callback)
  }

  fun stop(): Triple<Int, Int, Long> {
    running = false
    return Triple(frameCount, droppedFrames, System.currentTimeMillis())
  }
}