package com.RecSys.Recommender;


/**
 * This class contains different algorithms to derive implicit ratings.
 * They are all called by a method in ProcessData.java (convertToRatings)
 * Algorithm 2 and 3 were just experiments. We did not use them in the end. 
 * The most important algorithm is algorithm 4 which is divied into three steps that are called in method convertToRatingsStudyByChoiEtAl in ProcessData.java.
 */
public  class RatingAlgorithm {

	private static final int weightClicks = 1;
	private static final int weightBuys = 5;


	public static float algorithm2(int clicks, int buys, int amountClicks,int amountBuys) {

		float rating = clicks * weightClicks + buys * weightBuys;
		float denominator=(amountClicks*weightClicks)+(amountBuys*weightBuys);
		float fraction = (rating / denominator)*5 ;

		return fraction;
	}
	
	// rating is computed based on certain ranges
	public static float algorithm3(int clicks, int buys, int amountClicksBuys) {

		int rating = clicks * weightClicks + buys * weightBuys;

		// range has to be float in order for mahout to work later on
		float range;

		if (rating < 3) {
			range = 0;
		} else if (rating < 6) {
			range = 1;
		} else if (rating < 9) {
			range = 2;
		} else if (rating < 12) {
			range = 3;
		} else if (rating < 15) {
			range = 4;
		} else 
			range = 5;

		return range;
	}

	/**
	 * This method implements the CF-algorithm of following paper: http://www.sciencedirect.com/science/article/pii/S156742231200018X#
	 * Modification: Also clicks are regarded
	 * @param buys Amount of buys of the current user of the current product
	 * @param totalUserBuys total amount of buys of the user
	 */
	public static double algorithm4Step1(int buys, int totalUserBuys) {
		
		double  rating; 
			rating=  Math.log(((double) buys/(double) totalUserBuys)+1);
		return rating;
	}
	/**
	 * Preference for buys will be between 3 and 5. See paper http://www.sciencedirect.com/science/article/pii/S156742231200018X#
	 * @param absolutePreference The preference of the current user for the current product
	 * @param maximumPreference The maximum preference that any user had for the current product
	 */
	public static double algorithm4Step2(double absolutePreference, double maximumPreference) {
			
			double rating; 
				rating = 2+Math.ceil(3.0*(absolutePreference/maximumPreference));
			return rating;
	}
	
	/**
	 * Preference for clicks only will be either 1 or 2
	 * @param clicks Total clicks on product by current user
	 */
	public static double algorithm4Step3 (int clicks) {
		
		double rating; 
			if (clicks < 4)
				rating = 1.0;
			else
				rating = 2.0;
			
		return rating;
}
}