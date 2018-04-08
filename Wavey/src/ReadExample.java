import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadExample
{
	public static void main(String[] args)
	{
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File(args[0]));

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			double[] buffer = new double[100 * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			int buffers=0;
			int turn=0;
			//framesA correspond to left channel and framesB correspond to the right one
			ArrayList<Double> framesA = new ArrayList<Double>();
			ArrayList<Double> framesB = new ArrayList<Double>();
			//aList.addAll(Arrays.asList(question2));
		
			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);
				

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
					
				}
				//System.out.println(Arrays.toString(buffer));
				for(double b: buffer){
					if(turn==0){
					framesA.add(new Double(b));
					turn=1;
					}else{
						framesB.add(new Double(b));
						turn=0;
					}
				}

			}
			while (framesRead != 0);
			
			
			Double[] arrayA = framesA.toArray(new Double[framesA.size()]);
			Double[] arrayB = framesB.toArray(new Double[framesB.size()]);
			
			//monoFrames: mono version of wav
			Double[] monoFrames = new Double[arrayA.length];
			for(int i = 0; i<arrayA.length;i++){
				monoFrames[i]=(arrayA[i]+arrayB[i])/2.0; //average of both channels
			}
			
			//neighbourDifference encoding difference in intensity between frame and neighbour
			Double[] neighbourDifference = new Double[monoFrames.length];
			for(int i =0;i<monoFrames.length-1;i++){
				neighbourDifference[i]=Math.abs(monoFrames[i]-monoFrames[i+1]);
			}
			//last element doesn't have a right neighbour
			neighbourDifference[neighbourDifference.length-1]=0.0;

			
		    int[] peaks = periodicPeaks(neighbourDifference,290,132032);
		    System.out.println(Arrays.toString(peaks));
			//writes array into txt file
			BufferedWriter writer = null;
		    try {

		        writer = new BufferedWriter(new FileWriter("array.txt"));
		        for ( int i = 0; i < monoFrames.length; i++)
		        {      
		          writer.write(String.valueOf(monoFrames[i]));
		          writer.newLine();
		      writer.flush();
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

			// Output the minimum and maximum value
			
			System.out.printf("Min: %f, Max: %f\n", min, max);
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
	
	
	//returns int[] corresponding to neighbourDiffrence's potential periodic peaks 
	//doesnt verify if they are picks or not.
	public static int[] periodicPeaks(Double[] nDifference,int bufferLength,int frames){
		//potentialPeaks[i] stores the sum of the intensity of nDifferences which indeces are n*i
		Double[] potentialPeaks=new Double[bufferLength];
		for(int p=0;p<bufferLength;p++){
			Double sum=0.0;
			for(int n = 0;n<nDifference.length;n=n+bufferLength){
				sum+=nDifference[n];
			}
			potentialPeaks[p]=sum;
		}
		//look for index corresponding to max sum
		int maxIndex=0;
		Double maxValue= Double.MIN_VALUE;
		for(int p=0;p<potentialPeaks.length;p++){
			if(potentialPeaks[p]>maxValue){
				maxValue=potentialPeaks[p];
				maxIndex=p;
			}
		}
		int[] periodicLocations = new int[(frames/bufferLength)+1];
		int currentLocationIndex=0;
		for(int p=maxIndex;p<nDifference.length;p=p+bufferLength){
			periodicLocations[currentLocationIndex]=p;
			currentLocationIndex++;
		}
		return periodicLocations;
	}
	// O(n)
	public static double[] normalise(double[] input) {
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

		double[] out = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			out[i] = (input[i] - middle) / (double) scale;
		}

		return out;
	}

	// input must be absolute !!!
	public static boolean isPeak(double[] input, int pos) {
		int sumWindow = 30;
		double threshold = 3;	// returns true if sumAfter is "threshold" times more than sumBefore
		if (pos < sumWindow) return false;	// discard peaks at the start (avoid negative index)
		double sumBefore = 0;	// sum of all values between -sumWindow and pos (values before)
		double sumAfter = 0;	// sum of all values between pos and sumWindow (values after)

		// summing before
		for (int i = (-1) * sumWindow; i < 0; i++) { sumBefore += input[pos + i]; }

		// summing after
		for (int i = 0; i<sumWindow; i++) { sumAfter += input[pos + i]; }

		// if ratio of after:before is > threshold, return true
		if (sumAfter / (double) sumBefore > threshold) return true;
		return false;

	}
}
