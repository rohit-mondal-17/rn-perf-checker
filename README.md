# rn-perf-checker

Perf Scorer for your react native app

## Installation

```sh
npm install rn-perf-checker
```

## Hermes Deep Instrumentation (Optional)

By default rn-perf-checker works without modifying Hermes.

To enable deep VM instrumentation:

npx rnperf enable-hermes --yes

This will:
- Patch Hermes
- Build Hermes
- Enable Gradle override

To disable:

npx rnperf disable-hermes


## Usage


```js
import RNPerfChecker from 'rn-perf-checker';

// ...

RNPerfChecker.startProfiling()
RNPerfChecker.stopProfiling()
```


## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
