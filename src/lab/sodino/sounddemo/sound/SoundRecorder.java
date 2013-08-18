package lab.sodino.sounddemo.sound;

import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaRecorder;
import android.util.Log;

public class SoundRecorder {
	public static final int STATE_INIT = 0;
	public static final int STATE_RECORDING = 1;
	public static final int STATE_COMPLETE = 2;
	public static final int STATE_ERROR = 3;
	
	public static final int  SLEEP_TIME = 10;
	private MediaRecorder mediaRecorder;
	private ArrayList<SoundRecordListener> arrListener = new ArrayList<SoundRecorder.SoundRecordListener>();
	private boolean recording = true;
	private RecordThread recordThread;
	private int state;
	public SoundRecorder(String path){
		mediaRecorder = new MediaRecorder();
		// 设置声音来源为麦克风
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 按文档来，音频录制为amr编码时3gp输出格式
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置编码格式
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// 设置存放路径
		mediaRecorder.setOutputFile(path);
		
		state = STATE_INIT;
	}
	
	public void startRecord(){
		if(recordThread==null){
			recordThread = new RecordThread();
			recordThread.start();
		}
	}
	
	public void stopRecord(){
		recording = false;
	}

	public void addSoundRecordListener(SoundRecordListener lis){
		if(lis != null && arrListener.contains(lis) == false){
			arrListener.add(lis);
		}
	}
	
	class RecordThread extends Thread{
		RecordThread(){
			setName("RecordThread");
		}
		public void run(){
			try {
				mediaRecorder.prepare();
				mediaRecorder.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			state = STATE_RECORDING;
			while (recording) {
				int maxAmplitude = mediaRecorder.getMaxAmplitude();
				for(SoundRecordListener lis:arrListener){
					lis.recordProgress(maxAmplitude);
				}
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try{
				mediaRecorder.stop();
				mediaRecorder.release();
				mediaRecorder = null;
			}catch(RuntimeException re){
				re.printStackTrace();
			}
			for(SoundRecordListener lis:arrListener){
				lis.recordComplete(state);
			}
			state = STATE_COMPLETE;
		}
	}
	
	public static interface SoundRecordListener{
		public void recordStart();
		public void recordProgress(int maxAmplitude);
		public void recordComplete(int state);
	}
}
