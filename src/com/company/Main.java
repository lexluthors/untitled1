package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class Main {

    public static void main(String[] args) {
        //string=股票代码, list包含所有天数的价格,1,3,5,7日期的价格
        LinkedHashMap<String, ArrayList<Float>> allStockData = new LinkedHashMap<>();

	// write your code here
        String allCode = "2021/7/21 688536\n" +
                "2021/7/21 688608\n" +
                "2021/7/21 688016\n" +
                "2021/7/21 300628\n" +
                "2021/7/21 300776\n" +
                "2021/7/21 688023\n" +
                "2021/7/21 688116\n" +
                "2021/7/21 688111\n" +
                "2021/7/21 600563\n" +
                "2021/7/21 603713\n" +
                "2021/7/21 688179\n" +
                "2021/7/21 300274\n" +
                "2021/7/21 688006\n" +
                "2021/7/21 689009\n" +
                "2021/7/21 603806\n" +
                "2021/7/21 688139\n" +
                "2021/7/21 600298\n" +
                "2021/7/21 002812\n" +
                "2021/7/21 002459\n" +
                "2021/7/21 601799\n" +
                "2021/7/21 002049\n" +
                "2021/7/21 603108\n" +
                "2021/7/21 600763\n" +
                "2021/7/21 601688\n" +
                "2021/7/21 688036\n" +
                "2021/7/21 300433\n" +
                "2021/7/21 002241\n" +
                "2021/7/21 300502\n" +
                "2021/7/21 300059\n" +
                "2021/7/21 603613\n" +
                "2021/7/21 003816\n" +
                "2021/7/21 300408\n" +
                "2021/7/21 600079\n" +
                "2021/7/21 688012\n" +
                "2021/7/21 601318\n" +
                "2021/7/21 603986\n" +
                "2021/7/21 300759\n" +
                "2021/7/21 603707\n" +
                "2021/7/21 600346\n" +
                "2021/7/21 601677\n" +
                "2021/7/21 300529\n" +
                "2021/7/21 601965\n" +
                "2021/7/21 603899\n" +
                "2021/7/21 300476\n" +
                "2021/7/21 300496\n" +
                "2021/7/21 603501\n" +
                "2021/7/21 300454\n" +
                "2021/7/21 600887\n" +
                "2021/7/21 688099\n" +
                "2021/7/21 002568\n" +
                "2021/7/21 002714\n" +
                "2021/7/21 601058\n" +
                "2021/7/21 000910\n" +
                "2021/7/21 601088\n" +
                "2021/7/21 300661\n" +
                "2021/7/21 601021\n" +
                "2021/7/21 600809\n" +
                "2021/7/21 002311\n" +
                "2021/7/21 600031\n" +
                "2021/7/21 300601\n" +
                "2021/7/21 300122\n" +
                "2021/7/21 688981\n" +
                "2021/7/21 300760\n" +
                "2021/7/21 603799\n" +
                "2021/7/21 688126\n" +
                "2021/7/21 002048\n" +
                "2021/7/21 002271\n" +
                "2021/7/21 600406\n" +
                "2021/7/21 600383\n" +
                "2021/7/21 601169\n" +
                "2021/7/21 601888\n" +
                "2021/7/21 601166\n" +
                "2021/7/21 300413\n" +
                "2021/7/21 002304\n" +
                "2021/7/21 600036\n" +
                "2021/7/21 603128\n" +
                "2021/7/21 000568\n" +
                "2021/7/21 603885\n" +
                "2021/7/21 300012\n" +
                "2021/7/21 600690\n" +
                "2021/7/21 002493\n" +
                "2021/7/21 300285\n" +
                "2021/7/21 002475\n" +
                "2021/7/21 600048\n" +
                "2021/7/21 603259\n" +
                "2021/7/21 300014\n" +
                "2021/7/21 600660\n" +
                "2021/7/21 300253\n" +
                "2021/7/21 600703\n" +
                "2021/7/21 000858\n" +
                "2021/7/21 002601\n" +
                "2021/7/21 600297\n" +
                "2021/7/21 601012\n" +
                "2021/7/21 300015\n" +
                "2021/7/21 002415\n" +
                "2021/7/21 601658\n" +
                "2021/7/21 002027\n" +
                "2021/7/21 601899\n" +
                "2021/7/21 000725";
        String[] strs = allCode.split("\n");
        for (int i = 0; i < strs.length; i++) {
            StockBean stockBean = new StockBean();
            String[] oneCode = strs[i].split(" ");
//            System.out.println(oneCode[0].replaceAll("/","-"));
            stockBean.setDate(oneCode[0].replaceAll("/","-"));
            stockBean.setCode(getStockCode(oneCode[1]));
            String url = "https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param="+stockBean.getCode()+",day,"+stockBean.getDate()+","+getDate()+",500,qfq";
            String jsonAll = getStockData(url);
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonNode = jsonParser.parse(jsonAll);
            JsonObject jsonObject = jsonNode.getAsJsonObject();
            JsonObject jsonData = jsonObject.getAsJsonObject("data");
            JsonObject codeJson = jsonData.getAsJsonObject(stockBean.getCode());
            JsonArray qfqdayArray = new JsonArray();
            if (codeJson.has("qfqday")){
                qfqdayArray = codeJson.getAsJsonArray("qfqday");
            }else{
                qfqdayArray = codeJson.getAsJsonArray("day");
            }

            float diyitianPrice = Float.parseFloat(qfqdayArray.get(0).getAsJsonArray().get(2).getAsString());
            ArrayList<Float> prices = new ArrayList<>();
            prices.add(diyitianPrice);

            for (int i1 = 0; i1 < qfqdayArray.size(); i1++) {
                float cha = 0;
                String baifenbi = "0%";
//                System.out.println(qfqdayArray.get(i1).getAsJsonArray().get(2).getAsString());
                switch (i1){
//                    case 1:
//                    case 3:
                    case 5:
//                    case 7:
                    case 10:
                    case 15:
                    case 20:
                    case 25:
                    case 35:
                    case 40:
                    case 45:
                        prices.add(Float.parseFloat(qfqdayArray.get(i1).getAsJsonArray().get(2).getAsString()));
                        cha =  (Float.parseFloat(qfqdayArray.get(i1).getAsJsonArray().get(2).getAsString())-diyitianPrice)/diyitianPrice*100;
                        baifenbi =stockBean.getCode()+"股票"+i1+"天后的涨幅: "+ cha +"%";
                        System.out.println(baifenbi);
                        break;
                }
            }
            allStockData.put(stockBean.getCode(),prices);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        String url = "https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param=sz000636,day,2021-8-27,2021-10-11,500,qfq";
//        Gson gson = new Gson();
//        JsonParser jsonParser = new JsonParser();
//        JsonElement jsonNode = jsonParser.parse(getData(url));
//        JsonObject jsonObject = jsonNode.getAsJsonObject();
//        JsonObject jsonData = jsonObject.getAsJsonObject("data");
//        jsonData.getAsJsonObject("")
//        System.out.println("id : " + id);
    }



    public static String getStockData(String get_url){
        StringBuffer stringBuffer=new StringBuffer();
        try {
            URL url = new URL(get_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5*1000);//链接超时
            urlConnection.setReadTimeout(5*1000);//返回数据超时
            //getResponseCode (1.200请求成功 2.404请求失败)
            if(urlConnection.getResponseCode()==200){
                //获得读取流写入
                InputStream inputStream = urlConnection.getInputStream();
                byte[] bytes=new byte[1024];
                int len=0;
                while ((len=inputStream.read(bytes))!=-1){
                    stringBuffer.append(new String(bytes,0,len));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static String getStockCode(String code){
        //000、002、003、200、300
        if (code.startsWith("000")||code.startsWith("002")||code.startsWith("003")||code.startsWith("200")||code.startsWith("300")){
            return "sz"+code;
        }
        if (code.startsWith("689009")||code.startsWith("600")||code.startsWith("601")||code.startsWith("688")||code.startsWith("603")||code.startsWith("605")||code.startsWith("900")){
            return "sh"+code;
        }
        return code;
//        600、601、603、605、900,688
    }

    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }
}
