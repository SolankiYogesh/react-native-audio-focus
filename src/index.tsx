import { NativeModules, DeviceEventEmitter } from 'react-native';

const { AudioFocus } = NativeModules;
type AudioFocusEventType = 'onAudioFocusChange' | 'onIncomingCall';

export const addListener = (
  event: AudioFocusEventType,
  listener: (data: number) => void
) => {
  AudioFocus.addListener();
  DeviceEventEmitter.addListener(event, (data: number) => listener(data));
};

export const removeListener = () => {
  AudioFocus.removeListener();
  DeviceEventEmitter.removeAllListeners();
};

const AudioFocusModule = {
  addListener,
  removeListener,
};
export default AudioFocusModule;
