package com.rnperfchecker

import android.os.Handler
import android.os.Looper

object JSLongTaskMonitor {

  private var longTasks = 0
  private var running = false
  private val handler = Handler(Looper.getMainLooper())

  private val runnable = object : Runnable {
    override fun run() {
      val start = System.currentTimeMillis()
      handler.post {
        val delay = System.currentTimeMillis() - start
        if (delay > 32) longTasks++
      }
      if (running) handler.postDelayed(this, 16)
    }
  }

  fun start() {
    running = true
    longTasks = 0
    handler.post(runnable)
  }

  fun stop(): Int {
    running = false
    return longTasks
  }
}