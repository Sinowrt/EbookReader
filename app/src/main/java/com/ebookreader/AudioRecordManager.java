package com.ebookreader;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.R.attr.offset;

/**
*  @ 录音管理类
*
*  @ 实现readingActivity下的录音
*
**/

public class AudioRecordManager {
    public static final String TAG = "AudioRecordManager";
    public AudioRecord mRecorder;
    private DataOutputStream dos;
    private Thread recordThread;
    public boolean isStart = false;
    private static AudioRecordManager mInstance;
    private int bufferSize;
    public static boolean isRecordplay;

    private File pcmfile = null;//

    AudioTrack player;

    public AudioRecordManager() {
        bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 2);
        isRecordplay=false;
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static AudioRecordManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecordManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecordManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 销毁线程方法
     */
    private void destroyThread() {
        try {
            isStart = false;
            if (null != recordThread && Thread.State.RUNNABLE == recordThread.getState()) {
                try {
                    Thread.sleep(500);
                    recordThread.interrupt();
                } catch (Exception e) {
                    recordThread = null;
                }
            }
            recordThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordThread = null;
        }
    }

    /**
     * 启动录音线程
     */
    private void startThread() {
        destroyThread();
        isStart = true;
        if (recordThread == null) {
            recordThread = new Thread(recordRunnable);
            recordThread.start();
        }
    }

    /**
     * 录音线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                int bytesRecord;
                //int bufferSize = 320;
                byte[] tempBuffer = new byte[bufferSize];
                if (mRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
                    stopRecord();
                    return;
                }
                mRecorder.startRecording();
                //writeToFileHead();
                while (isStart) {
                    if (null != mRecorder) {
                        bytesRecord = mRecorder.read(tempBuffer, 0, bufferSize);
                        if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (bytesRecord != 0 && bytesRecord != -1) {
                            //在此可以对录制音频的数据进行二次处理 比如变声，压缩，降噪，增益等操作
                            //我们这里直接将pcm音频原数据写入文件 这里可以直接发送至服务器 对方采用AudioTrack进行播放原数据
                            dos.write(tempBuffer, 0, bytesRecord);
                        } else {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * 保存文件
     *
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        dos = new DataOutputStream(new FileOutputStream(file, true));
    }

    /**
     * 启动录音
     *
     * @param path
     */
    public void startRecord(String path) {
        try {
            setPath(path);
            startThread();
            pcmfile = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        try {
            destroyThread();
            if (mRecorder != null) {
                if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    mRecorder.stop();
                }
                if (mRecorder != null) {
                    mRecorder.release();
                }
            }
            if (dos != null) {
                dos.flush();
                dos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String path){
        File file=new File(path);
        if(file.exists()) {
            isRecordplay = true;
            DataInputStream dis = null;
            try {
                //从音频文件中读取声音
                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //最小缓存区
            int bufferSizeInBytes = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            //创建AudioTrack对象   依次传入 :流类型、采样率（与采集的要一致）、音频通道（采集是IN 播放时OUT）、量化位数、最小缓冲区、模式
            player = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);

            byte[] data = new byte[bufferSizeInBytes];
            player.play();//开始播放
            while (true) {
                int i = 0;
                try {
                    while (dis.available() > 0 && i < data.length) {
                        data[i] = dis.readByte();//录音时write Byte 那么读取时就该为readByte要相互对应
                        i++;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                player.write(data, 0, data.length);

                if (i != bufferSizeInBytes) //表示读取完了
                {
                    isRecordplay = false;
                    player.stop();//停止播放
                    player.release();//释放资源
                    break;
                }
            }
        }

    }

    public void play()
    {
        isRecordplay=true;
        DataInputStream dis=null;
        try {
            //从音频文件中读取声音
            dis=new DataInputStream(new BufferedInputStream(new FileInputStream(pcmfile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //最小缓存区
        int bufferSizeInBytes=AudioTrack.getMinBufferSize(8000,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
        //创建AudioTrack对象   依次传入 :流类型、采样率（与采集的要一致）、音频通道（采集是IN 播放时OUT）、量化位数、最小缓冲区、模式
        player=new AudioTrack(AudioManager.STREAM_MUSIC,8000,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);

        byte[] data =new byte [bufferSizeInBytes];
        player.play();//开始播放
        while(true)
        {
            int i=0;
            try {
                while(dis.available()>0&&i<data.length)
                {
                    data[i]=dis.readByte();//录音时write Byte 那么读取时就该为readByte要相互对应
                    i++;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            player.write(data,0,data.length);

            if(i!=bufferSizeInBytes) //表示读取完了
            {
                isRecordplay=false;
                player.stop();//停止播放
                player.release();//释放资源
                break;
            }
        }

    }

    public void stopPlay(){
        isRecordplay=false;
        player.stop();
        player.release();
    }
}
