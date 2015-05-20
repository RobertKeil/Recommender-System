package com.RecSys.Recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;

/**
 * This class preprocesses the data of the competition.
 * 
 * @author Robert and Daniel
 *
 */
public class ProcessData {
 static 	String startDir = System.getProperty("user.dir");

 /**
  * creates a dataset that contains every xth session of the original clicks dataset. 
  * @param interval determines in which interval a session should be kept in the file
  * @return the path of the reduced file
  * @throws Exception
  */
public static String reduceDataset (int interval) throws Exception {

		
		String clickFileName= "C:\\Users\\rober_000\\Documents\\yoochoose-clicks.dat";
		String reducedFileName= startDir
				+ "\\data\\YooChoose Dataset\\reduced" + interval + "th.csv";
		String lastSession = ""; 
		
		
		int counter=0; 

		FileInputStream clickFile= new FileInputStream(new File(clickFileName));
		BufferedReader brClick = new BufferedReader(new InputStreamReader(clickFile));
		PrintWriter reducedFile = new PrintWriter (reducedFileName);

		String tempClick = brClick.readLine();

		while (tempClick != null) {		

			String[] tempClickArr=tempClick.split(",");		
			
			//reset counter if interval has been reached
			if (!lastSession.equals(tempClickArr[0])){
				if (counter!=interval){
					counter++;
				}else{
					counter=0;
				}
			}
			
			//only print every xth session
			if (counter==interval){						
				reducedFile.println(tempClickArr[0] + "," + tempClickArr[1] + "," + tempClickArr[2] + "," + tempClickArr[3]);
			}
			
			lastSession = tempClickArr[0];
			tempClick=brClick.readLine();
		}
		brClick.close();
		reducedFile.close();
	
		return reducedFileName;
	}
	
	/**
	 * This method aggregates all records of the buys file that contain the same session id and same product id. 
	 * Several buys of the same user on the same product are summed up. This is why the timestamp is left out in the result
	 * @param buyFileName The path of the buys file that should be aggregated
	 * @param aggregatedFileName  The path of the file that should be created
	 */
	public static void aggregateBuys (String buyFileName, String aggregatedFileName) throws Exception {
		
	  String[] tempBuyArrNow;
	  String[] tempBuyArrLast;
	  int counter = 0; 
	  
	  FileInputStream buyFile= new FileInputStream(new File(buyFileName));
	  BufferedReader brBuy = new BufferedReader(new InputStreamReader(buyFile));

	  PrintWriter aggregatedFile = new PrintWriter (aggregatedFileName);
	  String tempBuy = brBuy.readLine();
	  tempBuyArrLast=tempBuy.split(",");

	  while (tempBuy !=null) {
		  tempBuyArrNow=tempBuy.split(",");

		  if (tempBuyArrNow[0].equals(tempBuyArrLast[0]) && tempBuyArrNow[2].equals(tempBuyArrLast[2])){
			  counter = counter + Integer.parseInt(tempBuyArrNow[4]);
		  }else{
			  aggregatedFile.println(tempBuyArrLast[0] + "," + tempBuyArrLast[2] + "," + tempBuyArrLast[3] + "," + counter);
			  counter = Integer.parseInt(tempBuyArrNow[4]);
		  }
		  tempBuyArrLast = tempBuyArrNow;
		  tempBuy=brBuy.readLine();
	  }
	  aggregatedFile.close();
	  brBuy.close();
	}
	
	public static void normalizeImplicitRecScore(String outputRecFile) throws Exception {
		
	String outputFileName= "C:\\Users\\Daniel\\workspace\\Recommender-System\\data\\YooChoose Dataset\\SVD Recommendations Normalized.csv";
	
		  
		  FileInputStream recFile= new FileInputStream(new File(outputRecFile));
		  BufferedReader brRec = new BufferedReader(new InputStreamReader(recFile));
		  String line= brRec.readLine();
		  PrintWriter outputFile = new PrintWriter (outputFileName);
		  String tempClick = brRec.readLine();
		
		  while (line!=null)
		  {
			  double normalizedValue;
			  
			 double realValue=Double.parseDouble(line.split(";")[2]);
			 if (realValue<=6){
				 
				 normalizedValue=(realValue/6)*5;
				 
				
				 
			 }
			 else
			 {
				 double result=realValue-6;
				 
				 if (result>8)
				 {
					 result=8;
					 
				 }
				 normalizedValue=(1-(0.12*result))*5;
				 
			 }
			  
			  outputFile.write(line.split(";")[0] + ";" + line.split(";")[1] + ";" + normalizedValue +"\n");
			  line=brRec.readLine();
		
			  
		  }
		  
		  
	
		  brRec.close();
		  outputFile.close();
		  
		
	}

