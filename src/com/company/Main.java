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
        String allCode = "2021/8/27 000636\n" +
                "2021/8/24 000999\n" +
                "2021/8/3 002318\n" +
                "2021/8/28 002572\n" +
                "2021/8/20 002967\n" +
                "2021/8/13 300034\n" +
                "2021/8/27 300114\n" +
                "2021/8/28 300132\n" +
                "2021/8/27 300316\n" +
                "2021/8/18 300548\n" +
                "2021/8/24 300699\n" +
                "2021/8/30 600031\n" +
                "2021/8/27 600233\n" +
                "2021/8/31 600496\n" +
                "2021/8/27 600582\n" +
                "2021/8/31 600885\n" +
                "2021/8/24 601100\n" +
                "2021/8/26 601838\n" +
                "2021/8/31 603197\n" +
                "2021/8/18 605376\n" +
                "2021/8/25 688005\n" +
                "2021/8/26 688029\n" +
                "2021/7/30 688122";
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
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 10:
                    case 15:
                        prices.add(Float.parseFloat(qfqdayArray.get(i1).getAsJsonArray().get(2).getAsString()));
                        cha =  (Float.parseFloat(qfqdayArray.get(i1).getAsJsonArray().get(2).getAsString())-diyitianPrice)/diyitianPrice*100;
                        baifenbi =stockBean.getCode()+"股票"+i1+"天后的涨幅: "+ cha +"%";
                        System.out.println(baifenbi);
                        break;
                }
            }
            allStockData.put(stockBean.getCode(),prices);
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
        if (code.startsWith("600")||code.startsWith("601")||code.startsWith("688")||code.startsWith("603")||code.startsWith("605")||code.startsWith("900")){
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
