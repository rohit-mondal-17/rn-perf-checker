package com.rnperfchecker

import android.os.Debug

object MemoryMonitor {

  fun getNativeHeap(): Long {
    return Debug.getNativeHeapAllocatedSize()
  }

  fun getJavaHeap(): Long {
    val runtime = Runtime.getRuntime()
    return runtime.totalMemory() - runtime.freeMemory()
  }
}