	/**
	 * This method aggregates the clicks with same session id and same product id.
	 * Several clicks of the same user on the same product are summed up. This is why the timestamp is left out in the result
	 * @param clickFileName Path of the clicks file that should be aggregated
	 * @param aggregatedFileName Path of the buys file that should be created 
	 */
	public static void aggregateClicks (String clickFileName, String aggregatedFileName) throws Exception {
			  
		  String[] tempClickArrNow;
		  String[] tempClickArrLast;
		  int counter = 0; 
		  
		  FileInputStream clickFile= new FileInputStream(new File(clickFileName));
		  BufferedReader brClick = new BufferedReader(new InputStreamReader(clickFile));

		  PrintWriter aggregatedFile = new PrintWriter (aggregatedFileName);
		  String tempClick = brClick.readLine();
		  tempClickArrLast=tempClick.split(",");

		  while (tempClick !=null) {
			  tempClickArrNow=tempClick.split(",");

			  if (tempClickArrNow[0].equals(tempClickArrLast[0]) && tempClickArrNow[2].equals(tempClickArrLast[2])){
				  counter++;
			  }else{
				  aggregatedFile.println(tempClickArrLast[0] + "," + tempClickArrLast[2] + /* "," + tempClickArrLast[3] + */ "," + counter);
				  counter = 1;
			  }
			  tempClickArrLast = tempClickArrNow;
			  tempClick=brClick.readLine();
		  }
		  aggregatedFile.close();
		  brClick.close();
		}
	/**Make sure to adapt respective parameters in the methods called by sortFile. 
	 * Changes are based on number sorting priorities, line separator, number of columns and paths of read and print file
	 * @author Daniel
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String  sortFile (String fileName)throws Exception
	{
		String[][] arrayFile=ConvertFileTo2dArray(fileName);
		arrayFile=sort2dArray(arrayFile);
		return  convertFrom2dArrayToFile(arrayFile, fileName);
	}
	public static String[][] ConvertFileTo2dArray(String filePath) throws Exception
	{
		
		int noOfRows=getNoOfRows(filePath);
		String [][] arrayFile=new String[noOfRows][3];
    	
	    
    	
		  FileInputStream file= new FileInputStream(new File(filePath));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  String line=brFile.readLine();
		  
		  int arrayLine=0;
		  
		  
		 while (line!=null)
			 
		 {
			 arrayFile[arrayLine]=line.split(";");
			 
			 line=brFile.readLine();
		
			 arrayLine++;
			 
		 }
		 
		 
		 
		 
		 
		 return arrayFile;
		 
	}
	public static int getNoOfRows(String fileName) throws Exception
	{
		  FileInputStream file= new FileInputStream(new File(fileName));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  String line=brFile.readLine();
		  
		  int lineCounter=0;
		  
		  while(line!=null)
		  {
			  lineCounter+=1;
			  
			 line= brFile.readLine();
			  
			  
		  }
		  return lineCounter;
	}
		
	public static String [][] sort2dArray(String [][] file){
		 Arrays.sort(file, new Comparator<String[]>() {
			 public int compare(String[] array1, String[] array2) {
			int  x = Integer.valueOf(array1[0]);
			        int j = Integer.valueOf(array2[0]);
			        
			        if (x==j)
			        {
			        	
			        	double y=Double.parseDouble(array1[2]);
			        	double z=Double.parseDouble(array2[2]);
			        	
			        	
			        	return Double.compare(z, y);
			      
			        	
			        	
			   
			        }
			        else{
			      return  Integer.compare(x, j);
			        }
			      
			       
			    }
		    });
		

		return file;
		
		}
	
	public static String convertFrom2dArrayToFile(String [][] arrayFile, String fileName) throws Exception
	{
		String startDir = System.getProperty("user.dir");
		
		
		String sortedFileName=startDir+"\\data\\YooChoose Dataset\\Sorted " + fileName.split("\\\\")[fileName.split("\\\\").length-1];
	
		PrintWriter sortedFile= new PrintWriter (sortedFileName);
	
	
		for (int i=0;i<arrayFile.length;i++)
		{
		   sortedFile.println(arrayFile[i][0]+";"+arrayFile[i][1]+";"+arrayFile[i][2]);
		   
		}
	
		sortedFile.close();
	return sortedFileName;
	}
	public static String convertToRatings(String mergedFileName) throws Exception
	{
		String [] arrayLine;
		String ratedFileName= startDir+"\\data\\YooChoose Dataset\\Rated " + mergedFileName.split("\\\\")[mergedFileName.split("\\\\").length-1];
	
		PrintWriter ratedFile= new PrintWriter (ratedFileName);
		
		  FileInputStream file= new FileInputStream(new File(mergedFileName));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  int[][]totalUserClicksAndBuys=getTotalClicksAndBuysForEachSession(mergedFileName);

		  String line=brFile.readLine();
		  
		  while (line!=null){
				
			arrayLine=line.split(",");
			int counter=0;
			 
			while(counter<totalUserClicksAndBuys.length )
			{
				 if(totalUserClicksAndBuys[counter]!=null){
				 if (Integer.parseInt(arrayLine[0])==totalUserClicksAndBuys[counter][0]){
					 
					 		ratedFile.println(arrayLine[0]+"," + 
					 		arrayLine[1]+","+ 
					 		RatingAlgorithm.algorithm2(Integer.parseInt(arrayLine[3]), Integer.parseInt(arrayLine[5]), totalUserClicksAndBuys[counter][1],totalUserClicksAndBuys[counter][2]));
						 }
				 }	
				 counter++;
			 }
			 
			 line=brFile.readLine();
		 }
		 ratedFile.close();
		 
	return ratedFileName;
	}

	/**
	 * This method creates a files with implicit ratings. it follows the approach by Chio et al. 
	 * Modification of the approach by Choi et al: Also clicks will be regarded. 
	 * @param mergedFileName
	 * @return
	 * @throws Exception
	 */
	public static String convertToRatingsStudyByChoiEtAl(String mergedFileName) throws Exception
	{
		String [] arrayLine;
		String ratedFileName= startDir+"\\data\\YooChoose Dataset\\RatedChoiEtAl " + mergedFileName.split("\\\\")[mergedFileName.split("\\\\").length-1];
		
		int[][]totalUserClicksAndBuys=getTotalClicksAndBuysForEachSession(mergedFileName);
		
		String[][] absolutePreference = new String[getNoOfRows(mergedFileName)][3];
		
		//In the 100th dataset, there are 20000 different products + buffer = 25000
		String[][] highestAbsolutePreferencePerProduct = new String[2][25000];
		
		String currentSessionID;
		String currentProductID; 
		String currentAbsolutePreference;
		
		int arrayMaximumPreferenceCounter=0;
		int arrayAbsolutePreferenceCounter=0;
		int clicksBuysCounter;
		int iterationsCounter=0;
		int index;
		
		PrintWriter ratedFile= new PrintWriter (ratedFileName);
		FileInputStream file= new FileInputStream(new File(mergedFileName));
		BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  
		String line=brFile.readLine();
		  
		while (line!=null){
				
			arrayLine=line.split(",");
			clicksBuysCounter=0;
			
			//already print those entries where no product was bought
			if (Integer.parseInt(arrayLine[5])==0){
			ratedFile.println(arrayLine[0] + "," + arrayLine[1] + "," 
					+ RatingAlgorithm.algorithm4Step3(Integer.parseInt(arrayLine[3])));
			}
			
			// only use Choi et al's algorithm if there have been buys
			if (Integer.parseInt(arrayLine[5])>0){
			
				while(clicksBuysCounter<totalUserClicksAndBuys.length ){
					
					// IF still sessions contained AND sessionIDs are the same 
					 if(totalUserClicksAndBuys[clicksBuysCounter]!=null
							 && Integer.parseInt(arrayLine[0])==totalUserClicksAndBuys[clicksBuysCounter][0]){
						 
						 absolutePreference[arrayAbsolutePreferenceCounter][0]= currentSessionID =arrayLine[0];
						 absolutePreference[arrayAbsolutePreferenceCounter][1]= currentProductID =arrayLine[1];
						 absolutePreference[arrayAbsolutePreferenceCounter][2]= currentAbsolutePreference =Double.toString(RatingAlgorithm.algorithm4Step1(
								 Integer.parseInt(arrayLine[5]), 
								 totalUserClicksAndBuys[clicksBuysCounter][2]));
						 arrayAbsolutePreferenceCounter++;
						 
						 // see if productID is already listed in array
						 if(ArrayUtils.contains(highestAbsolutePreferencePerProduct[0], currentProductID)){
							 
							 index = ArrayUtils.indexOf(highestAbsolutePreferencePerProduct[0], currentProductID);
							 
							 // see if absolutePreference of current user is new maximum preference for product
							 if (Float.parseFloat(currentAbsolutePreference) > Float.parseFloat(highestAbsolutePreferencePerProduct[1][index])){
								 
								 highestAbsolutePreferencePerProduct[0][index]=currentProductID;
								 highestAbsolutePreferencePerProduct[1][index]=currentAbsolutePreference;
							 }
					 }else {
						 //If productID was not listed in Array, add the value to it
						 highestAbsolutePreferencePerProduct[0][arrayMaximumPreferenceCounter]=currentProductID;
						 highestAbsolutePreferencePerProduct[1][arrayMaximumPreferenceCounter]=currentAbsolutePreference;
						 
						 arrayMaximumPreferenceCounter++;
					 }
				 	}
					 clicksBuysCounter++;
				}
			}	
			System.out.println(iterationsCounter++);
			line=brFile.readLine();
		}
			
			//now all absolute Preferences are stored as well as the maximum preferences of every product. Now the second part of the calculation begins
			int counter=0;

			while(counter < absolutePreference.length && absolutePreference[counter][0]!=null){
				
				// search for productID
				index = ArrayUtils.indexOf(highestAbsolutePreferencePerProduct[0], absolutePreference[counter][1]);
				
				ratedFile.println(absolutePreference[counter][0] + "," + absolutePreference[counter][1] + "," 
				+ RatingAlgorithm.algorithm4Step2(Double.parseDouble(absolutePreference[counter][2]), Double.parseDouble(highestAbsolutePreferencePerProduct[1][index])));						
						
				System.out.println(counter++);
			}
			
		 ratedFile.close();
		return ratedFileName;
	}
	
