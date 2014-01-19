
public class ClassificationArea {
	ClassificationTrack[] classificationTrack;
	
	public ClassificationArea(){
		classificationTrack = new ClassificationTrack[Constants.CLASSIFICATION_TRACKS_NUMBER];   //初始化track
		for(int i=0 ; i<classificationTrack.length ; i++)   //new 空間
			classificationTrack[i] = new ClassificationTrack();
	}
}
