package apiRecommendation.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class APIRetrieval {
	
	List<String> finalresult = new ArrayList<String>();
	String beforeKey;
	String afterKey;
	
    final String emptyResult = "Sorry, no API recommendation is retrieve from our database";
    final int recommendNo = 5;
	boolean beforeKeyFilled = false;
	boolean afterKeyFilled = false;
	
	/**
	 * This is to retrieval the recommendation based
	 * on the latest API called capture
	 */
	public List<String> getResult (List<String> methodListBeforeOffset, List<String> methodListAfterOffset) throws IOException {
		
		if(methodListBeforeOffset.size() >= 1){
			beforeKey = methodListBeforeOffset.get(methodListBeforeOffset.size()-1);
			beforeKey = removePart(beforeKey);
			beforeKeyFilled = true;
		}
		
		if(methodListAfterOffset.size() >= 1){
			afterKey = methodListAfterOffset.get(0);
			afterKey = removePart(afterKey);
			afterKeyFilled = true;
		}
		
		List<String> bigramResult = new ArrayList<String>();
		List<String> reversebigramResult = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		 
		if(beforeKeyFilled && afterKeyFilled && !beforeKey.equals("NA") && !afterKey.equals("NA")){
			 
			 Map<String,Double> sortedResult = new TreeMap<String, Double>();
			 
			 bigramResult = getBigramRecommendation(beforeKey);
			 reversebigramResult = getreverseBigramRecommendation(afterKey);
			
			 if(!bigramResult.get(0).equals(emptyResult) &&
					 !reversebigramResult.get(0).equals(emptyResult)){
				 sortedResult = getCombinedResult(bigramResult, reversebigramResult);
				 for(Map.Entry<String, Double> entry : sortedResult.entrySet()){
					 result.add(entry.getKey() + " : " + entry.getValue());
				 }
				 result = extractResult(result);
				 finalresult.add("Latest API detected");
				 finalresult.add("API detected before cursor position - " + beforeKey ); 
				 finalresult.add("API detected after cursor position - " + afterKey);
				 finalresult.add("");
				 finalresult.add("Recommendation based on latest API detected");
				 finalresult.addAll(result);
				 return finalresult;
				
			 }	
			 
		}
		
		if(beforeKeyFilled && !beforeKey.equals("NA")){
			
			bigramResult = getBigramRecommendation(beforeKey);
			if(!bigramResult.get(0).equals(emptyResult)){
				result = extractResult(bigramResult);
				finalresult.add("Latest API detected");
				finalresult.add("API used before cursor position - " + beforeKey ); 
				finalresult.add("");
				finalresult.add("Recommendation based on latest API detected");
				finalresult.addAll(result);
				return finalresult;
			}
			
		}
		
		if(afterKeyFilled && !afterKey.equals("NA")){
			
			reversebigramResult = getreverseBigramRecommendation(afterKey);
			if(!reversebigramResult.get(0).equals(emptyResult)){
				result = extractResult(reversebigramResult);
				finalresult.add("Latest API detected");
				finalresult.add("API used after cursor position - " + afterKey); 
				finalresult.add("");
				finalresult.add("Recommendation based on latest API detected");
				finalresult.addAll(result);
				return finalresult;
			}
			
		}
		
		if(afterKey.equals("NA") || beforeKey.equals("NA")){
			finalresult.add("do nothing");
			return finalresult;
		}
		
	    return finalresult;
	}
	
	/**
	 * Make sure that only the top result is display
	 * to the user 
	 */
	private List<String> extractResult(List<String> result) {
		List<String> extractresult = new ArrayList<String>();
		if(result.size() > recommendNo){
	    	for(int i = 0; i < recommendNo; i++){
	    		extractresult.add(result.get(i) + "%");
	    	}
	    }else{
	    	for(int i = 0; i < result.size(); i++){
	    		extractresult.add(result.get(i) + "%");
	    	}
	    }	
		return extractresult;
	}
	
	/**
	 * Combine the result of both the bigram and reversebigram
	 * Recalculate the percentage
	 */
	private Map<String, Double> getCombinedResult(List<String> bigramResult, List<String> reversebigramResult){
		
		 TreeMap<String,Double> combineResult = new TreeMap<String, Double>();
		 Map<String, Double> sortedMap = new TreeMap<String, Double>(); 
		 
		 for(int i = 0; i < bigramResult.size(); i++){
			 String[]parts = bigramResult.get(i).split(" : "); 
			 Double value = Double.parseDouble(parts[1]);
			 value = (double) value / 2; 
			 combineResult.put(parts[0] , value);
		 }
		 
		 for(int j = 0; j < reversebigramResult.size(); j++){
			 String[]parts = reversebigramResult.get(j).split(" : ");
			 Double value = Double.parseDouble(parts[1]);
			 value = (double) value / 2; 
			 
			 if(combineResult.containsKey(parts[0])){
				 Double containValue  = combineResult.get(parts[0]);
				 value = containValue + value;
			 }
			 
			 value = value * 100;
			 double formatValue = Math.round(value);
             formatValue = formatValue/100;
			 combineResult.put(parts[0] , formatValue);
			 
		 }
		 
		 sortedMap =  sortByValue(combineResult);
		 return sortedMap;
	}
	
	/**
	 * Sorted the percentage according to descending order
	 */
	private Map<String, Double> sortByValue(TreeMap<String, Double> combineResult) {
		return combineResult.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	            		  Map.Entry::getKey, 
	            		  Map.Entry::getValue, 
	            		  (e1, e2) -> e1, 
	            		  LinkedHashMap::new
	            		  ));
	}
	
	/**
	 * Retrieve the recommendation based on the key
	 * @param afterKey - key after offset
	 * @return reverse bigram Recommendation
	 */
	private List<String> getreverseBigramRecommendation(String afterKey) {
		 List<String> reversebigramRecommendation = new ArrayList<String>();
		 if(Data.REVERSEBIGRAMTABLE.containsKey(afterKey)){
			 reversebigramRecommendation = Data.REVERSEBIGRAMTABLE.get(afterKey);
		 }else{
			 reversebigramRecommendation.add(emptyResult);
		 }
		 return reversebigramRecommendation;
	}
	
	/**
	 * Retrieve the recommendation based on the key
	 * @param beforeKey  - key before offset
	 * @return bigram Recommendation
	 */
	private List<String> getBigramRecommendation(String beforeKey) {
		List<String> bigramRecommendation = new ArrayList<String>();
		if(Data.BIGRAMTABLE.containsKey(beforeKey)){
			 bigramRecommendation = Data.BIGRAMTABLE.get(beforeKey);
		}else{
			 bigramRecommendation.add(emptyResult);
		}
		return bigramRecommendation;
	}
	
	/**
	 * Remove the type of the object
	 */
	private String removePart(String key){
		
		if(key.contains("<")){
    		key = key.replaceAll("<.*?>", "");
    	}
    	 
    	if(key.contains(">")){
    		key = key.replaceAll(">", "");
    	}
    	 
		return key;
	}
	
}
