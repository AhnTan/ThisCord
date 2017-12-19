package com.example.thiscord;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by 안탄 on 2017-12-16.
 */

public class Recv_Voice extends Thread {
    private static final int AudioSampleRate = 44100;
    private static final int AudioChannel = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int AudioBit = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AudioMode = AudioTrack.MODE_STREAM;
    public AudioTrack track;
    public DatagramSocket socket = null;
    public DatagramPacket packet = null;
    byte[] data = new byte[4096];
    public Boolean rere = true;
    //private int portnumber = 9001;  // 나->소린
    private int portnumber = 9002;  // 소린->나


    public Recv_Voice(int user_port) {

        this.portnumber = user_port;
        Log.e("리시브 생성", "생성완료");
        try {
            socket = new DatagramSocket(portnumber);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        track = new AudioTrack(AudioManager.STREAM_MUSIC, AudioSampleRate, AudioChannel, AudioBit, track.getMinBufferSize(AudioSampleRate, AudioChannel, AudioBit), AudioTrack.MODE_STREAM);
        track.play();
        Log.e("오디오트랙받음", "ok");
    }

    @Override
    public void run() {
        super.run();
        try {
            while (rere) {
                packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                Log.e("데이터그램 받음", "ok");
                track.write(data, 0, data.length);
            }
            track.flush();
            track.play();
            //track.release();
            track = null;
        } catch (SocketException e) {
            e.printStackTrace();
            rere=false;
            track.stop();
            socket.disconnect();
            socket.close();
            Log.e("소켓닫힘", "ㄴㄴ");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            rere=false;
            track.stop();
            socket.disconnect();
            socket.close();
            Log.e("소켓닫힘", "ㄴㄴ");
            return;
        } catch (Exception e) {
            rere=false;
            track.stop();
            socket.disconnect();
            socket.close();
            Log.e("소켓닫힘", "ㄴㄴ");
            return;
        }
    }
}
