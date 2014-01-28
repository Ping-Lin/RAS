import java.util.ArrayList;

public class ClassificationArea {
	ClassificationTrack[] classificationTrack;
	BlockValues blockValues;   //檢查combination用
	ArrayList<Train> finishTrain;   //紀錄離開classification area的train資訊,當作紀錄用
	
	public ClassificationArea(){
		classificationTrack = new ClassificationTrack[Constants.CLASSIFICATION_TRACKS_NUMBER];   //初始化track
		for(int i=0 ; i<classificationTrack.length ; i++)   //new 空間
			classificationTrack[i] = new ClassificationTrack();
		blockValues = new BlockValues();
		finishTrain = new ArrayList<Train>();   //初始化
	}
	
	/**
	 * 現在從classification area的combine方式是從第一個track開始掃,如果可以combine的就把他抓出去combine	
	 * @param pullBackNum
	 * @param outboundArea
	 */
	public void classifiedTrain(int pullBackNum, OutboundArea outboundArea){   
		boolean ifCombination = false;   //判斷outboundArea是否有可以結合的track
		
		for(ClassificationTrack ct : this.classificationTrack){   //看哪一個classification track有block
			if(ct.isEmpty==false && ct.pullBackCheckingNow==false){   //若分類軌道有block
				ct.pullBackCheckingNow = true;
				ifCombination = false;
				for(OutboundTrack ot : outboundArea.OutboundTrack){   //檢查outboundTrack是否有可以結合的
					ifCombination = false;
					if(ot.isEmpty==false && ot.pullBackCheckingNow==false){	
						ot.pullBackCheckingNow = true;
						ArrayList<String> tmp = ot.outPutTrain.blockVariety();   //看要出去的火車的種類	
						tmp.add(ct.existTrain.inBoundBlock.get(0));   //因為classification track上的種類都一樣，所以取第一個加入即可				
						if(blockValues.ifBlockCombination(tmp)   //如果classification track的block種類是可以結合的
							|| ot.outPutTrain.inBoundBlock.contains(ct.existTrain.inBoundBlock.get(0))){   //或者有同樣block name可以丟的outboundTrack   
							ot.outPutTrain.inBoundBlock.addAll(ct.existTrain.inBoundBlock);
							ct.whichPullBack = pullBackNum;   //紀錄是哪一個pull back去拉的
							ct.isEmpty = true;								
							ct.existTrain = null;
							ifCombination = true;   //設為可結合				
						}
					}
					ot.pullBackCheckingNow = false;
					if(ifCombination == true)   //如果有被分配到outboundArea
						break;
				}
				if(ifCombination==false){   //如果不能結合
					for(OutboundTrack ot : outboundArea.OutboundTrack){   //檢查所有的outboundTrack
						if(ot.isEmpty && ot.pullBackCheckingNow == false){   //如果是空的，從classification track 移到 outboundTrack
							ot.pullBackCheckingNow = true;
							ot.isEmpty = false;   //outboundTrack設為有東西
							ot.outPutTrain = new Train();
							ot.outPutTrain = ct.existTrain;   //將classificationTrack的火車丟過來
							ct.whichPullBack = pullBackNum;   //紀錄是哪一個pull back去拉的
							ct.isEmpty = true;   //丟過去outboundTrack後就可以讓classificationTrack的位置空出來
							ct.existTrain = null;   //並讓其上面train消失
							ifCombination = true;
						}
						ot.pullBackCheckingNow = false;
						if(ifCombination == true)//如果有被分配到outboundArea
							break;
					}
				}
			}
			ct.pullBackCheckingNow=false;
		}
	}
}
