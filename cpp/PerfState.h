#pragma once
#include <atomic>

struct PerfState {
  std::atomic<size_t> hermesHeap{0};
};

extern PerfState g_perfState;