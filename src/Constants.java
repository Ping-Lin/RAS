/**
 * Constants 介面定義了一些常數的數值
 * @author Ping
 *
 */
public interface Constants {
	/**
	 * case 1
	 */
	int RECEIVING_TRACKS_NUMBER = 4;
	int RECEIVING_TRACKS_CAPACITY = 40;
	int CLASSIFICATION_TRACKS_NUMBER = 6;
	int CLASSIFICATION_TRACKS_CAPACITY = 50;
	int DEPARTURE_TRACKS_NUMBER = 4;
	int DEPARTURE_TRACKS_CAPACITY = 40;
	double HUMP_RATE = 2.5/60;   // 每拉一台車，單位:小時
	double HUMP_INTERVAL = 8/60; //單位:小時
	double TECHNICAL_INSPECTION_TIME = 30/60;   //單位:小時
	
	/*
	 * case 5
	 */
/*	int RECEIVING_TRACKS_NUMBER = 10;
	int RECEIVING_TRACKS_CAPACITY = 185;
	int CLASSIFICATION_TRACKS_NUMBER = 42;
	int CLASSIFICATION_TRACKS_CAPACITY = 60;
	int DEPARTURE_TRACKS_NUMBER = 7;
	int DEPARTURE_TRACKS_CAPACITY = 207;
	double HUMP_RATE = 1/60;   // 每拉一台車，單位:小時
	double HUMP_INTERVAL = 2/60; //單位:小時
	double TECHNICAL_INSPECTION_TIME = 45/60;   //單位:小時
*/}
