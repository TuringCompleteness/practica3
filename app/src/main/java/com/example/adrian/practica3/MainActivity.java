package com.example.adrian.practica3;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ListView songsList;
    private ImageView album;
    private ImageButton playButton;
    private int actualSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsList = findViewById(R.id.songsList);
        album = findViewById(R.id.album);
        playButton = findViewById(R.id.playButton);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("song", actualSong);
        outState.putInt("position", mediaPlayer.getCurrentPosition());
        mediaPlayer.pause();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        int song = savedInstanceState.getInt("song");
        int position = savedInstanceState.getInt("position");
        mediaPlayer.reset();
        setTrack(song);
        mediaPlayer.seekTo(position);
        mediaPlayer.start();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<String> songs = new ArrayList<>();
        songs.add("Summer Waves");
        songs.add("Swimmingly");
        songs.add("The Fairies");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songs);
        songsList.setAdapter(arrayAdapter);

        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (playButton.getVisibility() == View.INVISIBLE) {
                    playButton.setVisibility(View.VISIBLE);
                }
                mediaPlayer.stop();
                mediaPlayer.reset();
                setTrack(position);
                mediaPlayer.start();
                actualSong = position;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mediaPlayer.reset();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getVisibility() == View.VISIBLE && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.mipmap.play);
                }
                else if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    playButton.setImageResource(R.mipmap.pause);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void setTrack(int position) {
        AssetFileDescriptor afd;
        try {
        switch (position) {
            case 0:
                album.setImageResource(R.mipmap.album1);
                afd = getAssets().openFd("summerwaves.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                break;
            case 1:
                album.setImageResource(R.mipmap.album2);
                afd = getAssets().openFd("fairies.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                break;
            case 2:
                album.setImageResource(R.mipmap.album3);
                afd = getAssets().openFd("swimmingly.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                break;
            }
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
