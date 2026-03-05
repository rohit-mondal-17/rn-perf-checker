project(rnperfchecker)
cmake_minimum_required(VERSION 3.13.0)

set(PACKAGE_NAME "rnperfchecker")
set(BUILD_DIR ${CMAKE_SOURCE_DIR}/build)
set(CMAKE_VERBOSE_MAKEFILE ON)
set(CMAKE_CXX_STANDARD 17)

find_package(fbjni REQUIRED CONFIG)
find_package(ReactAndroid REQUIRED CONFIG)

add_library(
  ${PACKAGE_NAME}
  SHARED
  ../cpp/RNPerfCheckerJSI.cpp
  ../cpp/HostObjectTracker.cpp
)

target_link_libraries(
  ${PACKAGE_NAME}
  android
  fbjni::fbjni
  ReactAndroid::jsi
  ReactAndroid::reactnative
)