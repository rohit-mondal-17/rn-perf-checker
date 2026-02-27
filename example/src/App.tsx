import { useEffect } from 'react-native';
import RNPerfChecker from 'rn-perf-checker';


export default function App() {
  useEffect(()=>{
    RNPerfChecker.startProfiling()

    setTimeout(() => {
      RNPerfChecker.stopProfiling()
    }, 5000);
  },[])
  
  return <></>
}