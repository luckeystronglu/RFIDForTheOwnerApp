package com.foxconn.rfid.theowner.util.file;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class FormFileUpLoad
{
  public static int sendFileToServer(String url, Part[] parts1)
  {
    int status = 0;
    try {
      PostMethod filePost = new PostMethod(url);
      Part[] parts = parts1;

      MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(
        parts, filePost.getParams());
      filePost.setRequestEntity(multipartRequestEntity);
      HttpClient clients = new HttpClient();
      status = clients.executeMethod(filePost);
      BufferedReader rd = new BufferedReader(
        new InputStreamReader(filePost.getResponseBodyAsStream(), "UTF-8"));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = rd.readLine()) != null) {
        stringBuffer.append(line);
      }
      rd.close();

      Log.i("cat", stringBuffer.toString());
    } catch (Exception e) {
      Log.i("err", "发布是ba");
      return 500;
    }
    return status;
  }
}