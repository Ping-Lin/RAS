
public class OutboundTrack {
	boolean isEmpty;
	Train outPutTrain;   //要出去的火車
	int whichPullBack;
	boolean pullBackCheckingNow;

	public OutboundTrack(){
		 isEmpty = true;
		 outPutTrain = new Train();
		 whichPullBack = 0;
		 pullBackCheckingNow = false;
	}

}
