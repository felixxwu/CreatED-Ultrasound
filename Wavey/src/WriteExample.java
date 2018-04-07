import java.io.*;

public class WriteExample
{
   public static void main(String[] args)
   {
      try
      {
         int sampleRate = 44100;    // Samples per second
         double duration = 5.0;     // Seconds

         // Calculate the number of frames required for specified duration
         long numFrames = (long)(duration * sampleRate);

         // Create a wav file with the name specified as the first argument
         WavFile wavFile = WavFile.newWavFile(new File(args[0]), 1, numFrames, 16, sampleRate);
         int bufferLength = 300;
         // Create a buffer of 100 frames
         double[][] buffer = new double[1][bufferLength];
         int[] message = {0,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
         // Initialise a local frame counter
         long frameCounter = 0;
         double basicFreq=21000;
         double smoother =0;
         int alternating = 0;
         for(int m :message){
        	 
        	 long remaining = wavFile.getFramesRemaining();
             int toWrite = (remaining > bufferLength) ? bufferLength : (int) remaining;
             if(m==123){
            	 for (int s=0 ; s<toWrite ; s++, frameCounter++)
                 { 
                 		buffer[0][s] = 0;
                 		smoother=0;
                 }
            	
             }else{
        		 for (int s=0 ; s<toWrite ; s++, frameCounter++)
                 { 
        			 	double smootherInterval = toWrite/2;
        			 	buffer[0][s]=alternating;//*smoother;
        			 	if(alternating==0){alternating=1;}else{alternating=0;}
//                 		buffer[0][s] = Math.sin(2.0 * Math.PI * basicFreq * frameCounter / sampleRate)* smoother;
//                 		 if(s<smootherInterval && smoother<1){
//                 			 smoother=Math.pow(s/smootherInterval,1.3);;
//                 			 }
//                 		 if(s>toWrite-smootherInterval){
//                 			 smoother=Math.pow((toWrite-s)/smootherInterval,1.3);
//                 					 System.out.println(smoother);
//                 			 }
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
      }
      catch (Exception e)
      {
         System.err.println(e);
      }
   }
}
