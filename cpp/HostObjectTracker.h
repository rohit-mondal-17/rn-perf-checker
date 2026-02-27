#pragma once
#include <atomic>

struct HostObjectTracker {
  static std::atomic<int> liveCount;
};