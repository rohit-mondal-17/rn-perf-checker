import fs from 'fs';
import path from 'path';
import { execFileSync } from 'child_process';
export async function buildHermes() {
  // 1) Paths
  const projectAndroidDir = path.resolve('android'); // your app's android folder
  const rnRoot = path.resolve('node_modules/react-native');
  const reactAndroidPath = path.join(rnRoot, 'ReactAndroid');
  console.log('Building patched Hermes…');
  // 2) Pick the wrapper from YOUR project (not from ReactAndroid)
  const isWin = process.platform === 'win32';
  const wrapperBin = isWin ? 'gradlew.bat' : 'gradlew';
  const projectGradlew = path.join(projectAndroidDir, wrapperBin);
  if (!fs.existsSync(projectGradlew)) {
    throw new Error(
      `Could not find Gradle wrapper at ${projectGradlew}.\n` +
        `Did you run "gradlew wrapper" or create your Android project?`
    );
  }
  // 3) Ensure execute bit on POSIX (harmless if already set)
  if (!isWin) {
    try {
      fs.chmodSync(projectGradlew, 0o755);
    } catch {}
  }
  // 4) Build Hermes AAR inside ReactAndroid using your wrapper
  //    Equivalent shell: (cd android && ./gradlew -p ../node_modules/react-native/ReactAndroid :hermes-engine:assembleRelease)
  execFileSync(
    projectGradlew,
    ['-p', reactAndroidPath, ':hermes-engine:assembleRelease', '--no-daemon'],
    { stdio: 'inherit', cwd: projectAndroidDir, env: process.env }
  );
  // 5) Copy the built AAR into rn-perf-checker
  const builtAAR = path.join(
    reactAndroidPath,
    'hermes-engine/build/outputs/aar/hermes-engine-release.aar'
  );
  if (!fs.existsSync(builtAAR)) {
    throw new Error(
      `Hermes AAR not found at ${builtAAR}. Check Gradle output for errors.`
    );
  }
  const targetDir = path.resolve('node_modules/rn-perf-checker/android/libs');
  fs.mkdirSync(targetDir, { recursive: true });
  fs.copyFileSync(builtAAR, path.join(targetDir, 'hermes-engine.aar'));
  console.log('Patched Hermes AAR copied to rnperf.');
}