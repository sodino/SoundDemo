package lab.sodino.sounddemo;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ClipDrawableActivity extends Activity implements OnSeekBarChangeListener {
	private SeekBar seekbar;
	private ImageView imgClip;
	private ClipDrawable clipDrawable;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip);
		
		initViews();
		
	}
	
	private void initViews(){
		seekbar = (SeekBar) findViewById(R.id.seekbarTop);
		seekbar.setOnSeekBarChangeListener(this);
		
		imgClip = (ImageView) findViewById(R.id.imgClip);
		clipDrawable = generateClipDrawable(Gravity.CENTER_HORIZONTAL,ClipDrawable.HORIZONTAL);
		imgClip.setImageDrawable(clipDrawable);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		// 已经对Seekbar设置最大值为10000,所以此处直接使用progress
		clipDrawable.setLevel(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	
	/**@param gravity 看api吧，不是每个gravity都可以用的。 
	 * */
	private ClipDrawable generateClipDrawable(int gravity,int orientation){
		BitmapDrawable bitDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.clip_demo);
		ClipDrawable clip = new ClipDrawable(bitDrawable, gravity, orientation);
		return clip;
	}
}
