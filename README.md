## AudioFocus

This package provides functionality to handle audio focus changes and incoming calls in a React Native application on Android.

### Installation

To install this package, follow these steps:

1. Install the package using npm or Yarn:

   ```bash
   npm install react-native-audio-focus
   ```

   or

   ```bash
   yarn add react-native-audio-focus
   ```

2. Link the native module:

   ```bash
   react-native link react-native-audio-focus
   ```

### Usage

To use the `AudioFocus` module in your React Native code, follow these steps:

1. Import the module:

   ```javascript
   import AudioFocus from 'react-native-audio-focus';
   ```

2. Add listeners to handle audio focus changes and incoming calls:

   ```javascript
   // Add listener for audio focus changes
   AudioFocus.addListener('onAudioFocusChange', (focusChange) => {
     // Handle audio focus change event
   });

   // Add listener for incoming calls
   AudioFocus.addListener('onIncomingCall', () => {
     // Handle incoming call event
   });
   ```

3. Remove listeners when they are no longer needed:

   ```javascript
   AudioFocus.removeListener();
   ```

### Permissions

Make sure to add the necessary permissions to your `AndroidManifest.xml` file to handle incoming calls:

```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

### Troubleshooting

If you encounter any issues or errors while using this package, please refer to the [GitHub repository](https://github.com/SolankiYogesh/react-native-audio-focus) for further information and troubleshooting steps.

### License

This package is released under the [MIT License](https://opensource.org/licenses/MIT).

### Contributing

Contributions to this package are welcome! If you find any bugs or want to suggest new features, please open an issue or submit a pull request on the [GitHub repository]((https://github.com/SolankiYogesh/react-native-audio-focus/issues)).

### Acknowledgments

This package is inspired by the [React Native Audio Focus]((https://github.com/SolankiYogesh/react-native-audio-focus)) package.

### Contact

For any inquiries or questions, please contact [solankiyogesh3500@gmail.com](mailto:solankiyogesh3500@gmail.com).