	public static int [][] getTotalClicksAndBuysForEachSession(String mergedFileName) throws Exception{
		int noOfRows=getNoOfRows(mergedFileName);
		int[][]totalClicksAndBuys=new int[noOfRows][3];
		  FileInputStream file= new FileInputStream(new File(mergedFileName));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  String line=brFile.readLine();
		  
		 int sessionID=0;
		 int counter=0;
		 
		 while (line!=null)
		 {
			String arrayLine[]=line.split(",");
			 if (Integer.parseInt(arrayLine[0])==sessionID)
			 {
				 
				 totalClicksAndBuys[counter-1][1]+=Integer.parseInt(arrayLine[3]);
				 totalClicksAndBuys[counter-1][2]+=(Integer.parseInt(arrayLine[5]));
			 }
			 
			 else
			 {
				 totalClicksAndBuys[counter][0]=Integer.parseInt(arrayLine[0]);
				 totalClicksAndBuys[counter][1]=Integer.parseInt(arrayLine[3]);
				 totalClicksAndBuys[counter][2]=(Integer.parseInt(arrayLine[5]));
				 sessionID=totalClicksAndBuys[counter][0];
				 
				 counter++;
				 
			 }
			 line=brFile.readLine();
			 
		 }
	
		brFile.close();
		return totalClicksAndBuys;
	}
	
	
	/**
	 * This method joins a clicks file and a buys file. Both files have to be aggregated.
	 * Join happens on productID and sessionID 
	 * @param clickFileName path of the clicks file
	 * @param buyFileName path of the buys file
	 * @param mergedFileName path of the merged file to be created
	 * @throws Exception
	 */
	
