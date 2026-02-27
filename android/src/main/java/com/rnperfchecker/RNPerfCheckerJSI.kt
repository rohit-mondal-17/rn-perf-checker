package com.rnperfchecker

object RNPerfCheckerJSI {

  external fun getHermesHeap(): Double
  external fun getHostObjectCount(): Int
}