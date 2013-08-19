package lab.sodino.sounddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class TestImageView extends ImageView {

	public TestImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TestImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TestImageView(Context context) {
		super(context);
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Log.d("ANDROID_LAB", "ClipDrawableActivity TestImageView onDraw()");
	}
	
	public void onLayout (boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		Log.d("ANDROID_LAB", "ClipDrawableActivity TestImageView onLayout()");
	}
	
	public void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("ANDROID_LAB", "ClipDrawableActivity TestImageView onMeasure()");
	}
}
