import java.util.*;

public class WaveDecrypter {
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
		Double sum = 0.0;
		for (int b = 0; b < buffers.length; b++) {
			
			for (Double d : buffers[b]) {
				sum += d;
			}
			
			utility[b]=analyse(buffers[b]);
		}
		//System.out.println(sum/buffers.length);
		//int[] test = {1,1,0};
		System.out.println(Arrays.toString(utility));
		return getClassification(utility);
	}
	
	static int[] getClassification(Double[] utility){
		//applies k-clustering
		//ArrayList represent the indeces of utilies in utility
		ArrayList<Integer> beep = new ArrayList<Integer>();
		ArrayList<Integer> flat = new ArrayList<Integer>();
		
		//adds first utilities as cluster centres
		beep.add(0);
		flat.add(1);
		
		//average values of utilities in the cluster
		Double beepCenter=utility[0];
		Double flatCenter=utility[1];
		
		int changes=1;//number of elements that change cluster in each iteration
		while(changes!=0){ //keeps on looping until there're no changes in a whole iteration.
		
		for(int i = 0;i<utility.length;i++){
			changes=0;
			Double distanceToBeep=Math.abs(utility[i]-beepCenter);
			Double distanceToFlat=Math.abs(utility[i]-flatCenter);
			if(distanceToBeep<distanceToFlat && !beep.contains(i)){
				//System.out.println(Integer.toString(i)+ "beep");
				beep.add(i);
				if(flat.contains(i)){flat.remove(i);}
				//calculates center
				Double sum=0.0;
				for(int c: beep){
					sum+=utility[c];
				}
				beepCenter=sum/beep.size();
				changes++;
			}else if(distanceToBeep>=distanceToFlat && !flat.contains(i)){
				//System.out.println(Integer.toString(i)+ "flat");
				flat.add(i);
				if(beep.contains(i)){beep.remove(i);}
				//calculates center
				Double sum=0.0;
				for(int c: flat){
					sum+=utility[c];
				}
				flatCenter=sum/flat.size();
				changes++;
			}else{
			//System.out.println(Integer.toString(i)+ "nothing");
		}
		}
			
		}
		int[] out = new int[utility.length];
		Arrays.fill(out, 0);
		for(Integer b:beep){
			out[b]=1;
		}
		return out;
	}
	static Double analyse(Double[] buffer){
		int size = buffer.length;
		Double[] buffer1 = Arrays.copyOfRange(buffer, 0, size/5);
		Double[] buffer2 = Arrays.copyOfRange(buffer, size/5, 2*size/5);
		Double[] buffer3 = Arrays.copyOfRange(buffer, 2*size/5, 3*size/5);
		Double[] buffer4 = Arrays.copyOfRange(buffer, 3*size/5, 4*size/5);
		Double[] buffer5 = Arrays.copyOfRange(buffer, 4*size/5, size);
		Double sum1=0.0;Double sum2=0.0;Double sum3=0.0;Double sum4=0.0;Double sum5 = 0.0; 
		for(int b = 0;b<buffer1.length;b++){
			sum1+=buffer1[b];
			sum2+=buffer2[b];
			sum3+=buffer3[b];
			sum4+=buffer4[b];
			sum5+=buffer5[b];
		}
		//the higher it is the most likely it is that there's a peak
		Double utility = sum3-sum1-sum5;
		//System.out.println(utility);
		//System.out.println(Double.toString(sum1)+','+ sum2 + ',' + sum3 + ',' + sum4 + ',' + sum5);
		
		
		
		return utility;
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
