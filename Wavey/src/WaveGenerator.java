<<<<<<< HEAD
import java.io.File;
import java.util.*;
=======
import java.io.*;
import javax.sound.sampled.*;
import java.awt.Desktop;
>>>>>>> bf555b7f41083b33f9e9089eedd9addd284d35d8

public class WaveGenerator{
    public static void main(String[] args)
    {
        try
        {
            int sampleRate = 44100;    // Samples per second
            double duration = 5.0;     // Seconds

            // Calculate the number of frames required for specified duration
            long numFrames = (long)(duration * sampleRate);

            // Create a wav file with the name specified as the first argument
            WavFile wavFile = WavFile.newWavFile(new File("wav.wav"), 1, numFrames, 16, sampleRate);
            int bufferLength = 290;
            // Create a buffer of 100 frame s
            double[][] buffer = new double[1][bufferLength];
            String url = "Hi_Felix_and_Pablo_are_the_gods_of_the_universe";
            int[] half = WaveGenerator.encode(url);
            int[] message = new int[half.length*2];
            for(int h =0;h<half.length;h++){
            	message[h]=half[h];
            	message[h+half.length]=half[h];
            }
            // Initialise a local frame counter
            long frameCounter = 0;
            double basicFreq=22430; //22430
            double smoother =0;
            int alternating = 0;
            for(int m :message){

                long remaining = wavFile.getFramesRemaining();
                int toWrite = (remaining > bufferLength) ? bufferLength : (int) remaining;
                if(m==0){
                    for (int s=0 ; s<toWrite ; s++, frameCounter++)
                    { 
                     buffer[0][s] = 0;
                     smoother=0;
                 }

             }else{
                 for (int s=0 ; s<toWrite ; s++, frameCounter++)
                 { 
                   double smootherInterval = toWrite/2;
//        			 	buffer[0][s]=alternating*smoother;
//        			 	if(alternating==0){alternating=1;}else{alternating=0;}
                   buffer[0][s] = Math.sin(2.0 * Math.PI * basicFreq * frameCounter / sampleRate)* smoother;
                   if(s<smootherInterval && smoother<1){
                       smoother=Math.pow(s/smootherInterval,3.5);;
                   }
                   if(s>toWrite-smootherInterval){
                       smoother=Math.pow((toWrite-s)/smootherInterval,3.5);
                       // System.out.println(smoother);
                   }
               }
           }
           wavFile.writeFrames(buffer, toWrite);
       }

         // Loop until all frames written
//         while (frameCounter < numFr ames)
//         {
//            // Determine how many frames to write, up to a maximum of the buffer size 
//            long remaining = wavFile.getFramesRemaining();
//            int toWrite = (remaining > 1000) ? 1000 : (int) remaining;
//
//            // Fill the buffer, one tone per channel
//            
//            for (int s=0 ; s<toWrite ; s++, frameCounter++)
//            { 
//            		buffer[0][s] = Math.sin(2.0 * Math.PI * basicFreq * frameCounter / sampleRate);
//            }
//
//            // Write the buffer
//            wavFile.writeFrames(buffer, toWrite);
// 
//         }

         // Close the wavFile
       wavFile.close();

       Desktop.getDesktop().open(new File("wav.wav"));

   }
   catch (Exception e)
   {
       System.err.println(e);
   }
}

private static int bits = 7;

public static String charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~!*'();:@&=+$,/?#[]";

public static int[] startCode = {0,1,1,1,1,1,1,1,0};

public static int[][] codeList = { 
      {0,0,0,1,0,0,1},	//A
      {0,0,0,1,0,1,0},	//B
      {0,0,0,1,0,1,1},	//C
      {0,0,0,1,1,0,0},	//D
      {0,0,0,1,1,0,1},	//E
      {0,0,0,1,1,1,0},	//F
      {0,0,1,0,0,0,1},	//G
      {0,0,1,0,0,1,0},	//H
      {0,0,1,0,0,1,1},	//I
      {0,0,1,0,1,0,0},	//J
      {0,0,1,0,1,0,1},	//K
      {0,0,1,0,1,1,0},	//L
      {0,0,1,0,1,1,1},	//M
      {0,0,1,1,0,0,0},	//N
      {0,0,1,1,0,0,1},	//O
      {0,0,1,1,0,1,0},	//P
      {0,0,1,1,0,1,1},	//Q
      {0,0,1,1,1,0,0},	//R
      {0,0,1,1,1,0,1},	//S
      {0,1,0,0,0,1,1},	//T
      {0,1,0,0,1,0,0},	//U
      {0,1,0,0,1,0,1},	//V
      {0,1,0,0,1,1,0},	//W
      {0,1,0,0,1,1,1},	//X
      {0,1,0,1,0,0,0},	//Y
      {0,1,0,1,0,0,1},	//Z
      {0,1,0,1,0,1,0},	//a
      {0,1,0,1,0,1,1},	//b
      {0,1,0,1,1,0,0},	//c
      {0,1,0,1,1,0,1},	//d
      {0,1,0,1,1,1,0},	//e
      {0,1,1,0,0,0,1},	//f
      {0,1,1,0,0,1,0},	//g
      {0,1,1,0,0,1,1},	//h
      {0,1,1,0,1,0,0},	//i
      {0,1,1,0,1,0,1},	//j
      {0,1,1,0,1,1,0},	//k
      {0,1,1,0,1,1,1},	//l
      {0,1,1,1,0,0,0},	//m
      {0,1,1,1,0,0,1},	//n
      {0,1,1,1,0,1,0},	//o
      {0,1,1,1,0,1,1},	//p
      {1,0,0,0,1,0,0},	//q
      {1,0,0,0,1,0,1},	//r
      {1,0,0,0,1,1,0},	//s
      {1,0,0,0,1,1,1},	//t
      {1,0,0,1,0,0,0},	//u
      {1,0,0,1,0,0,1},	//v
      {1,0,0,1,0,1,0},	//w
      {1,0,0,1,0,1,1},	//x
      {1,0,0,1,1,0,0},	//y
      {1,0,0,1,1,0,1},	//z
      {1,0,0,1,1,1,0},	//0
      {1,0,1,0,0,0,1},	//1
      {1,0,1,0,0,1,0},	//2
      {1,0,1,0,0,1,1},	//3
      {1,0,1,0,1,0,0},	//4
      {1,0,1,0,1,0,1},	//5
      {1,0,1,0,1,1,0},	//6
      {1,0,1,0,1,1,1},	//7	
      {1,0,1,1,0,0,0},	//8
      {1,0,1,1,0,0,1},	//9
      {1,0,1,1,0,1,0},	//-
      {1,0,1,1,0,1,1},	//_
      {1,0,1,1,1,0,0},	//.
      {1,0,1,1,1,0,1},	//~
      {1,1,0,0,0,1,0},	//!
      {1,1,0,0,0,1,1},	//*
      {1,1,0,0,1,0,0},	//'
      {1,1,0,0,1,0,1},	//(
      {1,1,0,0,1,1,0},	//)
      {1,1,0,0,1,1,1},	//;
      {1,1,0,1,0,0,0},	//:
      {1,1,0,1,0,0,1},	//@
      {1,1,0,1,0,1,0},	//&
      {1,1,0,1,0,1,1},	//=
      {1,1,0,1,1,0,0},	//+
      {1,1,0,1,1,0,1},	//$
      {1,1,0,1,1,1,0},	//,
      {1,1,1,0,0,0,1},	///
      {1,1,1,0,0,1,0},	//?
      {1,1,1,0,0,1,1},	//#
      {1,1,1,0,1,0,0},	//[
      {1,1,1,0,1,0,1}	//]
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




   //-_.~!*'();:@&=+$,/?#[]
//   public static int[][] charCodes() {
//	   int i = 0;
//	   int n = 0;
//	   int[][] out = new int[charList.length()][7];
//	   while (i < charList.length()) {
//		   boolean isGood = true;
//		   String tryCode = Integer.toString(n, 2);
//		   // check if this code is good
//		   for (int j = 0; j < tryCode.length(); j++) {	// for each char in trycode
//			   	// if the next two are the same as this one
//			   	if (tryCode.charAt(j) == tryCode.charAt(j + 1) && tryCode.charAt(j) == tryCode.charAt(j + 2)) {
//			   		isGood = false;
//			   		break;
//			   	}
//		   }
//		   if (isGood) {
//			   // add tryCode to out
//			   for (int j = 6; j >= 0; j--) {
//				   if (j < 7 - tryCode.length()) {
//					   out[i][j] = 0;
//				   } else {
//					   
//				   }
//			   }
//		   }
//	   }
//   }

public static int charIndex(char c) {
    String charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~!*'();:@&=+$,/?#[]";
    for (int i = 0; i < charList.length(); i++) {
        if (charList.charAt(i) == c) {
           return i;
       }
   }
   return -1;
}

//   public static int[] 

//   public static boolean[] encode(String message) {
//	   
//   }
}
