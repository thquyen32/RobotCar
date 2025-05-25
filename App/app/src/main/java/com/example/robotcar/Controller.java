package com.example.robotcar;

import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Runnable;
import android.os.Handler;

import okhttp3.WebSocket;

public class Controller {
    private boolean isSending = false;
    private Runnable repeatSender;
    private Handler handler;

    public Controller() {
        this.handler = new Handler();
    }

    public View.OnTouchListener Foward(WebSocket webSocket) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isSending) {
                            isSending = true;
                            repeatSender = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject();
                                        json.put("move", "f");
                                        webSocket.send(json.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    handler.postDelayed(this, 100); //setup thoi gian lap
                                }
                            };
                            handler.post(repeatSender); //Khoi tao lap
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(repeatSender);
                        isSending = false;
                        return true;
                }
                return false;
            }
        };

    }

    public View.OnTouchListener Down(WebSocket webSocket) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isSending) {
                            isSending = true;
                            repeatSender = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject();
                                        json.put("move", "d");
                                        webSocket.send(json.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    handler.postDelayed(this, 100); //setup thoi gian lap
                                }
                            };
                            handler.post(repeatSender); //Khoi tao lap
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(repeatSender);
                        isSending = false;
                        return true;
                }
                return false;
            }
        };

    }

    public View.OnTouchListener Right(WebSocket webSocket) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isSending) {
                            isSending = true;
                            repeatSender = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject();
                                        json.put("move", "r");
                                        webSocket.send(json.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    handler.postDelayed(this, 100); //setup thoi gian lap
                                }
                            };
                            handler.post(repeatSender); //Khoi tao lap
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(repeatSender);
                        isSending = false;
                        return true;
                }
                return false;
            }
        };

    }

    public View.OnTouchListener Left(WebSocket webSocket) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isSending) {
                            isSending = true;
                            repeatSender = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject();
                                        json.put("move", "l");
                                        webSocket.send(json.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    handler.postDelayed(this, 100); //setup thoi gian lap
                                }
                            };
                            handler.post(repeatSender); //Khoi tao lap
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(repeatSender);
                        isSending = false;
                        return true;
                }
                return false;
            }
        };
    }

}
