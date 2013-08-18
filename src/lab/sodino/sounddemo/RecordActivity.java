package lab.sodino.sounddemo;

import java.io.File;

import lab.sodino.sounddemo.sound.SoundRecorder;
import lab.sodino.sounddemo.sound.SoundRecorder.SoundRecordListener;
import lab.sodino.sounddemo.util.AudioUtil;
import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class RecordActivity extends Activity implements SoundRecordListener, Callback{
	
	public static final float TOP_LEVEL = (219-28) * 10000.0f/219;
	public static final float BOTTOM_LEVEL = (219-120) * 10000.0f/219;
	public static final int MSG_RECORD = 1;
	private SoundRecorder soundRecorder;
	private ImageView imgMicroPhone;
	private ClipDrawable clipDrawable;
	private Handler handler;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		final Button btnPressed2Talk = (Button) findViewById(R.id.btnPress2Talk);
		btnPressed2Talk.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(v.getId() == R.id.btnPress2Talk){
					int action = event.getAction();
					if(action == MotionEvent.ACTION_DOWN){
						// 如果有背景音乐，暂停
						AudioUtil.muteAudioFocus(RecordActivity.this, true);
						btnPressed2Talk.setText(R.string.released2finish);
						String path = Environment.getExternalStorageDirectory() +File.separator + "test.amr";
						soundRecorder = new SoundRecorder(path);
						soundRecorder.addSoundRecordListener(RecordActivity.this);
						soundRecorder.startRecord();
					}else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
						// 如果有背景音乐，恢复
						AudioUtil.muteAudioFocus(RecordActivity.this, false);
						btnPressed2Talk.setText(R.string.press2talk);
						soundRecorder.stopRecord();
					}
					return false;
				}
				return false;
			}
		});
		
		imgMicroPhone = (ImageView) findViewById(R.id.imgMicroPhone);
		clipDrawable = (ClipDrawable) imgMicroPhone.getDrawable();
		
		handler = new Handler(this);
	}

	@Override
	public void recordStart() {
		
	}

	@Override
	public void recordProgress(int maxAmplitude) {
		int level = 0;
		int topAmp = 10000;
		if(maxAmplitude <= 0){
			// 
			level = 0;
		}else if(maxAmplitude < topAmp){
			// 
			level = (int) (((TOP_LEVEL - BOTTOM_LEVEL) * maxAmplitude / topAmp) + BOTTOM_LEVEL);
		}else{
			level = 10000;
		}
		
		Log.d("ANDROID_LAB", "maxAmp="+maxAmplitude+" level="+level);
		
		Message msg = handler.obtainMessage();
		msg.what = MSG_RECORD;
		msg.arg1 = level;
		handler.sendMessage(msg);
	}

	@Override
	public void recordComplete(int state) {
		Log.d("ANDROID_LAB", "recordComplete state="+state);
		Message msg = handler.obtainMessage();
		msg.what = MSG_RECORD;
		msg.arg1 = 0;
		handler.sendMessage(msg);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what){
		case MSG_RECORD:
//			Log.d("ANDROID_LAB", "h=" + clipDrawable.getIntrinsicHeight() +" w=" + clipDrawable.getIntrinsicWidth());
			clipDrawable.setLevel(msg.arg1);	
			break;
		}
		return false;
	}


	public void onStop(){
		super.onStop();
		if(soundRecorder != null){
			soundRecorder.stopRecord();
			soundRecorder = null;
		}
	}
	public void onDestory(){
		super.onDestroy();
		if(soundRecorder != null){
			soundRecorder.stopRecord();
		}
	}
}