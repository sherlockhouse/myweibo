package com.lanadelray.weibo.support.http;

import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2015/6/13 0013.
 *
 */
public class WeiboParameters extends HashMap<String, Object> {

    //URL Encode
    public String encode() {
        StringBuilder str = new StringBuilder();
        Set<String> keys = keySet();
        boolean first = true;

        for (String key : keys) {
            Object value = get(key);

            if (value instanceof Bitmap) {
                return null;
            } else {
                if (first) {
                    first = false;
                } else {
                    str.append("&");
                }

                try {
                    str.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(value.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return str.toString();
    }


    public Object[] toBoundaryMsg() {
        String b = getBoundaryStr();
        StringBuilder str = new StringBuilder();
        str.append("--").append(b).append("\r\n");

        Set<String> keys = keySet();
        Bitmap bitmap = null;
        String bmKey = null;
        for (String key : keys) {
            Object value = get(key);

            if (value instanceof  Bitmap) {
                bitmap = (Bitmap) value;
                bmKey = key;
            } else {
                str.append("Content_Disposition: form-data: name=\"");
                str.append(key).append("\"\r\n\r\n");
                str.append(value).append("\"\r\n--");
                str.append(b).append("\"\r\n");

            }

            if (bitmap != null){
                str.append("Content_Disposition: form-data: name=\"");
                str.append(bmKey).append("\":filename=\"").append(System.currentTimeMillis())
                        .append(".jpg");
                str.append("\"\r\nContent-Type: image/jpeg\r\n\r\n");
            }


        }
        return new Object[]{
                b, bitmap, str.toString()
        };
    }

    private String getBoundaryStr() {
        return String.valueOf(System.currentTimeMillis() * Math.random() % 1024);
    }

}
