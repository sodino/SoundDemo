package lab.sodino.sounddemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button btnPressed2Talk = (Button) findViewById(R.id.btnPress2Talk);
		btnPressed2Talk.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(v.getId() == R.id.btnPress2Talk){
					int action = event.getAction();
					if(action == MotionEvent.ACTION_DOWN){
						btnPressed2Talk.setText(R.string.released2finish);
					}else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
						btnPressed2Talk.setText(R.string.pressed2talk);
					}
					// 为了 按钮 变色，弄返回false吧..
					return false;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
