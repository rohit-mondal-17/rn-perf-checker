#include "PerfState.h"
#include <jsi/jsi.h>
#include <hermes/hermes.h>
#include "HostObjectTracker.h"

using namespace facebook;

PerfState g_perfState;

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_rnperfchecker_RNPerfCheckerJSI_getHermesHeap(JNIEnv*, jobject) {
  return (double)g_perfState.hermesHeap.load();
}

void updateHermesStats(jsi::Runtime& runtime) {
  auto hermes = dynamic_cast<hermes::HermesRuntime*>(&runtime);
  if (!hermes) return;

  auto heap = hermes->getHeapInfo();
  g_perfState.hermesHeap = heap.usedBytes;
}

class TrackedHostObject : public facebook::jsi::HostObject {
public:
  TrackedHostObject() {
    HostObjectTracker::liveCount++;
  }

  ~TrackedHostObject() override {
    HostObjectTracker::liveCount--;
  }
};

extern "C"
JNIEXPORT jint JNICALL
Java_com_rnperfchecker_RNPerfCheckerJSI_getHostObjectCount(JNIEnv*, jobject) {
  return HostObjectTracker::liveCount.load();
}
