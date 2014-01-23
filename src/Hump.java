import java.util.Timer;
import java.util.TimerTask;
/**
 * Hump類別為控制ReceivingArea和ClassificationArea的Engine
 * @author Ping
 *
 */
public class Hump {
	private Timer timer;   //控制hump的執行序
	Double allTimer;
	ReceivingArea receivingArea;
	ClassificationArea classificationArea;
	Train  tmp;   //分配用
	int count;   //計算是否要結束hump,當count為3時結束
	Double humpCurrentTime;
	
	public Hump(ReceivingArea r, ClassificationArea c, Double t){
		this.receivingArea = r;
		this.classificationArea = c;
		allTimer = t;
		timer = new Timer();
		timer.scheduleAtFixedRate(new HumpTask(), 0, 10);
		count=0;
		tmp = new Train();
		humpCurrentTime = new Double(Double.MAX_VALUE);
	}
	
	/**
	 * 此部分為Hump運作的過程
	 */
	class HumpTask extends TimerTask{
		boolean isExistBlockName;   //判斷track是否有存在blockName
		public void run(){
			tmp = receivingArea.firstComeTrain(humpCurrentTime);
			if(tmp!=null){   //有車子來，Hump開始分配
				humpCurrentTime = tmp.currentTime + (Constants.HUMP_RATE*(1.0f*tmp.inBoundBlock.size()));
				/**
				 * 測試用
				 */
				/*System.out.println("Press enter to continue...");
				new java.util.Scanner(System.in).nextLine();
				System.out.println("!!!"+ humpCurrentTime);*/
				
				
				count = 0;   //倒數重計
				for(int i=0 ; i<tmp.inBoundBlock.size() ; i++){   //要分配的block
					isExistBlockName = false;
					for(ClassificationTrack ct : classificationArea.classificationTrack){
						if(ct.isEmpty == false){   //找尋可以插入的block
							if(ct.existTrain.inBoundBlock.get(0).equals(tmp.inBoundBlock.get(i))){
								ct.existTrain.inBoundBlock.add(tmp.inBoundBlock.get(i));
								isExistBlockName = true;
								break;
							}
						}
					}
					if(isExistBlockName == false){   //如果找不到同樣名子的block Name，就看有沒有新軌道可以插入
						for(ClassificationTrack ct : classificationArea.classificationTrack){
							if(ct.isEmpty){   //若classification track有空位的話
								ct.isEmpty = false;
								ct.existTrain = new Train();
								ct.existTrain.inBoundBlock.add(tmp.inBoundBlock.get(i));
								break;
							}
						}
					}
				}
				//System.out.println(classificationArea.classificationTrack[5].existTrain.inBoundBlock);   //測試用
			}
			else{   //沒火車可以拉，Hump結束,做個檢查當數完3秒都沒東西拉時就可以結束
				try {
					Thread.sleep(1000);   //等待1秒
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(count==3){
					timer.cancel();
					System.out.println("Hump is over.");
				}
			}
		}
	}
	
}
