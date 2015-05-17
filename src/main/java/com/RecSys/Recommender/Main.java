package com.RecSys.Recommender;

import java.io.*;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * Hello world!
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		String startDir = System.getProperty("user.dir");

		String clicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\reduced1000th.csv";
		String buysFileName = startDir
				+ "\\data\\YooChoose Dataset\\yoochoose-buys.dat";
		String sortedClicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted reduced1000th.csv";
		String sortedBuysFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted yoochoose-buys.dat";
		String aggregatedClicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated reduced1000th NO TIME NO CATEGORY.csv";
		String aggregatedBuysFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated buys NO TIME.dat";
		String mergedFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated merged100th NO TIME.csv";
		String ratedFileName =  startDir
				+ "\\data\\YooChoose Dataset\\Rated Sorted Aggregated merged100th NO TIME.csv";
		String recommendationsFileName =  startDir
				+ "\\data\\YooChoose Dataset\\Recommendations Rated Sorted Aggregated merged1000th NO TIME.csv";
		String mergedFileNameBuys =  startDir
				+ "\\data\\YooChoose Dataset\\ Sorted Aggregated merged10000th NO TIME JUST BUYS.csv";
		
		
		
//		processData.reduceDataset(100);
//		 processData.sortFile(clicksFileName);
//		 processData.sortFile(buysFileName);	
		 ProcessData.aggregateClicks(sortedClicksFileName, aggregatedClicksFileName);
		// processData.aggregateBuys(sortedBuysFileName, aggregatedBuysFileName);
//		 ProcessData.joinDatasetsBuysWithoutCorrespondingProductID(aggregatedClicksFileName, aggregatedBuysFileName, mergedFileNameBuys);
//		processData.convertToRatings(mergedFileName);

//		ProcessData.convertToRatingsStudyByChoiEtAl(mergedFileName);
		
		// processData.aggregateBuys();

		// processData.reduceDataset();

		// processData.aggregateClicks();

		// processData.joinDatasets();

		// getIntersectionClickBuySession();

		// ---------------------------------

 
//		 MyRecommender.printRecommendations (ratedFileName); 
		 
//		MyRecommender recommender = new MyRecommender(ratedFileName, mergedFileName);
//		
//		recommender.evaluateRecommendationsFile(recommendationsFileName);
//		
		
//		double accuracy = recommender.evaluateRecommender();
//		 System.out.println(accuracy);
		 
	}
}