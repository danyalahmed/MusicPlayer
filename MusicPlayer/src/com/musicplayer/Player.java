package com.musicplayer;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class Player extends Activity implements android.view.View.OnClickListener {

	int position;
	Uri u;
	Thread updateSeekbar	;
	
	static MediaPlayer mp;
	ArrayList<File> mySongs;
	SeekBar sb;
	Button btPlay, btNext, btFastForward, btFastBackword, btLast;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		btPlay = (Button) findViewById(R.id.btPlay);
		btFastBackword = (Button) findViewById(R.id.btFastBackword);
		btFastForward = (Button) findViewById(R.id.btFastForward);
		btNext = (Button) findViewById(R.id.btNext);
		btLast = (Button) findViewById(R.id.btLast);
		
		btPlay.setOnClickListener((android.view.View.OnClickListener) this);
		btFastBackword.setOnClickListener((android.view.View.OnClickListener) this);
		btFastForward.setOnClickListener((android.view.View.OnClickListener) this);
		btNext.setOnClickListener((android.view.View.OnClickListener) this);
		btLast.setOnClickListener((android.view.View.OnClickListener) this);
		
		sb = (SeekBar) findViewById(R.id.seekBar1);
		updateSeekbar = new Thread(){
			@Override
			public void run(){
				int totalDuration = mp.getDuration();
				int currentPosition = 0;
				sb.setMax(totalDuration);
				while (currentPosition < totalDuration){
					try{
						sleep(500);
						currentPosition = mp.getCurrentPosition();
						sb.setProgress(currentPosition);
						
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};
		
		if (mp!=null){
			mp.stop();
			mp.release();
		}
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		ArrayList<File> mySongs = (ArrayList)b.getParcelableArrayList("songList");
		position = b.getInt("pos", 0);
		
		//universal resource indicator
		u = Uri.parse(mySongs.get(position).toString());
		mp = MediaPlayer.create(getApplicationContext(), u);
		mp.start();
		
		updateSeekbar.start();
		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mp.seekTo(seekBar.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v){
		int id = v.getId();
		switch (id){
		case R.id.btPlay:
			if (mp.isPlaying()){
				mp.pause();
				btPlay.setText(">");
				mp.pause();
			}
			else{
				btPlay.setText("||");
				mp.start();
				
			}
			break;
		case R.id.btFastBackword:
			mp.seekTo(mp.getCurrentPosition()-5000);
			break;
		case R.id.btFastForward:
			mp.seekTo(mp.getCurrentPosition()+5000);
		case R.id.btNext:
			mp.stop();
			mp.release();
			position = (position+1)%mySongs.size();
			u = Uri.parse(mySongs.get(position).toString());
			mp = MediaPlayer.create(getApplicationContext(), u);
			mp.start();
			break;
		case R.id.btLast:
			mp.stop();
			mp.release();
			position = (position-1 < 0)? mySongs.size()-1: position - 1;
			/*
			
			if (position-1 < 0)
				position = mySongs.size() -1;
			else position = position-1;
			
			*/
			u = Uri.parse(mySongs.get(position).toString());
			mp = MediaPlayer.create(getApplicationContext(), u);
			mp.start();
			break;
		}
	}
}
