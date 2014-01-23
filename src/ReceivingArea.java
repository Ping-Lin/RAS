import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
/**
 * ReceivingArea 定義了接收火車的區域的方法與變數。
 * trainSet為儲存所有來的火車，
 * ReceivingTrack為接收的軌道。
 * @author Ping
 *
 */
public class ReceivingArea {
	ArrayList<Train> trainSet;   //儲存所有來的火車
	double counter;   //時間軸一開始初始為零
	ReceivingTrack[] receivingTrack;
	private Timer timer;
	ArrayList<Train> finishTrain;   //紀錄離開receiving area的train資訊,在receive train結束的火車,當作紀錄用
	
	public ReceivingArea(){
		trainSet = new ArrayList<Train>();   //初始化來的train，將其放到trainSet
		finishTrain = new ArrayList<Train>();   //初始化
		receivingTrack = new ReceivingTrack[Constants.RECEIVING_TRACKS_NUMBER];   //初始化receiving track
		for(int i=0 ; i<receivingTrack.length ; i++)   //new 空間
			receivingTrack[i] = new ReceivingTrack();
		
		readExcelFile();
		timeTableAndBlockOutput();
		timer = new Timer();
		timer.scheduleAtFixedRate(new ReceivingTask(), 0, 10);
	}
	
