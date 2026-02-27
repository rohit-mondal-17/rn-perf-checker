#import <Foundation/Foundation.h>

static int longTasks = 0;
static CFRunLoopObserverRef observer;

void runLoopCallback(
  CFRunLoopObserverRef observer,
  CFRunLoopActivity activity,
  void *info
) {
  static CFAbsoluteTime lastTime = 0;
  CFAbsoluteTime now = CFAbsoluteTimeGetCurrent();

  if (lastTime != 0 && (now - lastTime) > 0.032) {
    longTasks++;
  }

  lastTime = now;
}

@interface JSLongTaskMonitor : NSObject
+ (void)start;
+ (int)stop;
@end

@implementation JSLongTaskMonitor

+ (void)start {
  longTasks = 0;

  observer = CFRunLoopObserverCreate(
    NULL,
    kCFRunLoopAllActivities,
    YES,
    0,
    runLoopCallback,
    NULL
  );

  CFRunLoopAddObserver(
    CFRunLoopGetMain(),
    observer,
    kCFRunLoopCommonModes
  );
}

+ (int)stop {
  if (observer) {
    CFRunLoopRemoveObserver(
      CFRunLoopGetMain(),
      observer,
      kCFRunLoopCommonModes
    );
    CFRelease(observer);
    observer = NULL;
  }
  return longTasks;
}

@end