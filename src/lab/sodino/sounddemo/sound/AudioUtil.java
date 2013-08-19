package lab.sodino.sounddemo.sound;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

public class AudioUtil {

	/**@param bMute 值为true时为关闭背景音乐。*/
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static boolean muteAudioFocus(Context context, boolean bMute) {
		if(context == null){
			Log.d("ANDROID_LAB", "context is null.");
			return false;
		}
		if(!VersionUtils.isrFroyo()){
			// 2.1以下的版本不支持下面的API：requestAudioFocus和abandonAudioFocus
			Log.d("ANDROID_LAB", "Android 2.1 and below can not stop music");
			return false;
		}
		boolean bool = false;
		AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		if(bMute){
			int result = am.requestAudioFocus(null,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
			bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
		}else{
			int result = am.abandonAudioFocus(null);
			bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
		}
		Log.d("ANDROID_LAB", "pauseMusic bMute="+bMute +" result="+bool);
		return bool;
	}
}
