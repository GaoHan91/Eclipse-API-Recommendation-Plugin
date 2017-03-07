package apiRecommendation.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Check whether the API used is an outlier
 * Add it to the warningList if it is an outlier
 */
public class APIWarningMessage {
	
	ArrayList<String> keysInfo;
	TreeMap<Integer, Integer> warningList = new TreeMap<Integer,Integer>();
	
	public APIWarningMessage(ArrayList<String> keysInfo) {
		this.keysInfo = keysInfo;
	}
	
	public void checkAPI(){
	
		for(int i=0; i<keysInfo.size()-1; i++){
			
			String[] firstkey = keysInfo.get(i).split(",");
			String[] secondkey = keysInfo.get(i+1).split(",");
			List<String> output = new ArrayList<String>();
			
			String key1 = removePart(firstkey[2]);
			String key2 = removePart(secondkey[2]);
			
			if(Data.BIGRAMTABLE.containsKey(key1)){
				
				output = Data.BIGRAMTABLE.get(key1);
				
				for(String result: output){
					
					String[] parts = result.split(":");
					String key = parts[0];
					String percentage = parts[1].trim();
					Double percentageValue = Double.parseDouble(percentage);
					
					if(key.contains(key2)){
						if(percentageValue < 1.0){
							warningList.put(Integer.parseInt(secondkey[0]), Integer.parseInt(secondkey[1]));
						}
						break;
					}		
					
				}
				
			}	
			
		}
		
	}	
	
	public TreeMap<Integer, Integer> getWarningList(){
		return warningList;
	}
	
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
