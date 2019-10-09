import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Bigram {

	TreeMap<String, List<String>> map = new TreeMap<>();

	public Bigram(String string) {

		Scanner s = new Scanner(string);
		String first = "";
		if (s.hasNext()) { 
			// Store the first word in string
			first = s.next();
		}
		while (s.hasNext()) {
			// If there is a next value, store it in second
			String second = s.next();
			// Put first = second into the map
			if (!map.containsKey(first)) {
				// If map does not contain first as a key, create a new list and put second in it
				List<String> resultList = new ArrayList<String>();
				resultList.add(second);
				map.put(first, resultList );
			} else {
				// Otherwise, map must already contain first as a key
				// So we must add second to the list that already exists
				map.get(first).add(second);
			}
			// Before we continue the loop, move first forward
			first = second;
		}

	}

	public boolean check(String string) {

		// Create a new Bigram with the sample string
		Bigram sample = new Bigram(string);
		Object[] sampleKeyArray = sample.map.keySet().toArray(); // Create an array of the keys within the sample
		for (int i=0; i<sampleKeyArray.length; i++) {
			// We want to search for each key in sample and check if the result in map is the same as the result in sample
			if (!map.containsKey(sampleKeyArray[i])) {
				// If map doesn't contain the key, we can simply return false
				return false;
			} else {
				// Otherwise, we must check if the result of map contains the same Strings as the result for sample
				// Start by creating an array of the results of this key in sample
				Object[] sampleResultArray = sample.map.get(sampleKeyArray[i]).toArray();
				// Now create a list of the results for this key in map 
				List<String> mapResultList = map.get(sampleKeyArray[i]);
				// Now we can do our comparison
				for (int k=0; k<sampleResultArray.length; k++) {
					// If the set of results in map for the key does not contain every element in sampleResultArray,
					// we can return false
					if (!mapResultList.contains(sampleResultArray[k])) return false;
				}		
			}
		}
		// If we have reached this point, all the bigrams must be valid, so return true
		return true;
	}

	/**
	 * Generate the most likely phrase of length count that starts with word
	 * @param word
	 * @param count
	 * @return the array of those strings
	 */
	public String[] generate(String word, int count) {
		String maxResult = "";
		// Turn the result for each key in map into a single String
		// That String being the most common result for that key
		// We will need a new map to do this
		TreeMap<String, String> commonMap = new TreeMap<>();
		// Also create an array of the keys in map
		Object[] mapKeyArray = map.keySet().toArray();
		// Go through each key in map with a for loop
		for (int i=0; i<mapKeyArray.length; i++) {
			// Find the most common result for this key
			int maxCount = 0;
			int resultCount = 0;
			// Create an array of the results for this key
			Object[] results = map.get(mapKeyArray[i]).toArray();
			Arrays.sort(results);
			// Start with result as the first object in the array
			Object result = results[0];
			for (int j=0; j<results.length; j++) {
				// If the object we are looking at is the same as result, we want to increase resultCount
				if (((String) results[j]).compareTo((String) result) == 0) {
					resultCount++;
				} else {
					// Because the list is sorted, we know that if results[i] is not the same as result,
					// we can change result to whatever we are looking at
					result = results[j];
					resultCount = 1;
				}
				// if resultCount is greater than the current maxCount, we want to update maxCount
				// and store the most common result
				if (resultCount>maxCount) {
					maxCount = resultCount;
					maxResult = (String)result;
				}
			}
			// at this point, maxResult must be the most common result for this key, so add it to commonMap
			commonMap.put((String)mapKeyArray[i], maxResult);
		}
		// Now that we have our commonMap, we can start to put together our array
		// First put the Strings in a list
		List<String> list = new ArrayList<>();
		String current = word;
		list.add(word);
		for (int j=0; j<count-1; j++) {
			if (commonMap.get(current)!=null) {
				current = (commonMap.get(current));
				list.add(current);
			}
		}
		// Once we have our list, copy it into our array
		Object[] phraseKey = list.toArray();
		String[] phrase = new String[phraseKey.length];
		for (int k=0; k<phraseKey.length; k++) {
			phrase[k] = (String)phraseKey[k];
		}
		 
		return phrase;
	}

}
