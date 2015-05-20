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
 * @author Daniel and Robert
 *
 */
public class Main {

	/** Main method just used to process data and to create recommendations.
	 * Path Strings can be changed as desired. 
	 * @author Robert
	 */
	
	public static void main(String[] args) throws Exception {

		String startDir = System.getProperty("user.dir");

		String clicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\reduced1000th.csv";
		String buysFileName = startDir
				+ "\\data\\YooChoose Dataset\\yoochoose-buys.dat";
		String sortedClicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted reduced100th.csv";
		String sortedBuysFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted yoochoose-buys.dat";
		String aggregatedClicksFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated reduced100th NO TIME NO CATEGORY.csv";
		String aggregatedBuysFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated buys NO TIME.dat";
		String mergedFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated merged100th NO TIME.csv";
		String ratedFileName =  startDir
				+ "\\data\\YooChoose Dataset\\RatedChoiEtAl Sorted Aggregated merged100th NO TIME.csv";
		String recommendationsChoiEtAlFileName =  startDir
				+ "\\data\\YooChoose Dataset\\Recommendations RatedChoiEtAl Sorted Aggregated merged100th NO TIME.csv";
		String mergedFileNameBuys =  startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated merged100th NO TIME JUST BUYS.csv";

		String implicitClicksFile= startDir
				+ "\\data\\YooChoose Dataset\\Sorted Aggregated reduced100th NO TIME NO CATEGORY.csv";


		String recommendationsSVDFileName= startDir
				+ "\\data\\YooChoose Dataset\\SVD Recommendations Normalized.csv";

		String mergedCombinedRecommendationsFileName = startDir
				+ "\\data\\YooChoose Dataset\\MergedChoiEtAl and SVD Recommendations Normalized.csv";

		String sortedMergedCombinedRecommendationsFileName = startDir
				+ "\\data\\YooChoose Dataset\\Sorted MergedChoiEtAl and SVD Recommendations Normalized.csv";

		
		//		ProcessData.normalizeImplicitRecScore(startDir
//				+ "\\data\\YooChoose Dataset\\SVD Recommendations Sorted Aggregated reduced100th NO TIME NO CATEGORY.csv");
		
		
		MyRecommender recommender = new MyRecommender(ratedFileName, mergedFileName);
		String a = recommender.mergeRecommendationsFiles(recommendationsSVDFileName, recommendationsChoiEtAlFileName);
		recommender.createFinalRecommendationsFile(a, 1);
	}
}