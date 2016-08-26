package com.demo03.quangnguyen.record;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class list_record extends AppCompatActivity {
    private ListView lvRecord;
    private int check;

    private int resultCode = 1;

    private MediaPlayer mediaPlayer = null;
//    private String OUTPUT_FILE;
    ArrayList<String> list= null;
    ArrayAdapter<String> adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);


        Bundle db = getIntent().getExtras();
        if(db!=null){
            list =getIntent().getExtras().getStringArrayList("listname");
        }

        lvRecord=(ListView)findViewById(R.id.listView_record);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        lvRecord.setAdapter(adapter);
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                OUTPUT_FILE = Environment.getExternalStorageDirectory() + "sdcard/AudioRecord+" + list.get(i) + ".3gp";
//                try {
//                    playRecording(getFilename(i));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                playRecording(getFilename(i));
            }
        });
        lvRecord.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int a, long l) {
                check = a;
                AlertDialog.Builder alertDialongBuiter = new AlertDialog.Builder(list_record.this);
                alertDialongBuiter.setMessage("Do you want to delete");
                alertDialongBuiter.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        i = 0;
                        String test = list.get(i + check);
                        Toast.makeText(list_record.this, getFilename(check), Toast.LENGTH_LONG).show();
                        File f = new File(getFilename(test));
                        Boolean deleted = f.delete();
                        list.remove((i + check));
                        adapter.notifyDataSetChanged();

                    }
                });
                alertDialongBuiter.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertDialongBuiter.create();
                alertDialog.show();

//                Toast.makeText(list_record.this, "chek = "+check, Toast.LENGTH_LONG).show();
//                Toast.makeText(list_record.this, "aaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_LONG).show();
//                AlertDialog.Builder alertDialongBuiter = new AlertDialog.Builder(list_record.this);
//                alertDialongBuiter.setMessage("Are you want delete");
//
//                alertDialongBuiter.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        list.remove(i + check + 1);
//                        deleteFile(getFilename(i + check + 1));
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//                alertDialongBuiter.setNegativeButton("no", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                AlertDialog alertDialog = alertDialongBuiter.create();
//                alertDialog.show();
                return false;
            }
        });

    }
//    private void killMediaPlayer() {
//        if (mediaPlayer != null) {
//            try {
//                mediaPlayer.release();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    private void playRecording(String OUTPUT_FILE){
        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(OUTPUT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
    private String getFilename(int i) {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "sdcard/AudioRecorder");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + list.get(i) + ".3gp");
    }
    private String getFilename(String i) {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "sdcard/AudioRecorder");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" +i + ".3gp");
    }
}

