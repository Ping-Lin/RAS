
public class OutboundArea {
	OutboundTrack[] OutboundTrack;
	public OutboundArea(){
		OutboundTrack = new OutboundTrack[Constants.DEPARTURE_TRACKS_NUMBER];   //初始化track
		for(int i=0 ; i<OutboundTrack.length ; i++)   //new 空間
			OutboundTrack[i] = new OutboundTrack();
	}
}
