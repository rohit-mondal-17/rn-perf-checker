#include "HostObjectTracker.h"

std::atomic<int> HostObjectTracker::liveCount{0};