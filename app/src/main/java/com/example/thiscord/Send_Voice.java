package com.example.thiscord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by 안탄 on 2017-12-16.
 */

public class Send_Voice extends Thread {
    //private String ip = Contact.ip_address;
    private String ip = "223.194.159.146";  // 또링
    //private String ip = "223.194.155.82"; // 나
    //private String ip = "223.194.155.152"; // 용석

    private static final int AudioSampleRate = 44100;
    private static final int AudioChannel = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int AudioBit = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AudioMode = AudioTrack.MODE_STREAM;
    //private int portnumber = 9002;      //소린->나
    private int portnumber = 9001;      //나->소린
    DatagramSocket socket = null;

    public Send_Voice(String ip, int port){
        this.ip = ip;
        this.portnumber = port;
    }

    @Override
    public void run() {
        super.run();
        boolean mic = true;
        Log.e("보내기시작", "시작하였다 : " + Thread.currentThread().getName());
        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, AudioSampleRate, AudioChannel, AudioBit, AudioRecord.getMinBufferSize(AudioSampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 5);
        int bytes_read = 0;
        int bytes_sent = 0;
        byte buffer[] = new byte[4096];
        try {
            Log.e("성공!", "보냄");
            socket = new DatagramSocket();
            if (record.getState() == AudioRecord.STATE_INITIALIZED) {
                record.startRecording();
            } else {
                Log.e("실패!", "보내기실패!!");
            }
            //DatagramPacket packet = new DatagramPacket(buffer, bytes_read, InetAddress.getByName(ip), portnumber);
            while (mic) {
                bytes_read = record.read(buffer, 0, buffer.length);
                DatagramPacket packet = new DatagramPacket(buffer, bytes_read, InetAddress.getByName(ip), portnumber);
                socket.send(packet);
                bytes_sent += bytes_read;
                Log.e("총보낸 byte 양", String.valueOf(bytes_sent));
                sleep(20, 0);
            }
            record.stop();
            record.release();
            socket.disconnect();
            socket.close();
            mic = false;
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("Send Thread Interrupt", "Interrupt Complete");
            record.stop();
            record.release();
            socket.close();
            mic = false;
            return;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            record.stop();
            record.release();
            socket.disconnect();
            socket.close();
            mic = false;
            return;
        } catch (SocketException e) {
            e.printStackTrace();
            record.stop();
            record.release();
            socket.disconnect();
            socket.close();
            mic = false;
            return;
        } catch (IOException e) {
            e.printStackTrace();
            mic = false;
            return;
        }
    }
}
