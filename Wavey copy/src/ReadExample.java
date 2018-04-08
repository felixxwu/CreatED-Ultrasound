import java.io.*;
import java.util.*;

public class ReadExample {

	static long sampleRate;
	public static void main(String[] args) {

		try {	
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File("pablo.wav"));
            int bufferLength = 290;

			Double[] monoFrames = readToMonoDouble(wavFile);

			Double[] positiveMonoFrames = new Double[monoFrames.length];

			for (int i = 0; i < monoFrames.length; i++){ positiveMonoFrames[i] = Math.abs(monoFrames[i]); }
			
			//neighbourDifference encodes difference in intensity between frame and neighbour
			Double[] neighbourDifference = new Double[monoFrames.length];
			for (int i = 0;i < monoFrames.length - 1; i++) {
				neighbourDifference[i] = Math.abs(monoFrames[i] - monoFrames[i+1]);
			}
			//last element doesn't have a right neighbour
			neighbourDifference[neighbourDifference.length - 1] = 0.0;
			
			exportWav(neighbourDifference, "neighbor.wav", sampleRate);
			exportWav(monoFrames,"mono.wav", sampleRate);
			exportWav(positiveMonoFrames,"positiveMono.wav", sampleRate);


			//calculates peaks
		    int[] peaks = periodicPeaks(neighbourDifference, bufferLength);
		    //allows visualization of periodicPeaks
		    Double[] peakVisualizer = new Double[neighbourDifference.length];
		    peakVisualizer[0]=1.0;
		    Arrays.fill(peakVisualizer, 0.0);
		    for (int p: peaks){
		    	peakVisualizer[p]=1.0;
		    }
		    exportWav(peakVisualizer,"peakVisualizer.wav", sampleRate);
		    		
		    //DECRIPTS ARRAY
		    //TRY WITH positiveMono and neighbourDifference
		    int[] decrypted = WaveDecrypter.decript(positiveMonoFrames, peaks, bufferLength);
		    
		    int[] trimmedDecrypted = WaveDecrypter.trim(decrypted,Cypher.startCode);
		    String answer = Cypher.decode(trimmedDecrypted);
		    System.out.println(answer);
		    ArrayList<Double> decryptedOut = new ArrayList<Double>();
		    
		    //print decrypted array to wave (hi-lo )
		    Double[] decryptedBuffer = new Double[bufferLength];
		    for (int d: decrypted){
		    	if (d == 1){
		    		Arrays.fill(decryptedBuffer, 0.0);
		    	} else {
		    		Arrays.fill(decryptedBuffer, 1.0);
		    	}
		    	decryptedOut.addAll(Arrays.asList(decryptedBuffer));
		    }
		    decryptedOut.addAll(Arrays.asList(decryptedBuffer));
	    	Double[] decryptedArray = new Double[decryptedOut.size()];
	    	decryptedOut.toArray(decryptedArray);
		    exportWav(decryptedArray,"decrypted.wav", sampleRate);
		    
//			exports array to txt file
			BufferedWriter writer = null;
		    try {

		        writer = new BufferedWriter(new FileWriter("decrypted.txt"));
		        for ( int i = 0; i < decrypted.length; i++)
		        {
		            writer.write(String.valueOf(decrypted[i]));
		            writer.write(",");
		            //writer.flush();
		        }

		    } catch(IOException ex) {
		        ex.printStackTrace();
		    } finally{
		        if(writer!=null){
		        	writer.close();
		        }  
		    }
		    
			
			// Close the wavFile
			wavFile.close();

			//exportWav(peaks, "peaks.wav");
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
    public static Double[] readToMonoDouble(WavFile wavFile) throws IOException, WavFileException {

        sampleRate = wavFile.getSampleRate();
        // Display information about the wav file
        wavFile.display();

        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();

        // Create a buffer of 100 frames
        double[] buffer = new double[100 * numChannels];

        int framesRead;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int buffers = 0;
        int turn = 0;
        //framesA correspond to left channel and framesB correspond to the right one
        ArrayList<Double> framesA = new ArrayList<Double>();
        ArrayList<Double> framesB = new ArrayList<Double>();
        //aList.addAll(Arrays.asList(question2));
        
        do {
            // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, 100);

            // Loop through frames and look for minimum and maximum value
            for (int s = 0 ; s < framesRead * numChannels ; s++)
            {
                if (buffer[s] > max) max = buffer[s];
                if (buffer[s] < min) min = buffer[s];
                
            }
            //System.out.println(Arrays.toString(buffer));
            for (double b : buffer) {
                if (turn == 0){
                    if (numChannels == 2){
                        framesA.add(new Double(b));
                    } else {
                        framesA.add(new Double(b));
                        framesB.add(new Double(b));
                    }
                turn = 1;
                } else {
                    if (numChannels == 2) {
                    framesB.add(new Double(b));
                    } else {
                        framesA.add(new Double(b));
                        framesB.add(new Double(b));
                    }
                    turn = 0;
                }
            }
        } while (framesRead != 0);
        
        //CREATES ARRAYS ENCONDING WAVE.
        Double[] arrayA = framesA.toArray(new Double[framesA.size()]);
        Double[] arrayB = framesB.toArray(new Double[framesB.size()]);
        
        //monoFrames: mono version of wav
        Double[] monoFrames = new Double[arrayA.length];
        for (int i = 0; i < arrayA.length; i++) {
            monoFrames[i] = (arrayA[i] + arrayB[i]) / 2.0; //average of both channels
        }
        System.out.printf("Min: %f, Max: %f\n", min, max);
        
        return monoFrames;
    }
	
	
	//returns int[] corresponding to neighbourDiffrence's potential periodic peaks 
	//doesnt verify if they are picks or not.
	public static int[] periodicPeaks(Double[] nDifference,int bufferLength){
		//potentialPeaks[i] stores the sum of the intensity of nDifferences which indeces are n*i
		Double[] potentialPeaks=new Double[bufferLength];
		for (int p=0;p<bufferLength;p++){
			Double sum=0.0;
			for (int n = p+4;n<nDifference.length-5;n=n+bufferLength){
				Double[] maxPeaks=Arrays.copyOfRange(nDifference, n-4, n+5);
					Double max = maxPeaks[0]; //gets the max value of the window of values
					for (Double m: maxPeaks){
						if(m>max){max=m;}
					}
						
					sum+=max;
				}
				
				
			potentialPeaks[p]=sum;
			}
		
		
		//the max index of potentialPeaks correspond to the offset in period that the peak is at.
		//look for  index corresponding to max sum
		int maxIndex=0;
		Double maxValue= Double.MIN_VALUE;
		for (int p=0;p<potentialPeaks.length;p++){
			if(potentialPeaks[p]>maxValue){
				maxValue=potentialPeaks[p];
				maxIndex=p;
			}
		}
		//calculates the actual locations of the peaks
		int[] periodicLocations = new int[(nDifference.length/bufferLength)+1];
		int currentLocationIndex=0;
		for (int p=maxIndex;p<nDifference.length;p=p+bufferLength){
			periodicLocations[currentLocationIndex]=p;
			currentLocationIndex++;
		}
		return periodicLocations;
	}

	public static void exportWav(Double[] input, String file, long sampleRate) {
	      try {
				double duration = input.length / (double) sampleRate;
				long numFrames = (long)(duration * sampleRate);
				WavFile wavFile = WavFile.newWavFile(new File(file), 1, numFrames, 16, sampleRate);
				double[][] buffer = new double[2][1];
				for (int i = 0; i < numFrames; i++) {
					buffer[0][0] = input[i];
					buffer[1][0] = input[i];
					wavFile.writeFrames(buffer, 1);
				}
	 
			    wavFile.close();
		  }
		  catch (Exception e)
		  {
		     System.err.println(e);
		  }
	}
	
	public static Double[] smoothOut(Double[] input, int smooth) {
		Double[] out = new Double[input.length];
		for (int i = 0; i < input.length; i++) {
			if (i < smooth || i > input.length - smooth) {
				out[i] = 0.0;
			} else {
				// sum neighboring values
				double avg = 0;
				for (int j = (-1) * smooth; j < smooth; j++) {
					avg += Math.abs(input[i + j]);
				}
				out[i] = (avg / ((double) smooth * 2.0)) - 1.0;
			}
		}
		return out;
	}
}















