const path = require("path");
const fs = require("fs");
const { run } = require("./utils");

async function applyHermesPatch(rnVersion) {
  const hermesPath = path.resolve(
    "react-native/ReactAndroid/hermes-engine"
  );

  if (!fs.existsSync(hermesPath)) {
    throw new Error("Hermes source not found.");
  }

  const patchFile = resolvePatch(rnVersion);

  console.log("Using patch:", patchFile);

  try {
    // Check if patch already applied
    run(`git apply --check ${patchFile}`, hermesPath);
    run(`git apply ${patchFile}`, hermesPath);
    console.log("Hermes patch applied successfully.");
  } catch (e) {
    console.log("Patch may already be applied or conflicts exist.");
  }
}

function resolvePatch(rnVersion) {
  const baseDir = path.resolve(__dirname, "patches");

  if (rnVersion.startsWith("0.79")) {
    return path.join(baseDir, "0.79.x.patch");
  }

  return path.join(baseDir, "0.79.x.patch");
}

module.exports = { applyHermesPatch };