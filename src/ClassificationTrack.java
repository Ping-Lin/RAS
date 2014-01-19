
public class ClassificationTrack {
	boolean isEmpty;
	Train existTrain;
	boolean pullBackCheckingNow;   //pullBack是否正在處理

	public ClassificationTrack(){
		 isEmpty = true;
		 existTrain = new Train();
		 pullBackCheckingNow = false;
	}

}