	/*
	 * readExcelFile 為獨取Excel檔的function, excel檔格式副檔名須為xls
	 */
	private void readExcelFile(){
		ArrayList<String> readTmp = new ArrayList<String>();   //儲存讀Excel用
		Train tmp = new Train();   //儲存單筆火車用
		try {
			InputStream excelFile = new FileInputStream("src/data_readme/Input_data_set_1.xls");   //要讀的檔案
			jxl.Workbook readWorkBook = Workbook.getWorkbook(excelFile);   //將其讀入workbook中
			Sheet readSheet = readWorkBook.getSheet("Inbound Train Info");   //讀sheet
			
			tmp.inBoundTrainId = "-1";
			for(int i=1 ; i<readSheet.getRows() ; i++){   //讀data		
				for(int j=0 ; j<readSheet.getColumns() ; j++){
					Cell cell = readSheet.getCell(j,i);   //取得格子
					String content = cell.getContents();   //取得格子資料
					readTmp.add(content);
				}
				
				//存進去tmp train裡
				if(tmp.inBoundTrainId.equals(readTmp.get(1))==false){   //若id不一樣，則代表是新的火車進來
					trainSet.add(tmp);   //所以先將tmp train存進去trainSet
					tmp=null;   //告知JVM可回收
					tmp = new Train();   //新另一個train tmp物件			
					
					tmp.day = readTmp.get(0);
					tmp.inBoundTrainId = readTmp.get(1);
					tmp.arrivalTime = readTmp.get(2);
					
				}
				tmp.inBoundBlock.add(readTmp.get(3));   //block持續儲存，新的火車會在上面判斷式清除舊的block list
				readTmp.clear();
			}
			trainSet.add(tmp);   //final add
			tmp=null;   //告知JVM可回收
			readTmp=null; //告知JVM可回收
			
			trainSet.remove(0);   //因為第一個物件是空值(inboundTrainId == -1)
			
			readWorkBook.close();   //關檔
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * timeTableAndBlockOutput會輸出timeTable 和 列車的block分布
	 */
	public void timeTableAndBlockOutput(){
		try {
			jxl.write.WritableWorkbook writeWorkBook = Workbook.createWorkbook(new File("outputFile.xls"));   //要寫入的路徑以及檔名
			jxl.write.WritableSheet writeSheet = writeWorkBook.createSheet("inbound_train", 0);   //建立一個工作表名字，然後0表示為第一頁

			Label label = new Label(0, 0, "Inbound Train No.");   //設一個label, 並添加column major
			writeSheet.addCell(label);   //將格子添加到表格
			label = new Label(1, 0, "Arrival Time");
			writeSheet.addCell(label);
			
			BlockValues b = new BlockValues();   //為了要輸出總共有幾種blocks
			for(int i=2, j=0 ; j<b.blockLength ; i++, j++){
				label = new Label(i, 0, b.blockValues[j]);
				writeSheet.addCell(label);   //將blocks的種類名字寫入sheet
			}
			
			for(int i=0, x=1; i<trainSet.size() ; i++, x++){   //輸入每一筆火車的資料
				label = new Label(0, x, trainSet.get(i).inBoundTrainId);
				writeSheet.addCell(label);   //寫入火車編號
				label = new Label(1, x, trainSet.get(i).arrivalTime);
				writeSheet.addCell(label);   //寫入到達時間
				for(int j=0, y=2 ; j<b.blockLength ; j++, y++){   //寫入車廂種類數量
					label = new Label(y, x, countBlocks(b.blockValues[j], trainSet.get(i)));
					writeSheet.addCell(label);
				}
				label = null;
			}
			
			writeWorkBook.write();   //最後的最後，在一次將資料寫入檔案內即可，這是一次性寫入，第二次寫入會無效
			writeWorkBook.close();   //關檔是好習慣
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {		
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}   
	}
	/**
	 * inbound trains的細節內容輸出
	 */
	public void inboundTrainInfoOutput(){
		try {
			jxl.Workbook readWorkBook = jxl.Workbook.getWorkbook(new File("outputFile.xls"));   //唯讀的Excel工作薄物件
			jxl.write.WritableWorkbook writeWorkBook = Workbook.createWorkbook(new File("outputFile.xls"), readWorkBook);   //要寫入的路徑以及檔名
			jxl.write.WritableSheet writeSheet = writeWorkBook.createSheet("inbound_train_info", 1);   //建立一個工作表名字，然後0表示為第一頁

			Label label = new Label(0, 0, "Inbound Train No");   //設一個label, 並添加(column major)
			writeSheet.addCell(label);   //將格子添加到表格
			writeSheet.addCell(new Label(1, 0, "Day No"));
			writeSheet.addCell(new Label(2, 0, "Arriving time at receiving area"));
			writeSheet.addCell(new Label(3, 0, "Receiving track No"));
			writeSheet.addCell(new Label(4, 0, "Number of blocks"));
			writeSheet.addCell(new Label(5, 0, "Number of cars"));
			writeSheet.addCell(new Label(6, 0, "Destination of blocks (number of cars)"));
			writeSheet.addCell(new Label(7, 0, "Starting time of humping job"));
			writeSheet.addCell(new Label(8, 0, "Ending time of humping job"));
			
			BlockValues b = new BlockValues();   //輸出block values用
			
			for(int i=0, x=1; i<finishTrain.size() ; i++, x++){   //輸入每一筆火車的資料
				writeSheet.addCell(new Label(0, x, finishTrain.get(i).inBoundTrainId));   //寫入火車編號
				writeSheet.addCell(new Label(1, x, finishTrain.get(i).day));   //寫入到達日
				writeSheet.addCell(new Label(2, x, finishTrain.get(i).arrivalTime));   //寫入到達時間
				writeSheet.addCell(new Label(3, x, finishTrain.get(i).receivingTrackNo));   //寫入receiving track No
							
				Integer numberOfBlocks=0;   //block種類數量
				//Integer numberOfCars=0;   //car總數量
				String tmpString = new String("");   //儲存輸出用destination of blocks
				for(int j=0 ; j<b.blockLength ; j++){   //寫入車廂種類數量
					int c = Integer.parseInt(countBlocks(b.blockValues[j], finishTrain.get(i)));
					if(c!=0){
						numberOfBlocks++;
						//numberOfCars+=c;
						tmpString+=b.blockValues[j] + "(" + c + ");";
					}
				}
				writeSheet.addCell(new Label(4, x, numberOfBlocks.toString()));
				writeSheet.addCell(new Label(5, x, String.valueOf(finishTrain.get(i).inBoundBlock.size())));
				writeSheet.addCell(new Label(6, x, tmpString));
				writeSheet.addCell(new Label(7, x, String.valueOf(finishTrain.get(i).currentTime)));
				writeSheet.addCell(new Label(8, x, String.valueOf(finishTrain.get(i).currentTime + 
						(Constants.HUMP_RATE*(1.0f*finishTrain.get(i).inBoundBlock.size())))));
			}
			label = null;
			writeWorkBook.write();   //最後的最後，在一次將資料寫入檔案內即可，這是一次性寫入，第二次寫入會無效
			writeWorkBook.close();   //關檔是好習慣
			readWorkBook.close();   //關檔是好習慣
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {		
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 計算同台火車有多少個[blockName]的個數, 回傳值用String
	 */
	private String countBlocks(String blockName, Train t){
		int count=0;
		for(int i=0 ; i< t.inBoundBlock.size() ; i++){
			if(blockName.equals(t.inBoundBlock.get(i))){
				count++;
			}
		}
		return String.valueOf(count);
	}
	
	/**
	 * 設Receiving Area也有一個引擎在控制火車的進入 
	 */
	class ReceivingTask extends TimerTask{   //FIFO，來的就先拉(這邊之後要改掉)
		int count;   //算receiving track是否都為空了當沒有車再進來的時候
		public void run(){
			count = 0;
			for(ReceivingTrack rt : receivingTrack){
				if(rt.isEmpty){   //如果receiving track是空的話
					if(trainSet.size()!=0){   //還有火車可以拉
						rt.existTrain = trainSet.get(0);   //加入最先來的火車
						trainSet.remove(0);   //將加進去的火車從trainSet刪除
						rt.isEmpty = false;
						rt.existTrain.currentTime = (rt.existTrain.arrivalTimePlusDay()+Constants.TECHNICAL_INSPECTION_TIME);   //進站需要檢查時間30分鐘
					}
					else   //沒火車了
						count++;   //track沒火車,count+1
				}
			}
			if(count == receivingTrack.length){   //當receiving track都為空且當沒有車再進來的時候
				timer.cancel();   //receivingTask 可以結束
				System.out.println("No arrival train.");
				inboundTrainInfoOutput();
			}
		}
	}
	
	/**
	 * 找出最先來的火車，有火車的話就回傳Train，沒有的話就回傳null,
	 * 要看hump的時間
	 */
	public Train firstComeTrain(Double humpTime){
		double minTime = humpTime;
		int trackNo=-1;   //最先來火車的火車軌道
		int i=0;
		for(ReceivingTrack rt : receivingTrack){
			if(rt.isEmpty==false && rt.existTrain.currentTime<minTime){   //track有火車且最先來
				minTime = rt.existTrain.currentTime;
				trackNo = i;   //設定最先來的火車軌道是第幾個			
			}
			i++;
		}
		if(trackNo != -1){   //軌道有火車
			Train tmp = receivingTrack[trackNo].existTrain;
			receivingTrack[trackNo].isEmpty = true;   //讓那個track可以在接車
			receivingTrack[trackNo].existTrain = null;   //讓那個軌道存在的train回歸null
			tmp.receivingTrackNo = "R" + String.valueOf(trackNo+1);   //要加1的原因是因為track命名是從R1開始
			if(humpTime!=Double.MAX_VALUE)   //因為只有第一次拉的時候,hump time為極大值
				tmp.currentTime = humpTime;
			tmp.currentTime += Constants.HUMP_INTERVAL;   //被hump拉出去的現在時間即為Sheet No.2 inbound_train_info的starting time of humping job
			finishTrain.add(tmp);
			return tmp;
		}
		else
			return null;
	}
}

