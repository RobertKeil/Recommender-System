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
 * This class contains the main method 
 *
 */
public class Main {

	/** Main method just used to process data and to create recommendations.
	 * Path Strings can be changed as desired. 
	 */
	
	public static void main(String[] args) throws Exception {

		String startDir = System.getProperty("user.dir");

		String clicksFileName = startDir
				+ "\\data\\0 Reduced Clicks\\reduced1000th.csv";
		String buysFileName = startDir
				+ "\\data\\yoochoose-buys.dat";
		String sortedClicksFileName = startDir
				+ "\\data\\1 Sorted\\Sorted reduced100th.csv";
		String sortedBuysFileName = startDir
				+ "\\data\\1 Sorted\\Sorted yoochoose-buys.dat";
		String aggregatedClicksFileName = startDir
				+ "\\data\\2 Aggregated\\Sorted Aggregated reduced100th NO TIME NO CATEGORY.csv";
		String aggregatedBuysFileName = startDir
				+ "\\data\\2 Aggregated\\Sorted Aggregated buys NO TIME.dat";
		String mergedFileName = startDir
				+ "\\data\\3 Merged\\Sorted Aggregated merged1000th NO TIME.csv";
		String ratedFileName =  startDir
				+ "\\data\\4 Rated\\Sorted RatedChoiEtAl Sorted Aggregated merged1000th NO TIME.csv";
		String ChoiEtAlPath =  startDir
				+ "\\data\\5 Recommendations\\Recommendations Sorted RatedChoiEtAl Sorted Aggregated merged100th NO TIME.csv";

		String implicitClicksFile= startDir
				+ "\\data\\2 Aggregated\\Sorted Aggregated reduced100th NO TIME NO CATEGORY.csv";

		String SVDPath= startDir
				+ "\\data\\5 Recommendations\\SVD Recommendations 100th Normalized.csv";

		String mergedCombinedRecommendationsFileName = startDir
				+ "\\data\\5 Recommenndations\\MergedChoiEtAl and SVD Recommendations Normalized.csv";

		String sortedMergedCombinedRecommendationsFileName = startDir
				+ "\\data\\5 Recommendations\\Sorted MergedChoiEtAl and SVD Recommendations Normalized.csv";

//		ProcessData.convertToRatingsStudyByChoiEtAl(mergedFileName);
		
		//		ProcessData.normalizeImplicitRecScore(startDir
//				+ "\\data\\YooChoose Dataset\\SVD Recommendations Sorted Aggregated reduced100th NO TIME NO CATEGORY.csv");
		

//		
//		ProcessData.sortFile(ratedFileName, true, 2, ",");	
		MyRecommender recommender = new MyRecommender(ratedFileName);
		recommender.createRecommendationsFile(3, false);
//		recommender.evaluateRecommender();
//		String mergedRecommendationsFile = recommender.mergeRecommendationsFiles(SVDPath, ChoiEtAlPath);
//		recommender.createFinalRecommendationsFile(mergedRecommendationsFile, 3);
	}
}