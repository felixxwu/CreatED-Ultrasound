
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.Desktop;
public class WaveGenerator{
    public static void main(String[] args)
    {
        try
        {
            int sampleRate = 44100;    // Samples per second
            double duration = 5.0;     // Seconds

            String outputFile = "output.wav";
            int bufferLength = 290;
            double basicFreq = 22430; //22430
            

            // Calculate the number of frames required for specified duration
            long numFrames = (long)(duration * sampleRate);

            // Create a wav file with the name specified as the first argument
            WavFile wavFile = WavFile.newWavFile(new File(outputFile), 1, numFrames, 16, sampleRate);
            // Create a buffer of 100 frame s
            double[][] buffer = new double[1][bufferLength];

            String url = "Hi-please-dont-fall-asleep";
            

            int[] half = Cypher.encode(url);

            int[] message = new int[half.length*2];
            for (int h = 0; h < half.length; h++) {
                message[h] = half[h];
                message[h + half.length] = half[h];
            }
            // Initialise a local frame counter
            long frameCounter = 0;
            double smoother = 0;
            int alternating = 0;
            for (int m : message) {

                long remaining = wavFile.getFramesRemaining();
                int toWrite = (remaining > bufferLength) ? bufferLength : (int) remaining;
                if (m == 0){
                    for (int s = 0 ; s<toWrite ; s++, frameCounter++)
                    { 
                        buffer[0][s] = 0;
                        smoother = 0;
                    }

                } else {
                    for (int s = 0 ; s<toWrite ; s++, frameCounter++) { 
                        double smootherInterval = toWrite/2;
//        			 	buffer[0][s]=alternating*smoother;
//        			 	if(alternating==0){alternating=1;}else{alternating=0;}
                        buffer[0][s] = Math.sin(2.0 * Math.PI * basicFreq * frameCounter / sampleRate) * smoother;
                        if (s<smootherInterval && smoother < 1){
                            smoother = Math.pow(s / smootherInterval, 3.5);;
                        }
                        if (s > toWrite - smootherInterval){
                            smoother = Math.pow((toWrite - s) / smootherInterval, 3.5);
                        // System.out.println(smoother);
                        }
                    }
                }
                wavFile.writeFrames(buffer, toWrite);
            }


         // Close the wavFile
            wavFile.close();

            Desktop.getDesktop().open(new File(outputFile));

        }
        catch (Exception e)
        {
           System.err.println(e);
       }
    }

    public static Double[] normalise(Double[] input) {
        // worst case for both
        double max = -1;
        double min = 1;
        for (int i = 0; i < input.length; i++) {
            // update max
            if (input[i] > max) {
                    max = input[i];
            }
            //update min
            if (input[i] < min) {
                    min = input[i];
            }
        }

        double scale = (max - min) / 2.0;
        double middle = (max + min) / 2.0;

        System.out.println(middle);
        System.out.println(scale);

        Double[] out = new Double[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = (input[i] - middle) / (double) scale;
        }

        return out;
    }


}
