
public class ClassificationTrack {
	boolean isEmpty;
	Train existTrain;
	boolean pullBackCheckingNow;   //pullBack是否正在處理
	int numberOfBlock;   //紀錄此track是有幾個block所組成的
	double currentTime;   //用來儲存現在track的最後執行時間,執行時間是指被hump送來的時間或者是被pull_back最後拉的時間
	int whichPullBack;   //判斷是由哪一個pull_back拉的,有1和2

	public ClassificationTrack(){
		 isEmpty = true;
		 existTrain = new Train();
		 pullBackCheckingNow = false;
		 numberOfBlock = 0;
		 currentTime = 0;
		 whichPullBack = 0;
	}

}
