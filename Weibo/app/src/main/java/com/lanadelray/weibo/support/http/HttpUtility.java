package com.lanadelray.weibo.support.http;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created by Administrator on 2015/6/13 0013.
 */
public class HttpUtility {
    private static final String TAG = HttpUtility.class.getSimpleName();

    public static final String POST = "POST";
    public static final String GET = "GET";

    public static String doRequest(String url, WeiboParameters params, String method) throws IOException {
        boolean isGet = false;
        if (method.equals(GET)){
            isGet = true;
        }

        String myUrl = url;
        
        String send = params.encode();
        if(isGet) {
            myUrl += "?" + send;
        }
        //TODO DEBUG

        URL u = new URL(myUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();

        conn.setRequestMethod(method);
        conn.setDoOutput(!isGet);

        if(isGet) {
            conn.setDoOutput(false);
            conn.setDoInput(true);
        } else {
            conn.setDoOutput(true);
        }

        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");

        if (send != null){
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.connect();


            if (!isGet) {
                DataOutputStream o = new DataOutputStream(conn.getOutputStream());

                o.write(send.getBytes());
                o.flush();
                o.close();
            }
        } else {
            Object[] r = params.toBoundaryMsg();
            String b = (String) r[0];
            Bitmap bmp = (Bitmap) r[1];
            String s = (String) r[2];
            byte[] bs = ("--" + b + "--\r\n").getBytes("UTF-8");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            byte[] img = bytes .toByteArray();

            int l = s.getBytes("UTF-8").length + img.length +  2 * bs.length;

            conn.setRequestProperty("Content-type", "multipart/form-data:boundary=" + b);
            conn.setRequestProperty("Content-Length", String.valueOf(l));
            conn.setFixedLengthStreamingMode(l);

            conn.connect();

            DataOutputStream o = new DataOutputStream(conn.getOutputStream());
            o.write(img);
            o.write(bs);
            o.write(bs);
            o.flush();
            o.close();

//todo dubug
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        } else {
            InputStream in = conn.getInputStream();

            String en = conn.getContentEncoding();

            if (en != null && en.equals("gzip")) {
                in = new GZIPInputStream(in);
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

            String s ;
            StringBuilder str = new StringBuilder();

            while ((s = buffer.readLine()) != null) {
                str.append(s);
            }

            return str.toString();
        }
    }

}
