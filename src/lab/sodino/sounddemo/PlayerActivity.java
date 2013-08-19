package lab.sodino.sounddemo;

import java.io.File;

import lab.sodino.sounddemo.sound.FormatNumber;
import lab.sodino.sounddemo.sound.VoicePlayer;
import lab.sodino.sounddemo.sound.VoicePlayer.VoicePlayerListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayerActivity extends Activity implements OnClickListener, Callback, VoicePlayerListener {
	String path = Environment.getExternalStorageDirectory() +File.separator + "test.amr";
	Button btnPlayer;
	TextView txtProgress;
	VoicePlayer voicePlayer;
	Handler handler;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		handler = new Handler(this);
		btnPlayer = (Button)findViewById(R.id.click2play);
		btnPlayer.setOnClickListener(this);
		txtProgress = (TextView)findViewById(R.id.txtProgress);
		
		if(new File(path).exists() == false){
			btnPlayer.setEnabled(false);
			txtProgress.setText(R.string.playerHint);
		}
	}
	@Override
	public void onClick(View v) {
		if(v == btnPlayer){
			if(voicePlayer == null){
				voicePlayer = new VoicePlayer(path, handler);
				voicePlayer.addPlayerListener(this);
				voicePlayer.start();
				btnPlayer.setText(R.string.click2pause);
			}else if(voicePlayer.getState() == VoicePlayer.STATE_PAUSE){
				voicePlayer.start();
				btnPlayer.setText(R.string.click2pause);
			}else if(voicePlayer.getState() == VoicePlayer.STATE_PLAYING){
				voicePlayer.pause();
				btnPlayer.setText(R.string.click2play);
			}
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
	@Override
	public void playerProgress(String path, int duration, int current) {
		txtProgress.setText(FormatNumber.formatProgresss(duration, current));
	}
	@Override
	public void playerPause(String path, int duration, int current) {
		
	}
	@Override
	public void playerCompletion(int state, String path, int duration) {
		if(state == VoicePlayer.STATE_ERROR){
			// do something...
		}else if(state == VoicePlayer.STATE_COMPLETION){
			
		}
		btnPlayer.setText(R.string.click2play);
		if(voicePlayer != null){
			voicePlayer.release();
			voicePlayer = null;
		}
	}
	
	public void onResume(){
		super.onResume();
		Log.d("ANDROID_LAB", "PlayerActivity onResume time=" + System.currentTimeMillis());
	}
	
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		Log.d("ANDROID_LAB", "PlayerActivity onWindowFocusChanged hasFocus=" + hasFocus + " btn.width=" + btnPlayer.getWidth() +" time=" + System.currentTimeMillis());
	}
}
