package com.home.shitou.app4.checkupdatelibrary.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.home.shitou.app4.checkupdatelibrary.callback.CheckUpdateCallback;
import com.home.shitou.app4.checkupdatelibrary.callback.CheckUpdateCallback2;
import com.home.shitou.app4.checkupdatelibrary.callback.DownloadCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by qiang_xi on 2016/10/9 20:36.
 * 网络请求类
 */

public class HttpRequest {
    private static final int hasUpdate = 0;
    private static final int noUpdate = 1;
    private static final int downloadSuccess = 2;
    private static final int downloading = 3;
    private static final int downloadFailure = 4;
    private static final int checkSuccess = 5;
    private static final int checkUpdateFailure = -1;

    private static CheckUpdateCallback updateCallback;//检查更新回调
    private static CheckUpdateCallback2 updateCallback2;//检查更新回调
    private static DownloadCallback downloadCallback;//下载回调
    private static long timestamp;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                //检查更新成功
                case checkSuccess:
                    updateCallback2.onCheckUpdateSuccess((String) msg.obj);
                    break;
                //有更新
                case hasUpdate:
                    updateCallback.onCheckUpdateSuccess((String) msg.obj, true);
                    break;
                //无更新
                case noUpdate:
                    updateCallback.onCheckUpdateSuccess((String) msg.obj, false);
                    break;
                //检查更新失败
                case checkUpdateFailure:
                    if (null != updateCallback) {
                        updateCallback.onCheckUpdateFailure((String) msg.obj, -1);
                    }
                    if (null != updateCallback2) {
                        updateCallback2.onCheckUpdateFailure((String) msg.obj);
                    }
                    break;
                //apk文件下载中,1s回调一次
                case downloading:
                    downloadCallback.onProgress(data.getLong("currentLength"), data.getLong("fileLength"));
                    break;
                //apk文件下载成功
                case downloadSuccess:
                    downloadCallback.onDownloadSuccess((File) data.getSerializable("file"));
                    break;
                //apk文件下载失败
                case downloadFailure:
                    downloadCallback.onDownloadFailure((String) msg.obj);
                    break;
            }
        }
    };

    private HttpRequest() {
    }


    /**
     * post请求检查更新,实体类中必须要有newAppVersionCode字段,不然检查不到更新
     *
     * @param currentVersionCode 当前应用版本号
     * @param urlPath            请求地址
     * @param callback           请求回调
     */
    public static void post(final int currentVersionCode, @NonNull final String urlPath, @NonNull final CheckUpdateCallback callback) {
        updateCallback = callback;
        final Message message = new Message();
        new Thread() {
            @Override
            public void run() {
                super.run();
                BufferedReader bufferedReader = null;
                try {
                    URL httpUrl = new URL(urlPath);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
                    //设置请求头header
                    httpURLConnection.setRequestProperty("test-header", "post-header-value");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(5000);
                    //获取内容
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    final StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    String json = sb.toString();
                    JSONObject object = new JSONObject(json);
                    int newAppVersionCode = object.getInt("newAppVersionCode");
                    message.obj = json;
                    //有更新
                    if (newAppVersionCode > currentVersionCode) {
                        message.what = hasUpdate;
                        handler.sendMessage(message);
                    }
                    //无更新
                    else {
                        message.what = noUpdate;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    message.obj = e.toString();
                    message.what = checkUpdateFailure;
                    handler.sendMessage(message);
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * post请求检查更新,实体类可以任意自定义,不需要有newAppVersionCode字段,所以扩展性更强,但是需要自己进行是否有更新的判断
     *
     * @param urlPath  请求地址
     * @param callback 请求回调
     */
    public static void post(final String urlPath, CheckUpdateCallback2 callback) {
        updateCallback2 = callback;
        final Message message = new Message();
        new Thread() {
            @Override
            public void run() {
                super.run();
                BufferedReader bufferedReader = null;
                try {
                    URL httpUrl = new URL(urlPath);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
                    //设置请求头header
                    httpURLConnection.setRequestProperty("test-header", "post-header-value");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(5000);
                    //获取内容
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    final StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    message.obj = sb.toString();
                    message.what = checkSuccess;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.obj = e.toString();
                    message.what = checkUpdateFailure;
                    handler.sendMessage(message);
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * get请求检查更新,实体类中必须要有newAppVersionCode字段,不然检查不到更新
     *
     * @param currentVersionCode 当前应用版本号
     * @param urlPath            请求地址
     * @param callback           请求回调
     */
    public static void get(final int currentVersionCode, @NonNull final String urlPath, @NonNull final CheckUpdateCallback callback) {
        updateCallback = callback;
        final Message message = new Message();
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputStream inputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader reader = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlPath);
                    URLConnection connection = url.openConnection();
                    httpURLConnection = (HttpURLConnection) connection;
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    StringBuilder sb = new StringBuilder();
                    String tempLine = null;
                    inputStream = httpURLConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(inputStreamReader);
                    while ((tempLine = reader.readLine()) != null) {
                        sb.append(tempLine);
                    }
                    String json = sb.toString();
                    JSONObject object = new JSONObject(json);
                    int newAppVersionCode = object.getInt("newAppVersionCode");
                    message.obj = json;
                    //有更新
                    if (newAppVersionCode > currentVersionCode) {
                        message.what = hasUpdate;
                        handler.sendMessage(message);
                    }
                    //无更新
                    else {
                        message.what = noUpdate;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    message.obj = e.toString();
                    message.what = checkUpdateFailure;
                    handler.sendMessage(message);
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }.start();
    }

    /**
     * get请求检查更新,实体类可以任意自定义,不需要有newAppVersionCode字段,所以扩展性更强,但是需要自己进行是否有更新的判断
     *
     * @param urlPath  请求地址
     * @param callback 请求回调
     */
    public static void get(final String urlPath, CheckUpdateCallback2 callback) {
        updateCallback2 = callback;
        final Message message = new Message();
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputStream inputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader reader = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(urlPath);
                    URLConnection connection = url.openConnection();
                    httpURLConnection = (HttpURLConnection) connection;
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    StringBuilder sb = new StringBuilder();
                    String tempLine = null;
                    inputStream = httpURLConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(inputStreamReader);
                    while ((tempLine = reader.readLine()) != null) {
                        sb.append(tempLine);
                    }
                    message.obj = sb.toString();
                    message.what = checkSuccess;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    message.obj = e.toString();
                    message.what = checkUpdateFailure;
                    handler.sendMessage(message);
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }.start();
    }

    /**
     * 下载专用
     *
     * @param downloadPath 下载地址
     * @param filePath     文件存储路径
     * @param fileName     文件名
     * @param callback     下载回调
     */
    public static void download(@NonNull final String downloadPath, @NonNull final String filePath, @NonNull final String fileName, @NonNull final DownloadCallback callback) {
        downloadCallback = callback;
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(downloadPath);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int fileLength = connection.getContentLength();
                    // 文件总长度
                    input = connection.getInputStream();
                    File directory = new File(filePath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    File file = new File(filePath, fileName);
                    //如果存在该文件,则删除
                    if (file.exists()) {
                        file.delete();
                    }
                    output = new FileOutputStream(file);
                    byte data[] = new byte[4096];
                    //当前下载进度
                    int current = 0;
                    int count;
                    Bundle bundle = new Bundle();
                    while ((count = input.read(data)) != -1) {
                        current += count;
                        output.write(data, 0, count);
                        if (fileLength > 0) {
                            //这里必须要进行消息发送间隔的约束,这里为1s发送一次,不然会发送大量的message,全部都在排着队,
                            //而这造成的后果就是当下载完成时,发送的下载完成message不能及时发送出去,导致
                            //软件已经下载完成,却不能及时回调下载完成方法,从而不能及时进行安装的bug
                            if (current < fileLength) {
                                if (System.currentTimeMillis() - timestamp < 1000) {
                                    continue;
                                }
                            }
                            timestamp = System.currentTimeMillis();
                            //这里需要一直new新的message,不然会报错
                            Message message = new Message();
                            message.what = downloading;
                            bundle.putLong("currentLength", current);
                            bundle.putLong("fileLength", fileLength);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                    Message message = new Message();
                    message.what = downloadSuccess;
                    bundle.putSerializable("file", file);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = downloadFailure;
                    message.obj = e.toString();
                    handler.sendMessage(message);
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }.start();
    }
}