	public static void joinDatasets(String clickFileName,String buyFileName, String mergedFileName) throws Exception{
			
		
//		  String buyFileName = "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\ProcessedDatasets\\aggregatedBuysNOTIME.dat";
//		  String clickFileName= "C:\\Users\\Robert\\Documents\\Studium\\Master\\Hiwi\\\\ProcessedDatasets\\aggregatedClicks10000thNOTIME.dat";
		String startDir = System.getProperty("user.dir");
		  
		  FileReader clickFile = new FileReader(new File(clickFileName));
		  BufferedReader brClick = new BufferedReader(clickFile);
		  FileInputStream buyFile= new FileInputStream(new File(buyFileName));
		  BufferedReader brBuy = new BufferedReader(new InputStreamReader(buyFile));

		  PrintWriter mergedFile = new PrintWriter (mergedFileName);

		  boolean entryFound = false; 
		  int counter=0;
		  String tempBuy;
		  String tempClick = brClick.readLine();

		  while (tempClick !=null) {
			  String[] tempClickArr=tempClick.split(",");
			  buyFile.getChannel().position(0);
			  tempBuy=brBuy.readLine();

			  while (tempBuy != null) {
				  String[] tempBuyArr=tempBuy.split(",");	

				  //if sessionid and itemid are same, print sessionid, itemid, category, clicks, price, quantity
				  if (tempBuyArr[0].equals(tempClickArr[0]) && tempBuyArr[1].equals(tempClickArr[1])) {		  
					  mergedFile.println(tempClickArr[0] + "," + tempClickArr[1] + "," + tempClickArr[2] + "," + tempClickArr[3] + ","
							  + tempBuyArr[2] + "," + tempBuyArr[3]);
					  entryFound = true; 
				  }
				  tempBuy=brBuy.readLine();
			  }

			  if (!entryFound){
				  mergedFile.println(tempClickArr[0] + "," + tempClickArr[1] + "," + tempClickArr[2] + "," + tempClickArr[3] + ",0,0");
			  }
			  entryFound = false;
			  tempClick = brClick.readLine();
			  
			  counter++;
			  if (counter%100==0){
				  System.out.println(counter);
			  }
		  }
		  brClick.close();
		  brBuy.close();
		  mergedFile.close();
	}

