cmake_minimum_required(VERSION 3.13)

add_library(
  rnperfchecker
  SHARED
  ../cpp/RNPerfCheckerJSI.cpp
)

target_link_libraries(
  rnperfchecker
  log
  ../cpp/RNPerfCheckerJSI.cpp
  ../cpp/HostObjectTracker.cpp
)