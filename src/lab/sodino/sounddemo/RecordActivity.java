package lab.sodino.sounddemo;

import java.io.File;

import lab.sodino.sounddemo.sound.SoundRecorder;
import lab.sodino.sounddemo.sound.SoundRecorder.SoundRecordListener;
import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.media.MediaRecorder;
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
						btnPressed2Talk.setText(R.string.released2finish);
						String path = Environment.getExternalStorageDirectory() +File.separator + "test.amr";
						soundRecorder = new SoundRecorder(path);
						soundRecorder.addSoundRecordListener(RecordActivity.this);
						soundRecorder.startRecord();
					}else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
						btnPressed2Talk.setText(R.string.pressed2talk);
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
		Log.d("ANDROID_LAB", "maxAmp="+maxAmplitude);
		Message msg = handler.obtainMessage();
		msg.what = MSG_RECORD;
		msg.arg1 = maxAmplitude;
		handler.sendMessage(msg);
	}

	@Override
	public void recordComplete(int state) {
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what){
		case MSG_RECORD:
			clipDrawable.setLevel(msg.arg1);	
			break;
		}
		return false;
	}
}