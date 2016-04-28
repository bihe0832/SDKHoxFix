
/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.bihe0832.hotfix.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FileUtils 
{


	public static void URLToFile(String strURL, File file)
	{
		InputStream is = null;
		
		try {
			is = __GetInputStreamFromURL(strURL);
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			byte[] bufferByte = new byte[1024];
			int l = -1;
			while ((l = is.read(bufferByte)) > -1) {
				bout.write(bufferByte, 0, l);
			}
			byte[] rBytes = bout.toByteArray();
			bout.close();
			is.close();

			if (!file.exists()) {
				file.createNewFile();
			}

			DataOutputStream dos = new DataOutputStream(new FileOutputStream(
					file));
			dos.write(rBytes);
			dos.flush();
			dos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private static  InputStream __GetInputStreamFromURL(String urlStr) 
    {  
        HttpURLConnection urlConn = null;  
        InputStream inputStream = null;  
        try 
        {  
        	URL url = new URL(urlStr);  
            urlConn = (HttpURLConnection)url.openConnection();  
            inputStream = urlConn.getInputStream();  
              
        }catch (MalformedURLException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        } catch (Exception e){
			e.printStackTrace();
		}
          
        return inputStream;  
    }
}





















