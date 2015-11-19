package com.musicplayer;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

	String[] items;
	ListView lv;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lvPlaylist);
        
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        
        for (int i =0; i<mySongs.size(); i++){
        	toast(mySongs.get(i).getName().toString());
        	items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout,R.id.textView1, items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songList", mySongs));
			}});
    }
    
    public ArrayList<File> findSongs(File root){
		File[] files = root.listFiles();
		ArrayList<File> al = new ArrayList<File>();
		for(File singleFile: files){
			if (singleFile.isDirectory() && !singleFile.isHidden()){
				al.addAll(findSongs(singleFile));
			}
			else {
				if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
					al.add(singleFile);
				}
			}
		}		
		
    	return al;
    	
    }
    
    public void toast(String text){
    	Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
