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
        String allCode = "2021/8/4 601236\n" +
                "2021/7/8 601015\n" +
                "2021/6/10 600909\n" +
                "2021/4/21 600888\n" +
                "2020/12/17 600919\n" +
                "2020/10/22 000728\n" +
                "2020/9/29 600008\n" +
                "2020/9/17 002788\n" +
                "2020/8/19 000088\n" +
                "2020/8/13 000885\n" +
                "2020/7/28 603269\n" +
                "2020/7/21 603633\n" +
                "2020/7/20 600999\n" +
                "2020/6/29 002500\n" +
                "2020/3/31 002884\n" +
                "2020/3/23 601555\n" +
                "2020/3/19 601162\n" +
                "2020/3/19 000977\n" +
                "2020/3/6 000901\n" +
                "2020/1/14 000750\n" +
                "2020/1/14 600419\n" +
                "2019/12/30 300715\n" +
                "2019/12/26 002466\n" +
                "2019/7/11 002185\n" +
                "2019/6/20 600480\n" +
                "2019/4/17 601012\n" +
                "2019/4/17 002166\n" +
                "2019/4/15 600360\n" +
                "2019/3/29 002202\n" +
                "2019/3/6 600113\n" +
                "2019/3/1 600459\n" +
                "2019/2/28 002017\n" +
                "2018/11/1 600219\n" +
                "2018/10/16 600380\n" +
                "2018/7/25 000563\n" +
                "2018/6/29 300387\n" +
                "2018/5/28 002350\n" +
                "2018/4/3 002775\n" +
                "2018/3/28 600256";
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

            //按照开盘价计算
            float diyitianPrice = Float.parseFloat(qfqdayArray.get(0).getAsJsonArray().get(1).getAsString());
            ArrayList<Float> prices = new ArrayList<>();
            prices.add(diyitianPrice);

            for (int i1 = 0; i1 < qfqdayArray.size(); i1++) {
                float cha = 0;
                String baifenbi = "0%";
//                System.out.println(qfqdayArray.get(i1).getAsJsonArray().get(2).getAsString());
                switch (i1){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 8:
                    case 12:
                    case 20:
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
