package com.RecSys.Recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
This class contains contains our recommendation logic.
*/
public class MyRecommender {
	
	String ratedFileName;
	String implicitFeedbackFile;
	
	/**
	 * This constructor has to be called. It permanently stores the ratedFileName for the recommender.
	 * @param ratedFileName The path of the file that contains the ratings (Structure: SessionID, ProductID, Rating (e.g. 5.0))
	 */
	public MyRecommender (String ratedFileName){
		this.ratedFileName = ratedFileName;
		this.implicitFeedbackFile=implicitFeedbackFile;
	}

	/**
	 * This inner class builds the recommender.
	 *
	 */
	static class MyRecommenderBuilder implements RecommenderBuilder{
		
		/**
		 * This method builds the recommender and contains all parameters of the recommender. Thus optimizing the recommender can be done by changing following values in this method:
		 * Similarty type, neighbourhood type, similarity threshold, recommender type
		 * @param dataModel A DataModel that is based on the recommendations file
		 */
		public GenericUserBasedRecommender buildRecommender (DataModel dataModel) throws TasteException{
			
			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
			return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		}
	}
	/**
	 * This inner class builds the SVD Recommender 
	 * 
	 */
	static class SVDRecommenderBuilder implements RecommenderBuilder{
			
			/**
			 * This method builds the SVD recommender with a factorizer.
			 * @param dataModel A DataModel that is based on the recommendations file.
			 */
			public SVDRecommender buildRecommender (DataModel dataModel) throws TasteException{
				
	//			SVD Recommender
		    	UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
		    	UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, dataModel);
		    	Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
		    			
		    	ALSWRFactorizer factorizer = new ALSWRFactorizer(dataModel, 50, 0.065, 15);
		
