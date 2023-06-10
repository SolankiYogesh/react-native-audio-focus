package com.audiofocus;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AudioFocusModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
  private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
  private final ReactApplicationContext reactContext;
  private PhoneStateListener phoneStateListener;
  private final TelephonyManager telephonyManager;
  private IncomingCallReceiver incomingCallReceiver;

  private boolean isListeningForAudioFocus = false;

  public AudioFocusModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.telephonyManager = (TelephonyManager) reactContext.getSystemService(Context.TELEPHONY_SERVICE);
    this.audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
      @Override
      public void onAudioFocusChange(int focusChange) {
        // Emit the audio focus change event to React Native
        getReactApplicationContext()
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit("onAudioFocusChange", focusChange);
      }
    };
    this.incomingCallReceiver = new IncomingCallReceiver(reactContext);
  }

  @Override
  public String getName() {
    return "AudioFocus";
  }

  @ReactMethod
  public void addListener() {
    if (!isListeningForAudioFocus) {
      startListeningForAudioFocus();
      isListeningForAudioFocus = true;
    }
  }

  @ReactMethod
  public void removeListener() {
    if (isListeningForAudioFocus) {
      stopListeningForAudioFocus();
      isListeningForAudioFocus = false;
    }
  }

  @ReactMethod
  public void startListeningForAudioFocus() {
    AudioManager audioManager = (AudioManager) getReactApplicationContext()
      .getSystemService(Context.AUDIO_SERVICE);
    audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    registerPhoneStateListener();
    reactContext.addLifecycleEventListener(this);
  }

  @ReactMethod
  public void stopListeningForAudioFocus() {
    AudioManager audioManager = (AudioManager) getReactApplicationContext()
      .getSystemService(Context.AUDIO_SERVICE);
    audioManager.abandonAudioFocus(audioFocusChangeListener);
    unregisterPhoneStateListener();
    reactContext.removeLifecycleEventListener(this);
  }

  @Override
  public void onHostResume() {
    // No need to do anything here since startListeningForAudioFocus is already called in addListener
  }

  @Override
  public void onHostPause() {
    // No need to do anything here since stopListeningForAudioFocus is already called in removeListener
  }

  @Override
  public void onHostDestroy() {
    removeListener();
  }

  private void registerPhoneStateListener() {
    phoneStateListener = new PhoneStateListener() {
      @Override
      public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        if (state == TelephonyManager.CALL_STATE_RINGING) {
          // Emit the incoming call event to React Native
          reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onIncomingCall", null);
        }
      }
    };
    if (Build.VERSION.SDK_INT >= 31) {
      if (ContextCompat.checkSelfPermission(reactContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    } else {
      telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
  }

  private void unregisterPhoneStateListener() {
    telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
  }

  private class IncomingCallReceiver extends BroadcastReceiver {
    private final ReactApplicationContext reactContext;

    public IncomingCallReceiver(ReactApplicationContext reactContext) {
      this.reactContext = reactContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();

      if (action != null && action.equals("android.intent.action.PHONE_STATE")) {
        String state = intent.getStringExtra("state");

        if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
          // Emit the incoming call event to React Native
          reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onIncomingCall", null);
        }
      }
    }
  }
}
