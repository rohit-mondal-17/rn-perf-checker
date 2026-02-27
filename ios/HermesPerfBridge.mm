#import <jsi/jsi.h>

using namespace facebook;

@interface HermesPerfBridge : NSObject
+ (NSDictionary *)getHermesStats;
@end

@implementation HermesPerfBridge

+ (NSDictionary *)getHermesStats {

  // If Hermes patched:
  // call into custom C function exposed by Hermes
  // Otherwise return empty

  return @{
    @"hostObjectLive": @(0),
    @"youngGCCount": @(0),
    @"oldGCCount": @(0)
  };
}

@end

extern "C"
NSDictionary * RNPerfGetHermesStats() {
  // return g_vmPerfStats values
}