import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;

public class PullBack {
	private Timer timer;   //控制PullBack的執行序
	private Timer timer2;
	Double allTimer;
	ClassificationArea classificationArea;
	OutboundArea outboundArea;
	BlockValues blockValues;   //檢查combination用
	int c=0;

	public PullBack(ClassificationArea c, OutboundArea o, Double t){
		this.classificationArea = c;
		this.outboundArea = o;
		allTimer = t;
		blockValues = new BlockValues();
		timer = new Timer();
		//timer2 = new Timer();
		timer.scheduleAtFixedRate(new PullBackTask(), 0, 100);	
		//timer2.scheduleAtFixedRate(new PullBackTask2(), 0, 10);
	}
	
	/**
	 * HumpTask類別為執行Hump的工作
	 *
	 */
	class PullBackTask extends TimerTask{
		int count=0;
		public void run(){
			/**
			 * Pull_back_1
			 */
			try{
				for(ClassificationTrack ct : classificationArea.classificationTrack){   //看哪一個classification track有block
					if(ct.isEmpty==false && ct.pullBackCheckingNow==false){   //若分類軌道有block
						ct.pullBackCheckingNow = true;
						boolean ifCombination = false;
						for(OutboundTrack ot : outboundArea.OutboundTrack){   //檢查outboundTrack是否有可以結合的
							if(ot.isEmpty==false && ot.pullBackCheckingNow==false){	
								ot.pullBackCheckingNow = true;
								ArrayList<String> tmp = ot.outPutTrain.blockVariety();   //看要出去的火車的種類	
								tmp.add(ct.existTrain.inBoundBlock.get(0));   //因為classification track上的種類都一樣，所以取第一個加入即可				
								if(blockValues.ifBlockCombination(tmp)   //如果classification track的block種類是可以結合的
									|| ot.outPutTrain.inBoundBlock.contains(ct.existTrain.inBoundBlock.get(0))){   ////或者有同樣block name可以丟的outboundTrack   
									ot.outPutTrain.inBoundBlock.addAll(ct.existTrain.inBoundBlock);
									ot.whichPullBack = 1;
									ct.isEmpty = true;								
									ct.existTrain = null;
									ifCombination = true;   //設為可結合
									break;
								}
							}
							ot.pullBackCheckingNow = false;
						}
						if(ifCombination==false){   //如果不能結合
							for(OutboundTrack ot : outboundArea.OutboundTrack){   //檢查所有的outboundTrack
								if(ot.isEmpty && ot.pullBackCheckingNow == false){   //如果是空的，從classification track 移到 outboundTrack
									ot.pullBackCheckingNow = true;
									ot.isEmpty = false;   //outboundTrack設為有東西
									ot.outPutTrain = new Train();
									ot.outPutTrain = ct.existTrain;   //將classificationTrack的火車丟過來
									ot.whichPullBack = 1;
									ct.isEmpty = true;   //丟過去outboundTrack後就可以讓classificationTrack的位置空出來
									ct.existTrain = null;   //並讓其上面train消失
									break;
								}
								ot.pullBackCheckingNow = false;
							}
						}
					}
					ct.pullBackCheckingNow=false;
				}
				
				/**
				 * 暫時先將輸出用count的方式寫
				 */
				for(OutboundTrack ot : outboundArea.OutboundTrack){
					if(ot.isEmpty==false && ot.outPutTrain.inBoundBlock.size()>=0){
						System.out.println(ot.outPutTrain.blockVariety() + "(" + ot.outPutTrain.inBoundBlock.size() + ")" + " Pull_Back_N" + ot.whichPullBack);
						c += ot.outPutTrain.inBoundBlock.size();
						ot.isEmpty = true;
						ot.outPutTrain = null;
						System.out.println(c);
					}
				}
				if(c==223){
					timer.cancel();
					timer2.cancel();
				}
			}
			catch(ConcurrentModificationException e){
				//e.printStackTrace();
			}
			catch(NullPointerException e){
				//e.printStackTrace();
			}
		}
	}
	
	class PullBackTask2 extends TimerTask{
		public void run(){
			/**
			 * Pull_Back_2
			 */
			try{
				for(ClassificationTrack ct : classificationArea.classificationTrack){   //看哪一個classification track有block
					if(ct.isEmpty==false && ct.pullBackCheckingNow==false){   //若分類軌道有block
						ct.pullBackCheckingNow=true;
						boolean ifCombination = false;
						for(OutboundTrack ot : outboundArea.OutboundTrack){   //檢查outboundTrack是否有可以結合的
							if(ot.isEmpty==false && ot.pullBackCheckingNow == false){	
								ot.pullBackCheckingNow = true;
								ArrayList<String> tmp2 = ot.outPutTrain.blockVariety();   //看要出去的火車的種類	
								tmp2.add(ct.existTrain.inBoundBlock.get(0));   //因為classification track上的種類都一樣，所以取第一個加入即可				
								if(blockValues.ifBlockCombination(tmp2)   //如果classification track的block種類是可以結合的
									|| ot.outPutTrain.inBoundBlock.contains(ct.existTrain.inBoundBlock.get(0))){   ////或者有同樣block name可以丟的outboundTrack   
									ot.outPutTrain.inBoundBlock.addAll(ct.existTrain.inBoundBlock);
									ot.whichPullBack = 2;
									ct.isEmpty = true;								
									ct.existTrain = null;
									ifCombination = true;   //設為可結合
									break;
								}
							}
							ot.pullBackCheckingNow = false;
						}
						if(ifCombination==false){   //如果不能結合
							for(OutboundTrack ot : outboundArea.OutboundTrack){   //檢查所有的outboundTrack
								if(ot.isEmpty && ot.pullBackCheckingNow == false){   //如果是空的，從classification track 移到 outboundTrack
									ot.pullBackCheckingNow = true;
									ot.isEmpty = false;   //outboundTrack設為有東西
									ot.outPutTrain = new Train();
									ot.outPutTrain = ct.existTrain;   //將classificationTrack的火車丟過來
									ot.whichPullBack = 2;
									ct.isEmpty = true;   //丟過去outboundTrack後就可以讓classificationTrack的位置空出來
									ct.existTrain = null;   //並讓其上面train消失
									break;
								}
								ot.pullBackCheckingNow = false;
							}
						}
					}
					ct.pullBackCheckingNow=false;
				}
				
				/**
				 * 暫時先將輸出用count的方式寫
				 */
				
				if(c==34130){
					timer.cancel();
					timer2.cancel();
				}
			}
			catch(ConcurrentModificationException e){
				//e.printStackTrace();
			}
			catch(NullPointerException e){
				//e.printStackTrace();
			}
		}
	}
}
