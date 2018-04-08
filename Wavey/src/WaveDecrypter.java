import java.util.Arrays;

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
		int[] isPeak = new int[buffers.length];

		for (int b = 0; b < buffers.length; b++) {
			Double sum = 0.0;
			for (Double d : buffers[b]) {
				sum += d;
			}
			System.out.println(sum);

			if (sum > 10) {
				isPeak[b] = 1;
			} else {
				isPeak[b] = 0;
			}
		}

		return isPeak;
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
