package lab.sodino.sounddemo;

import lab.sodino.sounddemo.util.AudioUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioFocusActivity extends Activity implements OnClickListener {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_focus);
		Button btnRequest = (Button) findViewById(R.id.btnAudioFocusRequest);
		btnRequest.setOnClickListener(this);
		Button btnAbandon = (Button) findViewById(R.id.btnAudioFocusAbandon);
		btnAbandon.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnAudioFocusAbandon:
			AudioUtil.muteAudioFocus(this, false);
			break;
		case R.id.btnAudioFocusRequest:
			AudioUtil.muteAudioFocus(this, true);
			break;
		}
	}
}
