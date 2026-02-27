const { execSync } = require("child_process");

function run(cmd, cwd) {
  execSync(cmd, {
    cwd,
    stdio: "inherit",
  });
}

module.exports = { run };