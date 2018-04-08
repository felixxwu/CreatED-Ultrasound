import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import java.awt.Desktop;

public class Cypher {

    private static int bits = 7;

    public static String charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~!*'();:@&=+$,/?#[]";

    public static int[] startCode = {0,1,1,1,1,1,1,1,0};

    public static int[] endCode = {1,1,1,0,1,1,0};

    public static int[][] codeList = { 
          {0,0,0,1,0,0,1},  //A
          {0,0,0,1,0,1,0},  //B
          {0,0,0,1,0,1,1},  //C
          {0,0,0,1,1,0,0},  //D
          {0,0,0,1,1,0,1},  //E
          {0,0,0,1,1,1,0},  //F
          {0,0,1,0,0,0,1},  //G
          {0,0,1,0,0,1,0},  //H
          {0,0,1,0,0,1,1},  //I
          {0,0,1,0,1,0,0},  //J
          {0,0,1,0,1,0,1},  //K
          {0,0,1,0,1,1,0},  //L
          {0,0,1,0,1,1,1},  //M
          {0,0,1,1,0,0,0},  //N
          {0,0,1,1,0,0,1},  //O
          {0,0,1,1,0,1,0},  //P
          {0,0,1,1,0,1,1},  //Q
          {0,0,1,1,1,0,0},  //R
          {0,0,1,1,1,0,1},  //S
          {0,1,0,0,0,1,1},  //T
          {0,1,0,0,1,0,0},  //U
          {0,1,0,0,1,0,1},  //V
          {0,1,0,0,1,1,0},  //W
          {0,1,0,0,1,1,1},  //X
          {0,1,0,1,0,0,0},  //Y
          {0,1,0,1,0,0,1},  //Z
          {0,1,0,1,0,1,0},  //a
          {0,1,0,1,0,1,1},  //b
          {0,1,0,1,1,0,0},  //c
          {0,1,0,1,1,0,1},  //d
          {0,1,0,1,1,1,0},  //e
          {0,1,1,0,0,0,1},  //f
          {0,1,1,0,0,1,0},  //g
          {0,1,1,0,0,1,1},  //h
          {0,1,1,0,1,0,0},  //i
          {0,1,1,0,1,0,1},  //j
          {0,1,1,0,1,1,0},  //k
          {0,1,1,0,1,1,1},  //l
          {0,1,1,1,0,0,0},  //m
          {0,1,1,1,0,0,1},  //n
          {0,1,1,1,0,1,0},  //o
          {0,1,1,1,0,1,1},  //p
          {1,0,0,0,1,0,0},  //q
          {1,0,0,0,1,0,1},  //r
          {1,0,0,0,1,1,0},  //s
          {1,0,0,0,1,1,1},  //t
          {1,0,0,1,0,0,0},  //u
          {1,0,0,1,0,0,1},  //v
          {1,0,0,1,0,1,0},  //w
          {1,0,0,1,0,1,1},  //x
          {1,0,0,1,1,0,0},  //y
          {1,0,0,1,1,0,1},  //z
          {1,0,0,1,1,1,0},  //0
          {1,0,1,0,0,0,1},  //1
          {1,0,1,0,0,1,0},  //2
          {1,0,1,0,0,1,1},  //3
          {1,0,1,0,1,0,0},  //4
          {1,0,1,0,1,0,1},  //5
          {1,0,1,0,1,1,0},  //6
          {1,0,1,0,1,1,1},  //7 
          {1,0,1,1,0,0,0},  //8
          {1,0,1,1,0,0,1},  //9
          {1,0,1,1,0,1,0},  //-
          {1,0,1,1,0,1,1},  //_
          {1,0,1,1,1,0,0},  //.
          {1,0,1,1,1,0,1},  //~
          {1,1,0,0,0,1,0},  //!
          {1,1,0,0,0,1,1},  //*
          {1,1,0,0,1,0,0},  //'
          {1,1,0,0,1,0,1},  //(
          {1,1,0,0,1,1,0},  //)
          {1,1,0,0,1,1,1},  //;
          {1,1,0,1,0,0,0},  //:
          {1,1,0,1,0,0,1},  //@
          {1,1,0,1,0,1,0},  //&
          {1,1,0,1,0,1,1},  //=
          {1,1,0,1,1,0,0},  //+
          {1,1,0,1,1,0,1},  //$
          {1,1,0,1,1,1,0},  //,
          {1,1,1,0,0,0,1},  ///
          {1,1,1,0,0,1,0},  //?
          {1,1,1,0,0,1,1},  //#
          {1,1,1,0,1,0,0},  //[
          {1,1,1,0,1,0,1}   //]
      };

      public static int[] charToCode(char c) {
        for (int i = 0; i < charList.length(); i++) {
            if (charList.charAt(i) == c) {
                    // return code
                int[] code = new int[bits];
                for (int j = 0; j < bits; j++) {
                    code[j] = codeList[i][j];
                }
                return code;
            }
        }
        return null;
    }

    public static char codeToChar(int[] code) {
        for (int i = 0; i < charList.length(); i++) {
            // check code
            boolean rightCode = true;
            for (int j = 0; j < bits; j++) {
                if (codeList[i][j] != code[j]) {
                    rightCode = false;
                    break;
                }
            }
            if (rightCode) {
                return charList.charAt(i);
            }
        }
        return '%';
    }

    public static int[] encode(String message) {
        int[] out = new int[bits * message.length() + startCode.length];
        for (int i = 0; i < startCode.length; i++) {
            out[i] = startCode[i];
        }
        // for each message char
        for (int i = 0; i < message.length(); i++) {
            // for each bit in the code
            int[] code = charToCode(message.charAt(i));
            for (int j = 0; j < bits; j++) {
                // startcode length because it starts after an ofset
                // * bits because its every 7 bits
                // + j for current 7 bits (this loop)
                out[startCode.length + (i)*bits + j] = code[j];
            }
        }
        return out;
    }

    // assumes perfect code (properly aligned)
    public static String decode(int[] code) {
        String out = "";
        int numCodes = code.length / bits;
        for (int i = 0; i < numCodes; i++) {
            int[] curCode = new int[bits];
            for (int j = 0; j < bits; j++) {
                curCode[j] = code[(i * bits) + j];
            }
            out += codeToChar(curCode);
        }
        return out;
    }

}