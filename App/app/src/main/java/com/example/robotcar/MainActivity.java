package com.example.robotcar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ImageButton len, xuong, trai, phai;
    FrameLayout mic;
    boolean state;

    private WebSocket webSocket;

    JSONObject json = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://192.168.1.14:3000")
                .build();


        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                Log.e("WebSocket", "Lỗi kết nối: ", t);
                if (response != null) {
                    Log.e("WebSocket", "Response: " + response.message());
                }
            }


            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("Websocket message", text);
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", 1);
                    webSocket.send(json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Websocket open", response.message());
            }
        });

        Controller controller = new Controller();
        View len = findViewById(R.id.len);
        View xuong = findViewById(R.id.xuong);
        View trai = findViewById(R.id.trai);
        View phai = findViewById(R.id.phai);
        FrameLayout mic = findViewById(R.id.mic);
        len.setOnTouchListener(controller.Foward(webSocket));
        xuong.setOnTouchListener(controller.Down(webSocket));
        phai.setOnTouchListener(controller.Right(webSocket));
        trai.setOnTouchListener(controller.Left(webSocket));
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Speechinput();
            }
        });

    }

    protected void Speechinput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó...");

        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Thiết bị không hỗ trợ nhận diện giọng nói", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("mic",result.get(0));
            String command = result.get(0);
            switch (command)
            {
                case "go":
                    sendCommand("f");
                    break;
                case "slow":
                    sendCommand("d");
                    break;
                case "left":
                    sendCommand("l");
                    break;
                case "right":
                    sendCommand("r");
                    break;
            }
        }
    }
    protected  void sendCommand(String command)
    {
        try {
            JSONObject json = new JSONObject();
            json.put("move", command);
            webSocket.send(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            JSONObject json = new JSONObject();
            json.put("id", 1);
            webSocket.close(1000, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}