		    	return new SVDRecommender(dataModel, factorizer);
		    	//
			}
		}

	/**
	 * This method creates a file that contains a certain amount of recommended products for some users based on the input file and the parameters that are defined in the inner class MyRecommenderBuilder. 
	 * @param numberOfRecommendations How many recommendations per user
	 * @param svdRecommender True if SVDRecommender should be used
	 * @throws Exception
	 * @return Path of recommendations file 
	 */
	public String createRecommendationsFile (int numberOfRecommendations,Boolean svdRecommender) throws Exception{
		if(!svdRecommender){
			String fileNameSubstring = ratedFileName.split("\\\\")[ratedFileName.split("\\\\").length-1];
			String outputFileName = ratedFileName.replace(fileNameSubstring, "Recommendations EuclideanDistance " + fileNameSubstring);
			
			DataModel model = new FileDataModel(new File(ratedFileName));
			 
			MyRecommenderBuilder builder = new MyRecommenderBuilder();
			UserBasedRecommender recommender = builder.buildRecommender(model);
			 
			FileInputStream ratedFile = new FileInputStream(new File(ratedFileName));
			BufferedReader ratedFileReader = new BufferedReader(new InputStreamReader(ratedFile));	
			PrintWriter recFile = new PrintWriter(outputFileName);
			String currentUser = "";
			String lastUser="";
			int counter=0;
			 
			String line = ratedFileReader.readLine();
			while (line != null) {
				
				currentUser= line.split(",")[0];
				
				//only print recommendations one time for every user
				if (!lastUser.equals(currentUser)){
					
					List<RecommendedItem> recommendations = recommender.recommend(Integer.parseInt(currentUser), numberOfRecommendations);
					 
					 for (RecommendedItem recommendation : recommendations) {
					 }
					 System.out.println(counter++);
				}
				
				lastUser=currentUser;
				line = ratedFileReader.readLine();
			 }
			 ratedFileReader.close();
			 recFile.close();
			
			return outputFileName;
			}
		else
		{
			String fileNameSubstring = implicitFeedbackFile.split("\\\\")[implicitFeedbackFile.split("\\\\").length-1];
			String outputFileName =implicitFeedbackFile.replace(fileNameSubstring, "SVD Recommendations " + fileNameSubstring);
			
			
			DataModel model = new FileDataModel(new File(implicitFeedbackFile));
			 
			SVDRecommenderBuilder builder = new SVDRecommenderBuilder();
			SVDRecommender recommender = builder.buildRecommender(model);
			 
			FileInputStream implicitFile = new FileInputStream(new File(implicitFeedbackFile));
			BufferedReader implicitFileReader = new BufferedReader(new InputStreamReader(implicitFile));	
			PrintWriter recFile = new PrintWriter(outputFileName);
			String currentUser = "";
			String lastUser="";
			 
			String line = implicitFileReader.readLine();
			int counter=0;
			while (line != null) {
				
				currentUser= line.split(",")[0];
				
				if (!lastUser.equals(currentUser)){
					
					List<RecommendedItem> recommendations = recommender.recommend(Integer.parseInt(currentUser), numberOfRecommendations);
					 
					 for (RecommendedItem recommendation : recommendations) {
						 recFile.println(currentUser + ";" + recommendation.getItemID()+ ";" + recommendation.getValue());
					 }
					 System.out.println(counter++);
				}
				
				lastUser=currentUser;
				line = implicitFileReader.readLine();
			 }
			 implicitFileReader.close();
			 recFile.close();
			
			return outputFileName;
		}
	}


	/**
	 * This Method prints the recommendations onto the console based on the input file and the parameters that are defined in the inner class MyRecommenderBuilder.
	 * @param svdRecommender True if SVD recommender should be used.
	 * @param numberOfRecommendations How many recommendations per User
	 * @throws Exception
	 */
	public void printRecommendationsToConsole (Boolean svdRecommender, int numberOfRecommendations) throws Exception{
		if (!svdRecommender){
		 DataModel model = new FileDataModel(new File(ratedFileName));
		 
		 MyRecommenderBuilder builder = new MyRecommenderBuilder();
		 UserBasedRecommender recommender = builder.buildRecommender(model);
		 
		 FileInputStream ratedFile = new FileInputStream(new File(ratedFileName));
		 BufferedReader ratedFileReader = new BufferedReader(new InputStreamReader(ratedFile));		
		 String line = ratedFileReader.readLine();
		
		 while (line != null) {
			 List<RecommendedItem> recommendations = recommender.recommend(Integer.parseInt(line.split(",")[0]), numberOfRecommendations);
			 
			 for (RecommendedItem recommendation : recommendations) {
				 System.out.println("Recommendation for " + line.split(",")[0] + ":" + recommendation);
			 }
			
			 line = ratedFileReader.readLine();
		 }
		 ratedFileReader.close();
		}
		else
		{
			DataModel model = new FileDataModel(new File(implicitFeedbackFile));
			
			 SVDRecommenderBuilder builder = new SVDRecommenderBuilder();
			SVDRecommender recommender = builder.buildRecommender(model);
			
			 FileInputStream implicitFeedback = new FileInputStream(new File(implicitFeedbackFile));
			 BufferedReader implicitFileReader = new BufferedReader(new InputStreamReader(implicitFeedback));		
			 String line = implicitFileReader.readLine();
			
			 while (line != null) {
				 List<RecommendedItem> recommendations = recommender.recommend(Integer.parseInt(line.split(",")[0]), numberOfRecommendations);
				 
				 for (RecommendedItem recommendation : recommendations) {
					 System.out.println("Recommendation for " + line.split(",")[0] + ":" + recommendation);
				 }
				
				 line = implicitFileReader.readLine();
			 }
			 implicitFileReader.close();
			}
		}
	
	
	/**
	 * Returns the evaluation of the recommender that is defined in the inner Class MyRecommenderBuilder and prints it on the console.
	 * @return a double representing the average absolute difference between the actual rating and the prediction
	 * @throws Exception
	 */
	public double evaluateRecommender() throws Exception {
		
		DataModel model = new FileDataModel(new File(ratedFileName));
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		return result;
		
	}
	/**
	 * merges the RecommendationsFiles of the SVDRecommender and the approach by ChoiEtAl.  Resulting File is not sorted. 
	 * @param SVDPath Path of the SVD recommendations file
	 * @param ChoiEtAlPath Path of the recommendations file with the approach by ChoiEtAl
	 * @return mergedRecommendationsFiles Path of the result file
	 * @throws Exception
	 */
	static public String mergeRecommendationsFiles (String SVDPath, String ChoiEtAlPath) throws Exception{
		
		String fileNameSubstring = SVDPath.split("\\\\")[SVDPath.split("\\\\").length-1];
		String intermediateFileName =SVDPath.replace(fileNameSubstring, "MergedChoiEtAl and " + fileNameSubstring);
		
		  FileInputStream SVDFile= new FileInputStream(new File(SVDPath));
		  BufferedReader brSVDFile = new BufferedReader(new InputStreamReader(SVDFile));
		  
		  FileInputStream ChoiEtAlFile= new FileInputStream(new File(ChoiEtAlPath));
		  BufferedReader brChoiEtAlFile = new BufferedReader(new InputStreamReader(ChoiEtAlFile));

		  PrintWriter mergedFile = new PrintWriter (intermediateFileName);

		  String line=brSVDFile.readLine();
		  while (line !=null) {
			  mergedFile.println(line);
			  line=brSVDFile.readLine();
		  }
		  brSVDFile.close();
		
		line=brChoiEtAlFile.readLine();
		  while (line !=null) {
			  mergedFile.println(line);
			  line=brChoiEtAlFile.readLine();
		  }
		  brChoiEtAlFile.close();
		  mergedFile.close();
		String outputFileName = ProcessData.sortFile(intermediateFileName, true, 2, ";");
		
		System.out.println(outputFileName);
		
		return outputFileName;
		  
	}
	/**
	 * Print the final recommendations for every user to a file by selecting the X highest ranked products for each user. Can be used directly after {@link #mergeRecommendationsFiles(String, String)} to combine both recommendation approaches.
	 * @param mergedRecommendationsFile Path of the file which contains the merged recommendations
	 * @param numberOfRecommendations How many recommendations per user?
	 * @return createFinalRecommendationsFile Path of the final recommendations file
	 * @throws Exception
	 */
	static public String createFinalRecommendationsFile (String mergedRecommendationsFile, int numberOfRecommendations) throws Exception{
		
		String fileNameSubstring = mergedRecommendationsFile.split("\\\\")[mergedRecommendationsFile.split("\\\\").length-1];
		String outputFileName =mergedRecommendationsFile.replace(fileNameSubstring, "!!!FINAL RECOMMENDATIONS " + numberOfRecommendations + " RecsPerSession " + fileNameSubstring);
		
		String [] tempArray;
		String currentSession;
		String lastSession = null;
		int counter = 0;
		
		  FileInputStream file= new FileInputStream(new File(mergedRecommendationsFile));
		  BufferedReader brFile = new BufferedReader(new InputStreamReader(file));
		  PrintWriter outputFile = new PrintWriter (outputFileName);
	
		  String line=brFile.readLine();
		  
		  while (line !=null) {
			  tempArray = line.split(";");
			  currentSession = tempArray[0];
			  
			  if (!currentSession.equals(lastSession)){
				  outputFile.println(line);
				  counter=0;
			  }else if (currentSession.equals(lastSession)&& counter < numberOfRecommendations){
				  outputFile.println(line);
			  }
			  
			  lastSession=currentSession;
			  counter++;
			  line=brFile.readLine();
		  }
		  brFile.close();
		  outputFile.close();
		  System.out.println(outputFileName);
		  return outputFileName;
	}


	/**
	 * Sees if the recommendationsFile corresponds to the mergedFile, i.e. if the recommended buys reflect the actual buys. Correctly predicted buys are printed in a new file. Incorrect ones are shown in the console.
	 * EDIT: Amount of correctly predicted buys will always be zero, because only products are recommended that have not been clicked on before i.e. that do not appear in the merged file.
	 * 
	 * @param recommendationsFileName path of the file with the recommendations
	 * @param mergedFileName path of the file that contains the merged clicks and buys
	 * @return A string that represents the path to the Evaluation File
	 */
	static public String evaluateRecommendationsFile (String recommendationsFileName, String mergedFileName) throws Exception{
		
		String fileNameSubstring = recommendationsFileName.split("\\\\")[recommendationsFileName.split("\\\\").length-1];
		String outputFileName = recommendationsFileName.replace(fileNameSubstring, "Evaluated " + fileNameSubstring);
		
		FileInputStream recFile = new FileInputStream(new File(recommendationsFileName));
		BufferedReader recFileReader = new BufferedReader(new InputStreamReader(recFile));	
		FileInputStream mergedFile = new FileInputStream(new File(mergedFileName));
		BufferedReader mergedFileReader = new BufferedReader(new InputStreamReader(mergedFile));
	
		PrintWriter evalFile = new PrintWriter(outputFileName);
		
		String userId;
		String productId;
		boolean recommendationCorrect = false; 
		
		String line = recFileReader.readLine();
		
		while(line !=null){
			
			userId = line.split(";")[0];
			productId = line.split(";")[1];
			String lineMerged = mergedFileReader.readLine();
			
			while(lineMerged != null){
				
				//check if recommendation was right, i.e. if there have been buys for the combination of userid and productid
				if (lineMerged.split(",")[0].equals(userId) 
						&& lineMerged.split(",")[1].equals(productId)
						&& Integer.parseInt(lineMerged.split(",")[5])>0){
					
					evalFile.println(userId + ";" + productId);
					recommendationCorrect = true; 
				}
				lineMerged = mergedFileReader.readLine();
			}
			
			if (!recommendationCorrect){
				System.out.println(userId + " + " + productId + " not correct");
			}
			
			recommendationCorrect=false;
			line = recFileReader.readLine();
			}
			
		
		recFileReader.close();
		mergedFileReader.close();
		evalFile.close();
		
		return outputFileName; 
		
	}
}
