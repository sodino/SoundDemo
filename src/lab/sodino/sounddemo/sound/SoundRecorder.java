package lab.sodino.sounddemo.sound;

import java.io.IOException;

import android.media.MediaRecorder;

public class SoundRecorder {
	
	private MediaRecorder mediaRecorder;
	
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
	}
	
	public void startRecord(){
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecord(){
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;
	}
}
