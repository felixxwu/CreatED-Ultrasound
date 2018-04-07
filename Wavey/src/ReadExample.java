import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
//git test
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
			
			System.out.println(arrayA.length);
			System.out.println(arrayB.length);
			Double[] monoFrames = new Double[arrayA.length];
			for(int i = 0; i<arrayA.length;i++){
				monoFrames[i]=arrayA[i]+arrayB[i];
			}
			//print array
		
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
}
