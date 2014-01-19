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
	
	public Hump(ReceivingArea r, ClassificationArea c, Double t){
		this.receivingArea = r;
		this.classificationArea = c;
		allTimer = t;
		timer = new Timer();
		timer.scheduleAtFixedRate(new HumpTask(), 0, 10);
	}
	
	/**
	 * 此部分為Hump運作的過程
	 */
	class HumpTask extends TimerTask{
		boolean isExistBlockName;   //判斷track是否有存在blockName
		public void run(){
			tmp = receivingArea.firstComeTrain();
			if(tmp!=null){   //有車子來，Hump開始分配
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
			else{   //沒火車可以拉，Hump結束
				timer.cancel();
				System.out.println("Hump is over.");
			}
		}
	}
}
