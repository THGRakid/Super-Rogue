package com.CW3.game;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * File Reader
 */
public class  In {
   private BufferedReader br;

   // for files and web pages
   public In(String s) {

      // try to read file from local file system
      try {
         URL url = getClass().getResource("/Dungeons/" + s);       // for files, even if included in a jar
         if (url == null) url = new URL(s);         // no file found, try a URL

         URLConnection site    = url.openConnection();
         InputStream is        = site.getInputStream();
         InputStreamReader isr = new InputStreamReader(is);
         br = new BufferedReader(isr);
      } catch(IOException ioe) { ioe.printStackTrace(); }
   }



   // return rest of line as string and return it, not including newline 
   public String readLine() {
       String s = null;
       try { s = br.readLine(); }
       catch(IOException ioe) { ioe.printStackTrace(); }
       return s;
   }



}
