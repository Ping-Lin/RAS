import java.util.ArrayList;

public class BlockValues {
	public String[] blockValues;
	public int blockLength;
	public String[] blockCombination;
	public BlockValues(){
		
		/**
		 * Big Case
		 */	
/*		blockValues = new String[]{"AA", "AB", "AC", "AD", "AE", "AF", "AG" 
				, "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ"
				, "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA"
				, "BB", "BC", "BD", "BE", "BF", "BG"};
		blockLength = blockValues.length;
		
		/**
		 * Data Sets 2 and 3 Combination:
		 */
/*		blockCombination = new String[]{"AD,AF", "AF,AW,AY", "AH,AK", "AN,AP,AJ,BG", "AR,AW,AY"};
		
		/**
		 * Data Sets 4 and 5 Combination:
		 */
/*		blockCombination = new String[]{"AA,AJ,AN,AP,BC,BE", "AA,AJ,AN,AP,BG", "AB,AD,AF,AS,AU,BA,BB",
  		 "AB,AF,AW", "AC,AG,AX", "AE,AH,AK", "AF,AW,AY", "AH,AK,AT", "AI,AL", "AP,BE,BF", "AQ,BD", "AR,AW,AY", "AV,AZ"};
	
		
		/**
		 * Small Case, Illustrative Example
		 */
		blockValues = new String[]{"B1", "B2", "B3", "B4", "B5", "B6"};
		blockLength = blockValues.length;
		blockCombination = new String[]{"B5,B6"};
	}
	
	/*
	 * 此為看Block是否可以結合，參數為要判斷的block集合
	 */
	boolean ifBlockCombination(ArrayList<String> blockSet){
		boolean ifNoCombine = false;
		for(String bn : blockCombination){
			ifNoCombine = false;
			for(String a : blockSet){
				if(bn.contains(a) == false)
					ifNoCombine = true;
			}
			if(ifNoCombine == false)
				return true;
		}
		return false;
	}
}
