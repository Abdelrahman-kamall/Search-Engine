package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import eg.edu.alexu.csd.filestructure.btree.IBTree;
import eg.edu.alexu.csd.filestructure.btree.ISearchEngine;
import eg.edu.alexu.csd.filestructure.btree.ISearchResult;

public class MySearchEngine implements ISearchEngine {
	IBTree<String, ArrayList<ISearchResult>> bT = new MyBTree<String, ArrayList<ISearchResult>>(0);
	
	public MySearchEngine(int t) {
		bT = new MyBTree<String, ArrayList<ISearchResult>>(t);
	}

	

	@Override
	public void indexWebPage(String filePath) {
		domParser(filePath,true);
	}

	@Override
	public void indexDirectory(String directoryPath) {
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			this.indexWebPage(file.getAbsolutePath());
		}
	}

	@Override
	public void deleteWebPage(String filePath) {
		domParser(filePath,false);
		
	}

	@Override
	public List<ISearchResult> searchByWordWithRanking(String word) {
		ArrayList<ISearchResult> result = bT.search(word);
		
		
		ordering_basedon_freq(result);
		return result;
	}

	@Override
	public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
		ArrayList<HashMap<String,Integer>> temp = new  ArrayList<HashMap<String , Integer>>();
		ArrayList<ISearchResult> result = new  ArrayList<ISearchResult>();
		
		sentence=sentence.toLowerCase();
		String[] words = sentence.split("\\s");
		
		ArrayList<ISearchResult> first_row = (ArrayList<ISearchResult>) searchByWordWithRanking(words[0]);
		
		for(int counter =0 ;counter < words.length ; counter++) {
			HashMap<String,Integer> hash = new HashMap<String,Integer>();
			turn_into_hash((ArrayList<ISearchResult>) searchByWordWithRanking(words[counter]),hash);
			temp.set(counter,hash);
		}
		
		
		 for(int counter =0 ; counter < first_row.size() ; counter++){
			String Id =first_row.get(counter).getId();
			int minRank = first_row.get(counter).getRank();
			for(int counter1 =1 ; counter1 <temp.size() ; counter1++) {
				int minRank_new = temp.get(counter1).get(Id);
				if(minRank_new<minRank) {
					minRank=minRank_new;
				}
				
			}
			ISearchResult sr = new MySearchResult();
			sr.setId(Id);
			sr.setRank(minRank);
			result.add(sr);
			
		}
		ordering_basedon_freq(result);
		return result;
	}
	
	void turn_into_hash(ArrayList<ISearchResult> arr , HashMap<String,Integer> hash) {
		
		for(int counter =0; counter <arr.size() ; counter++) {
			hash.put(arr.get(counter).getId(), arr.get(counter).getRank());
		}
		
	}
	
	void ordering_basedon_freq(ArrayList<ISearchResult> arr) {
		
		for(int counter1 =0 ;counter1 < arr.size() ; counter1++) {
			int min_freq = arr.get(counter1).getRank();
			int min_ind = counter1;
			for(int counter2 = counter1+1 ; counter2<arr.size() ; counter2++) {
				int min_freq_new = arr.get(counter1).getRank();
				if(min_freq_new < min_freq) {
					min_freq = min_freq_new;
					min_ind = counter2;
				}
			}
			ISearchResult sr = arr.get(min_ind);
			arr.remove(min_ind);
			arr.add(min_ind, sr);
		}
	}
	private void domParser(String filePath , boolean flag) {
		try {
			File inputFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("doc");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String id = eElement.getAttribute("id");
					String data = eElement.getChildNodes().item(0).getTextContent().toString();
					String[] str = data.split("[\" \"|\n]");
					for (String s : str) {
						if (s.compareTo("") != 0) {
							if(flag) {
							insertion(s, id);
							}else {
								Delete_by_word(s,id);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertion(String s, String id) {
		if (this.bT.search(s) != null) {
			boolean flag = false;
			ArrayList<ISearchResult> temp = this.bT.search(s);
			for (int i = 0; i < temp.size(); i++) {
				if (temp.get(i).getId().compareTo(id) == 0) {
					temp.get(i).setRank(temp.get(i).getRank() + 1);
					flag = true;
					break;
				}
			}
			if (!flag) {
				ISearchResult sResult = new MySearchResult();
				sResult.setId(id);
				sResult.setRank(1);
				temp.add(sResult);
			}
		} else {
			ISearchResult sResult = new MySearchResult();
			sResult.setId(id);
			sResult.setRank(1);
			ArrayList<ISearchResult> aL = new ArrayList<ISearchResult>();
			aL.add(sResult);
			this.bT.insert(s, aL);
		}

	}
	
	boolean Delete_by_word(String word,String id) {
		ArrayList<ISearchResult> arr_sr = bT.search(word);
		for(int counter=0;counter<arr_sr.size();counter++) {
			if(id.equals(arr_sr.get(counter).getId())) {
				arr_sr.remove(counter);
				return true;
			}
			if(arr_sr.size() ==0) {
				return bT.delete(word);
			}
			if(counter==arr_sr.size()-1 ) {
				return false;
			}
		}
		
		return false;
		
	}

}
