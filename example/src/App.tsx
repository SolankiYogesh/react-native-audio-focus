import * as React from 'react';

import { StyleSheet, SafeAreaView, Text, Dimensions } from 'react-native';
import AudioFocusModule from 'index';
import Video from 'react-native-video';

export default function App() {
  const [isPlaying, setISPlaying] = React.useState(true);

  React.useEffect(() => {
    AudioFocusModule.addListener('onAudioFocusChange', (e) => {
    
      setISPlaying(e === 1);
    });
    return () => {
      AudioFocusModule.removeListener();
    };
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <Video
        source={{
          uri: 'https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
        }}
        resizeMode="cover"
        controls
        paused={!isPlaying}
        style={styles.videoStyle}
      />
      <Text style={styles.textStyle}>{isPlaying ? 'Playing' : 'Paused'}</Text>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  videoStyle: {
    height: Dimensions.get('window').height * 0.8,
  },

  textStyle: {
    textAlign: 'center',
    fontSize: 15,
    fontWeight: '500',
    color: 'white',
    flex: 1,
    textAlignVertical: 'center',
  },
});
