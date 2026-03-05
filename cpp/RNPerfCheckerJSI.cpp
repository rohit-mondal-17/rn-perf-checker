#include <jni.h>
#include <jsi/jsi.h>
// #include <hermes/hermes.h>
#include "PerfState.h"
#include "HostObjectTracker.h"
using namespace facebook;

// Global perf state
PerfState g_perfState;

// Package: com.rnperfchecker
// Class  : RNPerfCheckerJSI
// Method : getHermesHeap() : double
extern "C"
JNIEXPORT jdouble JNICALL
Java_com_rnperfchecker_RNPerfCheckerJSI_getHermesHeap(JNIEnv* /*env*/, jobject /*thiz*/) {
    // g_perfState.hermesHeap should be an atomic or a double you control
    // Cast to jdouble is explicit and correct for JNI
    return static_cast<jdouble>(g_perfState.hermesHeap.load());
}