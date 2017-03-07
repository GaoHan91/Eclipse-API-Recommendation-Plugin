package apiRecommendation.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class to load the knowledge based 
 * whenever the API Recommendation View is initialise
 */
public class Data {
	
	public static final Map<String, ArrayList<String>> BIGRAMTABLE = new HashMap<String, ArrayList<String>>();
	public static final Map<String, ArrayList<String>> REVERSEBIGRAMTABLE = new HashMap<String, ArrayList<String>>();
	
	public void loadBigram() throws IOException{
		
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream("API_File_bigram.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String line = "";
        while ((line = reader.readLine()) != null) {
        	 String api[] = line.split(",");
	         ArrayList<String> value = new ArrayList<String>();
	         
	         for(int i = 1; i < api.length; i++){
	        	 value.add(api[i]);
	         }
	         BIGRAMTABLE.put(api[0], value);
        }
        reader.close();
	
	}
	
	public void loadreversebigram() throws IOException{
		
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream("API_File_reversebigram.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String line = "";
        while ((line = reader.readLine()) != null) {
        	 String api[] = line.split(",");
	         ArrayList<String> value = new ArrayList<String>();
	         
	         for(int i = 1; i < api.length; i++){
	        	 value.add(api[i]);
	         }
	         REVERSEBIGRAMTABLE.put(api[0], value);
        }
        reader.close();
	   
	}
	
}
