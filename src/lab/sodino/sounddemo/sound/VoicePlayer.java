package lab.sodino.sounddemo.sound;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.util.Log;

/**
 * 用于语音简介处本地amr播放。
 * @author sodinochen
*/
public class VoicePlayer implements OnCompletionListener,OnErrorListener{
	public final static int TYPE_RESOURCE_ID = 1;
	public final static int TYPE_SDCARD_AMR = 2;
	
	
	/**播放结束后仍需要显示满进度的时间。单位毫秒。*/
	public final static int END_BUFFER = 900;
	public final static int STATE_PREPARE = 1;
	public final static int STATE_PLAYING = 2;
	public final static int STATE_PAUSE = 3;
	public final static int STATE_COMPLETION = 4;
	public final static int STATE_STOP = 5;
	public final static int STATE_RELEASE = 6;
	public final static int STATE_END_BUFFER = 7;
	public final static int STATE_ERROR = 8;
	/**回调播放进度的时间间隔。*/
	public final static int TIME_UPDATE_PROGRESS = 50;
	/**getDuration()不准确，按最后的200ms点击暂停时，就当播放完了吧。*/
	public final static int RECORD_BUFFER_END_TIME = 200;
	/**本地音频路径。*/
	private String pttPath;
	private Handler handler;
	private MediaPlayer mPlayer;
	/**记录播放状态。*/
	private int state;
	private int type;
	private ArrayList<VoicePlayer.VoicePlayerListener> arrListener = new ArrayList<VoicePlayer.VoicePlayerListener>();
	/**要求最后的播放结束时间延迟。*/
	private boolean enableEndBuffer = false;
	/**声音焦点标识符。*/
	private boolean audioFocus = false;
	private Context mContext;
	private Runnable runnable = new Runnable(){
		int lastCurrent = 0;
		public void run(){
			if(state == STATE_COMPLETION || state == STATE_STOP || state == STATE_RELEASE || state == STATE_ERROR){
				lastCurrent = 0;
				return;
			}else if(state == STATE_PAUSE){
				lastCurrent = mPlayer.getCurrentPosition();
				return;
			}
			
			if(mPlayer == null){
				return;
			}
			
			int duration = mPlayer.getDuration();
			int current = mPlayer.getCurrentPosition();
			Log.d("ANDROID_LAB", "duration="+duration+" current="+current +" lastCurrent="+lastCurrent);
			if(current < lastCurrent){
				// 碰到有些变态机子，getCurrentPosition()都会往回倒...导致进度条缩了一下才又往前走!
				current = lastCurrent;
			}
			
			
			if(lastCurrent != 0 && lastCurrent == current && lastCurrent > duration - RECORD_BUFFER_END_TIME){
				// getDuration()不准呀，永远都播不到getDuration()的位置
				duration = current;
				Log.d("VoicePlayer","change duration from "+duration +" to "+current);
			} 
			if (current > lastCurrent) {
				lastCurrent = current;
			}
			
			for(VoicePlayerListener lis : arrListener){
				lis.playerProgress(pttPath, duration, current);
			}
			
			if(handler != null){
				handler.postDelayed(this, TIME_UPDATE_PROGRESS);
			}
		}
	};
	
	/**
	 * @param path 设置播放路径。
	 * @param progressHandler 用于处理进度条，当传参为null时，将无法回调播放进度。
	 * */
	public VoicePlayer(String path,Handler progressHandler){
		this.pttPath = path;
		this.handler = progressHandler;
		mPlayer = new MediaPlayer();
		state = STATE_PREPARE;
		type = TYPE_SDCARD_AMR;
	}
	
	public VoicePlayer(Context context,int resourceId){
		mPlayer = MediaPlayer.create(context, resourceId);
		state = STATE_PREPARE;
		type = TYPE_RESOURCE_ID;
	}
	
	/**设置是否控制声音焦点：<br/>
	 * 1.在播放时取消掉其它程序的背景音乐。<br/>
	 * 2.在播放结束后恢复其它程序的背景音乐。<br/>*/
	public boolean setAudioFocus(Context context){
		mContext = context;
		if(mContext != null){
			audioFocus = true;
		}
		return audioFocus;
	}
	
	public String getPttPath(){
		return pttPath;
	}
	
	
	public void addPlayerListener(VoicePlayerListener lis){
		if (arrListener.contains(lis) == false) {
			arrListener.add(lis);
		}
	}
	
