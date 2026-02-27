const path = require("path");

function detectRNVersion() {
  const rnPkg = require("react-native/package.json");
  return rnPkg.version;
}

module.exports = { detectRNVersion };