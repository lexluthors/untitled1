package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("adb shell top -d 1 -o %CPU,%MEM,CMDLINE |grep guoyuan.szkingdom.android.phone");
//            Process process = Runtime.getRuntime().exec("adb shell top -m 10");


//            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            //StringBuffer b = new StringBuffer();
//            String line = null;
//            StringBuffer b = new StringBuffer();
//            while ((line = br.readLine()) != null) {
//                b.append(line + "\n");
//            }
//            System.out.println(b.toString());




            //p.waitFor();
            BufferedReader bReader=new BufferedReader(new InputStreamReader(process.getInputStream(),"gbk"));
            String line=null;
            while((line=bReader.readLine())!=null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
