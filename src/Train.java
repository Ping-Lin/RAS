import java.util.ArrayList;

public class Train {
	String day, inBoundTrainId, arrivalTime, departureTime, receivingTrackNo;
	ArrayList<String> inBoundBlock;
	Double currentTime;   //現在進行的時間
	
	public Train(){
		day = new String();
		inBoundTrainId = new String();
		arrivalTime = new String();
		departureTime = new String();
		inBoundBlock = new ArrayList<String>();
		currentTime = new Double(0.0f);
	}
	
	/**
	 * //將日期與到達時間整合後，統一轉成double型態
	 */
	double arrivalTimePlusDay(){
		return (Double.parseDouble(day)-1)*24+Double.parseDouble(arrivalTime);
	}
	
	/**
	 * 將block有的種類挑出來，放進一個字串陣列，
	 * 回傳值為ArrayList<String>
	 */
	ArrayList<String> blockVariety(){
		boolean theSame = false;   //判斷是否有一樣的
		ArrayList<String> tmp = new ArrayList<String>();   //暫存不同的block name
		for(String blockName : inBoundBlock){
			theSame = false;
			for(String tmpName : tmp){
				if(tmpName.equals(blockName)){   //檢查是否有一樣的
					theSame = true;
					break;
				}
			}
			if(theSame == false)
				tmp.add(blockName);
		}
		return tmp;
	}
}