	/**
	 * This method prints all records that correspond to a session in  the clicks file but which contain a product id that did not appear in the clicks file
	 * EDIT: Output File is always empty because there are no buys of products that hve not been clicked on
	 * @param clickFileName
	 * @param buyFileName
	 * @param mergedFileName
	 * @throws Exception
	 */
		public static void joinDatasetsBuysWithoutCorrespondingProductID(String clickFileName,String buyFileName, String mergedFileName) throws Exception{
			
			  FileReader buyFile = new FileReader(new File(buyFileName));
			  BufferedReader brBuy = new BufferedReader(buyFile);
			  FileInputStream clickFile= new FileInputStream(new File(clickFileName));
			  BufferedReader brClick = new BufferedReader(new InputStreamReader(clickFile));
	
			  PrintWriter mergedFile = new PrintWriter (mergedFileName);
	
			  boolean entryFound = false; 
			  boolean sessionFound = false; 
			  int counter=0;
			  String tempClick;
			  String tempBuy = brBuy.readLine();
	
			  while (tempBuy !=null) {
				  clickFile.getChannel().position(0);
				  String[] tempBuyArr=tempBuy.split(",");
				  
				  tempClick=brClick.readLine();
				  
				  while (tempClick != null) {

					  String[] tempClickArr=tempClick.split(",");	
	
					  //if sessionid and itemid are same, do not print anything
					  if (tempClickArr[0].equals(tempBuyArr[0]) && tempClickArr[1].equals(tempBuyArr[1])) {		  
						  entryFound = true; 
						  sessionFound = true; 
					  }	
					  
					  if (tempClickArr[0].equals(tempBuyArr[0])) {		  
						  sessionFound = true; 
					  }
					  
					  tempClick=brClick.readLine();
				  }
				  
				  //Only print files if combination of Session and Product has not been found but sessionid has been found
				  if (!entryFound && sessionFound){
					  mergedFile.println(tempBuyArr[0] + ";" + tempBuyArr[1] + ";?;0;" + tempBuyArr[2] + ";" + tempBuyArr[3]);
				  }
				  entryFound = false;
				  sessionFound = false;
				  
				  counter++;
				  if (counter%1000==0){
					  System.out.println(counter);
				  }
				  tempBuy = brBuy.readLine();
			  }
			  brClick.close();
			  brBuy.close();
			  mergedFile.close();
		}
		
		
		public static void getTopKResults(int k,String fileName) throws Exception
		{
			
//			FileReader sortedRatingsFile= new FileReader(new File(fileName));
//			  BufferedReader sortedRatingsBufferedReader= new BufferedReader(sortedRatingsFile);
			  FileInputStream ratingsFile= new FileInputStream(new File(fileName));
			  BufferedReader sortedRatingsBufferedReader = new BufferedReader(new InputStreamReader(ratingsFile));
	String topKFileName=startDir
				+ "\\data\\YooChoose Dataset\\top " + k +"recommendations.csv";
			  PrintWriter topKWriter = new PrintWriter (topKFileName);
			  
			 String line=sortedRatingsBufferedReader.readLine();
			 
			
		while (line!=null)
		{
			
			String sessionId=line.split(";")[0];
			
		
			boolean moreThanTopKAvailable=true;
			
				for(int counter=1;counter<=k;counter++)
				{
					
					
					if (line.split(";")[0]==sessionId){
					topKWriter.write(line +"/n");
					}
					
					else{
						
						moreThanTopKAvailable=false;
						break;
					}
					
					line=sortedRatingsBufferedReader.readLine();
					
					
				}
			
				if (moreThanTopKAvailable)
				{
					while(line.split(";")[0]==sessionId)
					{
						line=sortedRatingsBufferedReader.readLine();
						
					}
				}
				
			
		
			  
		}
			
			  
			  
		}
}