import java.util.*;

public class WaveDecrypter {
//	public static void main(String[] args){
//		Double[] test = {9.0,1.0,9.0,4.0,3.0,5.0};
//		getClassification(test);
//		
//	}
	static int[] decript(Double[] positiveMono, int[] peaks, int bufferLength) {
		// contains buffers separately (some of them will correspond to peaks
		// and others to flats
		Double[][] buffers = new Double[positiveMono.length / bufferLength][bufferLength];

		for (int p = 0; p < peaks.length - 1; p++) {
			int from = 0;
			int to = positiveMono.length - 1;
			if (peaks[p] - (bufferLength / 2) > 0) {
				from = peaks[p] - (bufferLength / 2);
			}
			if (peaks[p] + (bufferLength / 2) < positiveMono.length) {
				to = peaks[p] + bufferLength / 2 - 1;
			}

			buffers[p] = Arrays.copyOfRange(positiveMono, from, to);
		}
		// Check whether buffers[i] is peak or flat using threshold for the sum
		// of all intensities of the frames in a buffer
		Double[] utility = new Double[buffers.length];
		Double averageUtility=0.0;
		System.out.println(averageUtility);
		System.out.println("hey");
		for (int b = 0; b < buffers.length; b++) {

			
			utility[b]= analyse(buffers[b]); //sum;
			System.out.println(averageUtility);
			averageUtility+=utility[b];
			//ANALYSE IS NOT WORKING WITH NOISY SAMPLES--------------
		}
		//averageUtility=averageUtility/buffers.length;
		int[] out= new int[utility.length];
		
		for(int i = 0; i<out.length;i++){
			
			if(utility[i]>averageUtility*1.75){
				out[i]=1;
			}else{
				out[i]=0;
			}
		}
//		System.out.println(averageUtility*1.75);
		return out;
		//return getClassification(utility); //ADVANCE CLASSIFICATION NOT WORKING
	}
	
	static int[] getClassification(Double[] utility){
		//applies k-clustering
		//ArrayList represent the indeces of utilies in utility
		ArrayList<Integer> beep = new ArrayList<Integer>();
		ArrayList<Integer> flat = new ArrayList<Integer>();
		
		//adds first utilities as cluster centres
		beep.add(new Integer(520));
		flat.add(new Integer(10));
		
		//average values of utilities in the cluster
		Double beepCenter=utility[0];
		Double flatCenter=utility[1];
		
		int changes=1;//number of elements that change cluster in each iteration
		while(changes!=0){ //keeps on looping until there're no changes in a whole iteration.
			changes=0;
		for(int i = 0;i<utility.length;i++){
			Double distanceToBeep=Math.abs(utility[i]-beepCenter);
			Double distanceToFlat=Math.abs(utility[i]-flatCenter);
			if(distanceToBeep<distanceToFlat && !beep.contains(i)){
				
				
				beep.add(i);
				if(flat.contains(i)){flat.remove(new Integer(i));}
				//calculates center
				Double sum=0.0;
				for(int c: beep){
					sum+=utility[c];
				}
				beepCenter=sum/beep.size();
//				System.out.println(Integer.toString(i)+ "beep");
//				System.out.println(changes);
				changes++;
			}else if(distanceToBeep>=distanceToFlat && !flat.contains(i)){
				
				flat.add(i);

				
				if(beep.contains(i)){beep.remove(new Integer(i));}
				//calculates center
				Double sum=0.0;
				//System.out.println(i);
				for(int c: flat){
					sum+=utility[c];
				}
				//System.out.println(flat.size());
				flatCenter=sum/flat.size();
//				System.out.println(Integer.toString(i)+ "flat");
//				System.out.println(changes);
				changes++;
				
			}else{
				
//			System.out.println(Integer.toString(i)+ "nothing");
//			System.out.println(changes);
		}
		}
			
		}
		
		int[] out = new int[utility.length];
		Arrays.fill(out, 0);
		
		for(Integer b:beep){
			if(b<out.length){out[b]=1;}
		}
		
		return out;
	}
	
	static Double analyse(Double[] buffer){
		
		int size = buffer.length;
		Double[] buffer1 = Arrays.copyOfRange(buffer, 0, size/5);
		Double[] buffer3 = Arrays.copyOfRange(buffer, 2*size/5, 3*size/5);
		Double[] buffer4 = Arrays.copyOfRange(buffer, 4*size/5, size);
		
		Arrays.sort(buffer3);
		Arrays.sort(buffer1);
		Arrays.sort(buffer4);
		
		
			
			//adds the 5 top values in buffer3
			Double max3=0.0;
			Double max1=0.0;
			Double max4=0.0;
	
			
			for(int i=buffer3.length-35;i<buffer3.length;i++){
				max3+=buffer3[i];
			
			}
			for(int i=0;i<buffer1.length-20;i++){
				max1+=buffer1[i];
			
			}
			for(int i=0;i<buffer4.length-20;i++){
				max4+=buffer4[i];
			
			}
			
//			System.out.println(max3); //sum of 10 max vals in max3
//			System.out.println(max1+max4); //sum of 5 peaks in max1 + sum of 5 peaks in max4
//			System.out.println("\n");
			

		return max3/((max1+max4)/70);
	}

	static int[] trim(int[] message, int[] startCode) {
		int[] trimmed = Arrays.copyOf(message, message.length);

		// cuts init part
		for (int i = 0; i < message.length - 6; i++) { // startCode = 0 1 1 1 1 1 1 1 0
			int[] isStartingCode = Arrays.copyOfRange(trimmed, i, i + startCode.length);
			if (Arrays.equals(startCode, isStartingCode)) {
				//if the initCode is found cut the code and everything preceding it
				trimmed = Arrays.copyOfRange(trimmed, i + startCode.length, trimmed.length);
				break;
			}
		}

		// cuts ending part
		for (int i = 0; i < message.length - 6; i++) { // *endCode = 0 1 1 1 1 1 1 1 0
			int[] isStartingCode = Arrays.copyOfRange(trimmed, i, i + startCode.length);
			if (Arrays.equals(startCode, isStartingCode)) {
				//if the endcode is found cut the endcode and everything after it
				trimmed = Arrays.copyOfRange(trimmed, 0, i);
				break;
			}
		}
		return trimmed;
	}
}
