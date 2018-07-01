package Platform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class LeskAlgorithm {
	public static void main(String args[]){
//		PINE 
//		1. kinds of evergreen tree with needle-shaped leaves
//		2. waste away through sorrow or illness
		
//		CONE 
//		1. solid body which narrows to a point
//		2. something of this shape whether solid or hollow
//		3. fruit of certain evergreen trees
		
		// PINE
		HashMap<String,String> pineList = new HashMap<String,String>();
		pineList.put("1", "kinds of evergreen tree with needle-shaped leaves");
		pineList.put("2", "waste away through sorrow or illness");
		
		// CONE
		HashMap<String,String> coneList = new HashMap<String,String>();
		coneList.put("1", "solid body which narrows to a point");
		coneList.put("2", "something of this shape whether solid or hollow");
		coneList.put("3", "fruit of certain evergreen trees");
		ArrayList<String> result = LeskAlgorithmWSD(pineList, coneList);
		System.out.println(result);
		
	}
	
	public static ArrayList<String> LeskAlgorithmWSD(
			HashMap<String, String> senseList1, HashMap<String, String> sensenList2){
		ArrayList<String> matchedSensePair = new ArrayList<String>();
		// load stopword
		ArrayList<String> stopWordList = loadStopWordList("stopword.txt");
		
		// create unique vocabulary for each sense
		HashMap<String, ArrayList<String>> uniqueSenseList1 = new HashMap<String, ArrayList<String>>();
		uniqueSenseList1 = createUniqueVocabulary(senseList1, stopWordList);
		
		HashMap<String, ArrayList<String>> uniqueSenseList2 = new HashMap<String, ArrayList<String>>();
		uniqueSenseList2 = createUniqueVocabulary(sensenList2, stopWordList);
		
		matchedSensePair = overlap(uniqueSenseList1, uniqueSenseList2, matchedSensePair);
		
		return matchedSensePair;
	}
	
	public static ArrayList<String> overlap(HashMap<String, ArrayList<String>> uniqueSenseList1,
			HashMap<String, ArrayList<String>> uniqueSenseList2, ArrayList<String> matchedSensePair){
		String maxLabel = "";
		double overlapCount = 0.0;
		
		for(Map.Entry<String, ArrayList<String>> list1 : uniqueSenseList1.entrySet()){	
			double count = 0.0;
			for(Map.Entry<String, ArrayList<String>> list2 : uniqueSenseList2.entrySet()){
				for(int i=0;i<list1.getValue().size();i++){
					for(int j=0;j<list2.getValue().size();j++)
						if(list1.getValue().get(i).equals(list2.getValue().get(j))){
							System.out.println(list1.getValue().get(i));
							count++;
						}
				}
			}
			
			if(count>overlapCount) {
				maxLabel = list1.getKey();
				overlapCount = count;
			}
		}
		matchedSensePair.add(maxLabel+"@"+String.valueOf(overlapCount));
		
		maxLabel = "";
		overlapCount = 0.0;
		for(Map.Entry<String, ArrayList<String>> list2 : uniqueSenseList2.entrySet()){	
			double count = 0.0;
			for(Map.Entry<String, ArrayList<String>> list1 : uniqueSenseList1.entrySet()){
				for(int i=0;i<list2.getValue().size();i++){
					for(int j=0;j<list1.getValue().size();j++) {
						if(list2.getValue().get(i).equals(list1.getValue().get(j))){
							System.out.println(list1.getValue().get(i));
							count++;
						}
					}
				}
			}
			
			if(count>overlapCount) {
				maxLabel = list2.getKey();
				overlapCount = count;
			}
		}
		matchedSensePair.add(maxLabel+"@"+String.valueOf(overlapCount));
		
		return matchedSensePair;
	}
	
	public static HashMap<String, ArrayList<String>> createUniqueVocabulary(
			HashMap<String, String> senseList, ArrayList<String> stopWordList){
		HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
		for(Map.Entry<String, String> sense : senseList.entrySet()){
			String key = sense.getKey();
			String sentence = sense.getValue();
			ArrayList<String> tokens = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(sentence);
			while(tokenizer.hasMoreTokens()){
				String str = tokenizer.nextToken();
				if(!stopWordList.contains(str))
					tokens.add(str);
			}
			temp.put(key, tokens);
		}
		
		return temp;
	}
	
	public static ArrayList<String> loadStopWordList(String filePath){
		ArrayList<String> stopwordList = new ArrayList<String>();
		try{
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready()){
				String str = br.readLine().trim();
				if(str!=null&&!str.isEmpty())
					stopwordList.add(str);
			}
		}catch(Exception e){
			System.out.println("can not read stopword");
		}
		
		return stopwordList;
	}
}
