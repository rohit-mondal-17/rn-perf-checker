const { detectRNVersion } = require("./detectRN");
const { applyHermesPatch } = require("./applyPatch");
const { buildHermes } = require("./buildHermes");

(async function run() {
  try {
    const rnVersion = detectRNVersion();
    console.log("Detected RN version:", rnVersion);

    await applyHermesPatch(rnVersion);
    await buildHermes();

    console.log("rnperf Hermes instrumentation ready.");
  } catch (e) {
    console.error("rnperf Hermes patch failed:", e.message);
    process.exit(1);
  }
})();