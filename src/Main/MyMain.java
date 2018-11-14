package Main;

import Untils.GrammaticalAnalysis;
import Untils.Keywords;
import Untils.RegularExpression;

import java.util.Scanner;

public class MyMain {
    public static void main(String[] arg) {
        System.out.print("zhangyinghao:~zhangyinghao$");
        Scanner sc = new Scanner(System.in);
        String loginSentence;
        String sentence;
        loginSentence = sc.nextLine();
        if(loginSentence.matches(RegularExpression.LOGIN)==true){
            char temp;
            while (true) {
                System.out.print(Keywords.MYSQL+">");
                sentence = "";
                sentence = sc.nextLine();
                temp = sentence.charAt(sentence.length() - 1);
                if (sentence.equals("quit;")) {
                    System.out.print("bye");
                    break;
                } else if (temp == ';') {
                    GrammaticalAnalysis.grammaticlAnalysis(sentence);
                } else {
                    while (true) {
                        if (temp == ';') {
                            break;
                        }
                        System.out.print("    ->");
                        sentence = sentence + sc.nextLine();
                        temp = sentence.charAt(sentence.length() - 1);
                    }
                    GrammaticalAnalysis.grammaticlAnalysis(sentence);
                }
            }
        }else {
            System.out.println("The sentence don't identify !");
        }

    }
}
