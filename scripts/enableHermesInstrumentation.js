#!/usr/bin/env node

const { detectRNVersion } = require("../hermes-patch/detectRN");
const { applyHermesPatch } = require("../hermes-patch/applyPatch");
const { buildHermes } = require("../hermes-patch/buildHermes");
const fs = require("fs");
const path = require("path");

const args = process.argv.slice(2);

if (!args.includes("enable-hermes")) {
  console.log(`
Usage:

  npx rnperf enable-hermes --yes

This will:
- Patch Hermes
- Build Hermes
- Enable Gradle override
`);
  process.exit(0);
}

if (!args.includes("--yes")) {
  console.log(`
⚠️  This will modify node_modules and rebuild Hermes.
⚠️  This may take several minutes.

Re-run with --yes to continue.
`);
  process.exit(0);
}

(async function run() {
  try {
    const rnVersion = detectRNVersion();
    console.log("Detected RN:", rnVersion);

    await applyHermesPatch(rnVersion);
    await buildHermes();
    enableGradleFlag();

    console.log("Hermes instrumentation enabled.");
  } catch (e) {
    console.error("Failed:", e.message);
    process.exit(1);
  }
})();

function enableGradleFlag() {
  const gradleProps = path.resolve("android/gradle.properties");

  if (!fs.existsSync(gradleProps)) {
    console.log("gradle.properties not found.");
    return;
  }

  const content = fs.readFileSync(gradleProps, "utf8");

  if (!content.includes("RNPerfHermesEnabled")) {
    fs.appendFileSync(
      gradleProps,
      "\nRNPerfHermesEnabled=true\n"
    );
    console.log("Gradle flag enabled.");
  }
}