package com.demo03.quangnguyen.record;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Record extends Activity {
    ArrayList<String> listName = new ArrayList<String>();
    private static final String AUDIO_RECORDER_FOLDER = "sdcard/AudioRecorder";
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";

    private String check;

    private TextView txtvTime;
    private Button btnRecord,btnList;
    private String Out_File;
    private Chronometer chronometer;

    private MediaRecorder mediaRecorder;

    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);
        txtvTime = (TextView)findViewById(R.id.textViewtime);
        btnRecord = (Button)findViewById(R.id.buttonrecord);
        btnList = (Button)findViewById(R.id.buttonlist);
//        editname = (EditText)findViewById(R.id.editTextname);
//        Out_File = Environment.getDataDirectory()+"/DCIM+"+System.currentTimeMillis()+".3gp";
        chronometer = (Chronometer)findViewById(R.id.chronometer1);
        i =0;

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showListRecord = new Intent(Record.this, list_record.class);
                showListRecord.putStringArrayListExtra("listname", listName);
                startActivity(showListRecord);
                startActivityForResult(showListRecord, 1);
            }
        });
//        Check Phay và Pause
//        use i specify button Pause or Play
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    try {
                        startRecording();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i = 1;
                } else if (i == 1) {
                    try {
                        open(view);
                        stopRecording();
                        chronometer.stop();
                        txtvTime.setText(chronometer.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i = 0;
                }
            }
        });
    }
// Messges
    public void open(View view)
    {
        AlertDialog.Builder alertDialongBuiter = new AlertDialog.Builder(this);
        alertDialongBuiter.setMessage("Do you want to save");
        alertDialongBuiter.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Record.this, "Done", Toast.LENGTH_LONG).show();
            }
        });
        alertDialongBuiter.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File f = new File(check);// delete file
                Boolean deleted = f.delete();
                listName.remove(listName.size()-1);//Remove name in listname

            }
        });
        AlertDialog alertDialog = alertDialongBuiter.create();
        alertDialog.show();
    }
    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        check = getFilename();
        mediaRecorder.setOutputFile(check);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    private void stopRecording() {
        if (null != mediaRecorder) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    //    private void beginRecord()throws Exception{
//        killMediaRecord();
//        File outfile = new File(getFilename());
//        if(outfile.exists())
//        {
//            outfile.delete();
//        }
//
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mediaRecorder.setOutputFile(getFilename());
//        mediaRecorder.start();
//         mediaRecorder.setOnErrorListener(errorListener);
//        mediaRecorder.setOnInfoListener(infoListener);
//        try {
//            mediaRecorder.prepare();
//            mediaRecorder.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void killMediaRecord(){
//        if(mediaRecorder!=null)
//        {
//            mediaRecorder.release();
//        }
//    }
//    private void stopRecord() throws Exception{
//        if (null != mediaRecorder) {
//            mediaRecorder.stop();
//            mediaRecorder.reset();
//            mediaRecorder.release();
//            mediaRecorder = null;
//        }
//    }
    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        String temp = System.currentTimeMillis()+"";
        listName.add(temp);
        return (file.getAbsolutePath() + "/" + temp + AUDIO_RECORDER_FILE_EXT_3GP);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                listName = data.getStringArrayListExtra("return");
            }
        }
    }
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(Record.this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(Record.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

}