	public interface VoicePlayerListener{
//		/**
//		 * @param isContinue 是否是暂停之后再点击的继续播放.播放或继续播放也是由用户发起的，这里不再重复通知了。*/
//		public void playerStart(String path,boolean isContinue);
		/**在Handler中被调用，属于UI线程可直接刷新界面。*/
		public void playerProgress(String path,int duration,int current);
		public void playerPause(String path,int duration,int current);
		/**播放完了之后，将会回收mPlayer.*/
		public void playerCompletion(int state, String path,int duration);
//		/**强制停止了，将会回收mPlayer.强制停止时也是由用户发起的，这里不再重复通知了。*/
//		public void playerStop(String path,int duration,int current);
	}


	/** 开始/继续播放。 */
	public void start() {
		if (state == STATE_PREPARE) {
			try {
				state = STATE_PLAYING;
				if(type == TYPE_SDCARD_AMR){
					mPlayer.setDataSource(pttPath);
					mPlayer.prepare();
				}
				mPlayer.setOnCompletionListener(this);
				mPlayer.setOnErrorListener(this);
				mPlayer.start();
//					for (VoicePlayerListener lis : arrListener) {
//						lis.playerStart(pttPath, false);
//					}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(audioFocus){
				AudioUtil.muteAudioFocus(mContext, true);
			}
			Log.d("VoicePlayer", "start to play...  for test time="+System.currentTimeMillis());
		} else if(state == STATE_PAUSE){
			state = STATE_PLAYING;
			mPlayer.start();
//			for (VoicePlayerListener lis : arrListener) {
//				lis.playerStart(pttPath, true);
//			}
			if(audioFocus){
				AudioUtil.muteAudioFocus(mContext, true);
			}
			Log.d("VoicePlayer", "continue to play... for test time="+System.currentTimeMillis());
		}
		if(handler != null){
			handler.post(runnable);
		}
	}
	
	public void pause(){
		if(state == STATE_END_BUFFER){
			// do nothing..等待buffer的时间过了..
		}else{
			if(audioFocus){
				AudioUtil.muteAudioFocus(mContext, false);
			}
			
			state = STATE_PAUSE;
			mPlayer.pause();
			for(VoicePlayerListener lis:arrListener){
				lis.playerPause(pttPath, mPlayer.getDuration(), mPlayer.getCurrentPosition());
			}
		}
	}
	
	public void release(){
		if(audioFocus){
			AudioUtil.muteAudioFocus(mContext, false);
		}
		
		state = STATE_RELEASE;
		if(mPlayer != null){
			mPlayer.stop();
			mPlayer.release();
		}
		mPlayer = null;
	}

	public int getState(){
		return state;
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// 返回false，继续回调onCompletion()，当做播放完成处理。
		Log.d("ANDROID_LAB", "onError what="+what+" extra="+extra);
		playEnd(true);
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d("VoicePlayer","onCompletion duration="+mPlayer.getDuration()+" current="+mPlayer.getCurrentPosition()+" enableEndBuffer="+enableEndBuffer+" thread="+Thread.currentThread().getName());
		if(enableEndBuffer){
			if(handler != null){
				state = STATE_END_BUFFER;
				handler.postDelayed(new Runnable(){
					public void run(){
						playEnd(false);
					}
				}, END_BUFFER);
			}else{
				playEnd(false);
			}
		}else{
			playEnd(false);
		}
	}
	
	private void playEnd(boolean endError){
		if(audioFocus){
			AudioUtil.muteAudioFocus(mContext, false);
		}
		
		int duration = 0;
		if (endError) {
			state = STATE_ERROR;
		} else {
			state = STATE_COMPLETION;
		}
		if (mPlayer != null) {
			if(state == STATE_COMPLETION){
				// 在播放完后多显示进度条的1s内，如果关闭了对话框会直接将mPlayer回收，此时mPlayer为null
				duration = mPlayer.getDuration();
			}
			mPlayer.release();
		}
		mPlayer = null;
		for(VoicePlayerListener lis:arrListener){
			lis.playerCompletion(state, pttPath,duration);
		}
	}
	
	/**播放结束后再延迟一段时间才发出播放完成通知。*/
	public boolean setBufferEnd(){
		if(handler == null){
			enableEndBuffer = false;
			// 需要handler不为空
			return false;
		}
		enableEndBuffer = true;
		return true;
	}
	
	public int getType(){
		return type;
	}
}