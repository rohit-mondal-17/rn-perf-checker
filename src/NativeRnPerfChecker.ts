import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export type PerfReport = {
  score: number;
  js: {
    longTasks: number;
  };
  ui: {
    fps: number;
    droppedFrames: number;
  };
  memory: {
    nativeHeapMB: number;
    javaHeapMB: number;
    hermesHeapMB: number;
    hostObjectCount: number;
  };
};

export interface Spec extends TurboModule {
  startProfiling(): void;
  stopProfiling(): Promise<PerfReport>;
  getCurrentMetrics(): PerfReport;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'RNPerfChecker'
);