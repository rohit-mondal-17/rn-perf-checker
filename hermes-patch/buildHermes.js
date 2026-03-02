const path = require("path");
const fs = require("fs");
const { run } = require("./utils");

async function buildHermes() {
  const reactAndroidPath = path.resolve(
    "node_modules/react-native/ReactAndroid"
  );

  console.log("Building patched Hermes...");

  run("./gradlew :hermes-engine:assembleRelease", reactAndroidPath);

  const builtAAR = path.resolve(
    reactAndroidPath,
    "hermes-engine/build/outputs/aar/hermes-engine-release.aar"
  );

  const targetDir = path.resolve(
    "node_modules/rn-perf-checker/android/libs"
  );

  fs.mkdirSync(targetDir, { recursive: true });

  fs.copyFileSync(
    builtAAR,
    path.join(targetDir, "hermes-engine.aar")
  );

  console.log("Patched Hermes AAR copied to rnperf.");
}

module.exports = { buildHermes };