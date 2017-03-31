package com.auribises.speechrecognizer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecognitionListener {

    TextView txtOutput;
    Button btnSpeak;
    SpeechRecognizer speechRecognizer;

    TextToSpeech tts;

    ProgressDialog progressDialog;

    void initViews() {

        txtOutput = (TextView) findViewById(R.id.textView);
        btnSpeak = (Button) findViewById(R.id.button);
        btnSpeak.setOnClickListener(this);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Listening...");
        progressDialog.setCancelable(false);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS) {
                    Toast.makeText(MainActivity.this, "TTS is Initialized", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(this));
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        progressDialog.show();
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        progressDialog.dismiss();
    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> resultList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (resultList != null && resultList.size() > 0) {
            String output = resultList.get(0);
            txtOutput.setText(output);

            if (output.toLowerCase().contains("call") && output.toLowerCase().contains("sir")) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:+91 99155 71177"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Please Grant Permissions in Settings",Toast.LENGTH_LONG).show();
                }else {
                    startActivity(i);
                }
            }

            if(output.toLowerCase().contains("how") && output.toLowerCase().contains("you")){
                tts.speak("I am fine, Thank You very much", TextToSpeech.QUEUE_FLUSH,null);
            }

        }

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
