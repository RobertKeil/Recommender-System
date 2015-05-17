package com.RecSys.Recommender;

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
	 * Only buys are regarded. 
	 * @param clicks
	 * @param buys
	 * @param amountClicksBuys
	 * @return
	 */
	public static double algorithm4Step1(int clicks, int buys, int totalUserClicks, int totalUserBuys) {
		
		double  rating; 
			rating=  Math.log(((double) buys/(double) totalUserBuys)+1);
		return rating;
	}

	public static double algorithm4Step2(double absolutePreference, double maximumPreference) {
			
			double rating; 
				rating = Math.floor(5.0*(absolutePreference/maximumPreference));
			return rating;
	}
}