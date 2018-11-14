package Untils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUntils {
    /**
     * 获得有效的正则表达式结果
     * @param sentence
     * @param regular
     * @return
     */
    public static String getValues(String sentence,String regular){
        String result=null;
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(sentence);
        while (m.find()){
            result=m.group();
        }
        return result;
    }

    /**
     * 判断改句子是否符合当前正则表达式
     * @param sentence
     * @param regular
     * @return
     */
    public static boolean isValid(String sentence,String regular){
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(sentence);
        return m.matches();
    }
}
