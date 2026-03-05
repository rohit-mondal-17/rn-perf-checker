import { useEffect, RootTagContext, useContext } from 'react-native';
import RNPerfChecker from 'rn-perf-checker';
export default function App() {
  const rootTag = useContext(RootTagContext);
  useEffect(()=>{
    RNPerfChecker.startProfiling(rootTag)
    setTimeout(() => {
      RNPerfChecker.stopProfiling()
    }, 5000);
  },[])
  return <></>
}