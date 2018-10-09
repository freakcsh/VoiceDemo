package com.android.freak.voicedemo;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */

public class VoiceTemplate {
    private static final String DOT = ".";

    private String numString;

    private String prefix;//收款成功音频

    private String suffix;//元的音频

    public VoiceTemplate() {

    }

    /**
     * 获取播放的音频文件顺序集合
     * @param money
     * @return
     */
    public static List<String> getDefaultTemplate(String money){
        return new VoiceTemplate()
                .prefix("success")
                .numString(money)
                .suffix("yuan")
                .gen();
    }

    public String getPrefix() {
        return prefix;
    }

    public VoiceTemplate prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public VoiceTemplate suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }


    public String getNumString() {
        return numString;
    }

    public VoiceTemplate numString(String numString) {
        this.numString = numString;
        return this;
    }


    public List<String> gen() {
        return genVoiceList();
    }

    private List<String> createReadableNumList(String numString) {
        List<String> result = new ArrayList<>();
        if (!TextUtils.isEmpty(numString)) {
            int len = numString.length();
            for (int i = 0; i < len; i++) {
                if ('.' == numString.charAt(i)) {
                    result.add("dot");
                } else {
                    result.add(String.valueOf(numString.charAt(i)));
                }
            }
        }
        return result;
    }


    /**
     * 播放音频文件顺序
     * @return
     */
    private List<String> genVoiceList() {
        List<String> result = new ArrayList<>();
        if (!TextUtils.isEmpty(prefix)) {
            result.add(prefix);
        }
        if (!TextUtils.isEmpty(numString)) {
            result.addAll(genReadableMoney(numString));
        }

        if (!TextUtils.isEmpty(suffix)) {
            result.add(suffix);
        }
        return result;
    }


    private List<String> genReadableMoney(String numString) {
        List<String> result = new ArrayList<>();
        if (!TextUtils.isEmpty(numString)) {
            if (numString.contains(DOT)) {
                String integerPart = numString.split("\\.")[0];
                String decimalPart = numString.split("\\.")[1];
                List<String> intList = readIntPart(integerPart);
                List<String> decimalList = readDecimalPart(decimalPart);
                result.addAll(intList);
                if (!decimalList.isEmpty()){
                    result.add("dot");
                    result.addAll(decimalList);
                }
            }else {
                //int
                result.addAll(readIntPart(numString));
            }
        }
        return result;
    }

    private List<String> readDecimalPart(String decimalPart) {
        List<String> result = new ArrayList<>();
        if (!"00".equals(decimalPart)){
            char[] chars = decimalPart.toCharArray();
            for (char ch : chars) {
                result.add(String.valueOf(ch));
            }
        }
        return result;
    }


    private List<String> readIntPart(String integerPart) {
        List<String> result = new ArrayList<>();
        String intString = readInt(Integer.parseInt(integerPart));
        int len = intString.length();
        Log.e("TAG","字符串："+intString);
        for (int i =0; i < len;i++){
            char current = intString.charAt(i);
            if (current == '拾'){
                result.add("ten");
            }else if (current == '佰'){
                result.add("hundred");
            }else if (current == '仟'){
                result.add("thousand");
            }else if (current == '万'){
                result.add("ten_thousand");
            }else if (current == '亿'){
                result.add("ten_million");
            }else {
                result.add(String.valueOf(current));
            }
        }
        return result;
    }



    private static final char [] NUM ={'0','1','2','3','4','5','6','7','8','9'};
    private static final char [] CHINESE_UNIT = {'元','拾','佰','仟','万','拾','佰','仟','亿','拾','佰','仟'};

    /**
     * 返回关于钱的中文式大写数字,支仅持到亿
     * */
    public static String readInt(int moneyNum){
        String res="";
        int i=0;
        if(moneyNum==0) {
            return "0";
        }

        if (moneyNum == 10){
            return "拾";
        }

        if (moneyNum > 10 && moneyNum < 20) {
            return "拾" + moneyNum % 10;
        }

        while(moneyNum>0){
            res=CHINESE_UNIT[i++]+res;
            res=NUM[moneyNum%10]+res;
            moneyNum/=10;
        }
        return res.replaceAll("0[拾佰仟]", "0")
                .replaceAll("0+亿", "亿").replaceAll("0+万", "万")
                .replaceAll("0+元", "元").replaceAll("0+", "0")
                .replace("元","");
    }
}
