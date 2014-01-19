
public class Test {
	public static void main(String[] args) {
		Double allTimer = new Double(0.0);   //總時間軸
		ReceivingArea receivingArea = new ReceivingArea();
		ClassificationArea classificationArea = new ClassificationArea();
		OutboundArea outboundArea = new OutboundArea();
		Hump hump = new Hump(receivingArea, classificationArea, allTimer);
		PullBack pullBack = new PullBack(classificationArea, outboundArea, allTimer);
		
		
	}
	
}
