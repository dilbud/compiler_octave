package pkg;



import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Data {
	private HashMap<String, int[][]> memory = new HashMap<String, int[][]>(); // for end of program memory
	private HashMap<String, int[][]> dynamic = new HashMap<String, int[][]>(); // for end of line dynamic memory
	
	private Stack<String> idStack = new Stack<String>();
	private Queue<Integer> consQueue = new LinkedList<>(); // for matix
	private Stack<String> opStack = new Stack<String>();
	private Queue<String> matOpQueue = new LinkedList<>();
	private int semicolo = 0;
	private int comma = 0;
	private boolean lock = true;
	private boolean printLock = true;
	private boolean exit = true;
	private int [][] currentMat = null; // final matrix output
	private String currentId = null;
	private int scaler = 1;
	
	void setScaler(int op) {
		if(op == 45) {
			this.scaler = -1;
		}else {
			this.scaler = 1;
		}
	}
	int getScaler() {
		return this.scaler;
	}
	
	void setMatDim(String op) {
		
		if(op == ";") {
			this.semicolo += 1;
		}
		if(op == ",") {
			this.comma += 1;
		}
		this.matOpQueue.add(op);
	}
	
	void clear() { // memory
		this.lock = false;
		this.memory.clear();
	}
	void clc() {
		this.lock = false;
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException | IOException e) {
		    System.out.print("\033[H\033[2J");  
		    System.out.flush();
		}
	}
	
	void lineClear() { 
		this.idStack.clear();
		this.consQueue.clear();
		this.opStack.clear();
		this.dynamic.clear();
		this.matOpQueue.clear();
		this.semicolo = 0;
		this.comma = 0;
		this.lock = true;
		this.printLock = true;
		this.exit = true;
		this.currentMat = null;
		this.currentId = null;
	}
	
	void setOp(String op) {
		this.opStack.push(op);
	}
	void setId(String id) {
		this.idStack.push(id);
		
	}
	void setCons(int cons) {
		this.consQueue.add(cons);
	}
	void setPrintLock(int val) {
		if(val == 59) {
			this.printLock = false;
		}else{
			this.printLock = true;
		}
	}
	
	boolean getExit(){
		return this.exit;
	}
	
	void exit() {
		this.lock = false;
		this.exit = false;
	}
	
	void printVal() {
		if(this.lock) {
			if(this.printLock) {
				System.out.println((this.currentId !=null?this.currentId:"ans") +" =");
				for (int row = 0; row < this.currentMat.length; row++) {
					System.out.print("\t");
			        for (int col = 0; col < this.currentMat[row].length; col++) {
			            System.out.printf("%-6d", this.currentMat[row][col]);
			        }
			        System.out.println();
			    }
			}
		}
	}

	private void copyMemory() {
		for(int i=0; i<this.idStack.size();i++) {
			String id = this.idStack.get(i);
			int [][]mat = this.memory.get(id);
			this.dynamic.put(id, mat);
		}
	}
	
	private int[] getDim() {
		if(this.semicolo == 0 & this.comma == 0) {
			int [] a = {1,1};
			return a;
		}
		if(this.semicolo == 0 & this.comma != 0) {
			int [] a = {1,this.comma+1};
			return a;
		}
		if(this.semicolo != 0 & this.comma == 0) {
			int [] a = {this.semicolo+1,1};
			return a;
		}
		String current;
		int comma = 0;
		int semicolo = 0;
		int row=0, col=0;
		boolean lock = true;
		int s = this.matOpQueue.size();
		for (int i=1 ; i<=s ; i++) {
			current = this.matOpQueue.remove();
			if(current == ";") {
				semicolo +=1;
				if(lock) {
					col = comma+1;
					lock = false;
				}			
			}
			if(current == ",") {
				comma +=1;
			}
		}
		row = semicolo +1;
		int []a = {row,col};
		return a;
	}
	
	private void sum() throws Exception {
		String key1 = this.idStack.pop();
		String key2 = this.idStack.pop();
		int [][]val1 = this.dynamic.get(key1);
		int [][]val2 = this.dynamic.get(key2);
		if(val1 != null & val2 != null) {
			int val1_row = val1.length;
			int val1_col = val1[0].length;
			int val2_row = val2.length;
			int val2_col = val2[0].length;
			if(val1_row == val2_row & val1_col == val2_col) {
				int [][] sum = new int[val1_row][val1_col];
				for(int i=0; i<val1_row;i++) {
					for(int j=0;j<val1_col;j++) {
						sum[i][j] = val1[i][j] + val2[i][j];
					}
				}
				this.idStack.push("temp");
				this.dynamic.put("temp", sum);
			}else {
				throw new Exception("error: Dimensions not equal");
			}
		}else {
			throw new Exception("error: "+ key2 +" or "+key1 + " undefined");
		}
	}
	
	private void sub() throws Exception {
		String key1 = this.idStack.pop();
		String key2 = this.idStack.pop();
		int [][]val1 = this.dynamic.get(key1);
		int [][]val2 = this.dynamic.get(key2);
		if(val1 != null & val2 != null) {
			int val1_row = val1.length;
			int val1_col = val1[0].length;
			int val2_row = val2.length;
			int val2_col = val2[0].length;
			
			if(val1_row == val2_row & val1_col == val2_col) {
				int [][] sub = new int[val1_row][val1_col];
				for(int i=0; i<val1_row;i++) {
					for(int j=0;j<val1_col;j++) { 
						sub[i][j] = val2[i][j] - val1[i][j];
					}
				}
				this.idStack.push("temp");
				this.dynamic.put("temp", sub);
			}else {
				throw new Exception("error: Dimensions not equal");
			}
		}else {
			throw new Exception("error: "+ key2 +" or "+key1 + " undefined");
		}
	}
	
	private void mul() throws Exception { 
		String key1 = this.idStack.pop();
		String key2 = this.idStack.pop();
		int [][]val1 = this.dynamic.get(key1);
		int [][]val2 = this.dynamic.get(key2);
		
		if(val1 != null & val2 != null) {
			
			int val1_row = val1.length;
			int val1_col = val1[0].length;
			int val2_row = val2.length;
			int val2_col = val2[0].length;	
			
			if(val2_col == val1_row) {
				int [][] mul = new int[val2_row][val1_col];
				for(int i=0; i<val2_row;i++) {
					for(int j=0;j<val1_col;j++) {
						mul[i][j] = 0;
						for(int k=0;k<val1_row;k++) {
							mul[i][j] += val2[i][k] * val1[k][j];
						}
					}
				}
				this.idStack.push("temp");
				this.dynamic.put("temp", mul);
				System.out.println("end *");
			}else {
				throw new Exception("error: matrix multiplication rules violation");
			}
		}else {
			throw new Exception("error: "+ key2 +" or "+key1 + " undefined");
		}
	}
	
	private void unimul() throws Exception {
		if(this.consQueue.size() == 0) {
			String key1 = this.idStack.pop();
			String key2 = this.idStack.pop();
			int [][]val1 = this.dynamic.get(key1);
			int [][]val2 = this.dynamic.get(key2);
			if(val1 != null & val2 != null) {
				int val1_row = val1.length;
				int val1_col = val1[0].length;
				int val2_row = val2.length;
				int val2_col = val2[0].length;
				System.out.println( " ");
				boolean chk = (val1_row == val2_row) & (val1_col == val2_col);
				if(val1_col == 1 & val1_row == 1) {
					int sing = val1[0][0];
					int [][] unimul = new int[val2_row][val2_col];
					for(int i=0; i<val2_row;i++) { 
						for(int j=0;j<val2_col;j++) {
							unimul[i][j] = val2[i][j] *sing;
						}
					}
					this.idStack.push("temp");
					this.dynamic.put("temp", unimul);	
				}else if(!chk) {
					throw new Exception("error: "+ key1+ " undefined");
				}else {
					int [][] unimul = new int[val2_row][val2_col];
					for(int i=0; i<val1_row;i++) { 
						for(int j=0;j<val1_col;j++) {
							unimul[i][j] = val1[i][j] *val2[i][j];
						}
					}
					this.idStack.push("temp");
					this.dynamic.put("temp", unimul);
				}
			}else {
				throw new Exception("error: "+ key1  + " or " +key2 + " undefined");
			}
		}	
		if(this.consQueue.size() == 1) {
			String key1 = this.idStack.pop();
			int [][]val = this.dynamic.get(key1);
			int sing = this.consQueue.remove();
			if(val != null) {
				int val_row = val.length;
				int val_col = val[0].length;
				int [][] unimul = new int[val_row][val_col];
				for(int i=0; i<val_row;i++) { 
					for(int j=0;j<val_col;j++) {
						unimul[i][j] = val[i][j] *sing;
					}
				}
				this.idStack.push("temp");
				this.dynamic.put("temp", unimul);		
			}else {
				throw new Exception("error: "+ key1+ " undefined");
			}
		}
	}
	
	void operate() throws Exception {
		if(this.lock) {
			this.copyMemory(); // to dynamic memory
			if(this.opStack.isEmpty()) { // one path 
				String key = this.idStack.pop();
				int [][] mat = this.dynamic.get(key);
				if(mat != null) {
					this.dynamic.put("temp", mat);
					this.idStack.push("temp");
				}else {
					throw new Exception("error: "+ key+ " undefined");
				}
			}else {
				int opStackSize = this.opStack.size();
				for(int p=0;p<opStackSize;p++) {
					String op = this.opStack.pop();
					switch(op) {
						case "+" :
							this.sum();
							break;
						case "-" :
							this.sub();
							break;
						case "*" :
							this.mul();
							break;
						case ".*" :
							this.unimul();
							break;
						case "=" :
							if(this.consQueue.isEmpty()) {
								String key1 = this.idStack.pop();
								int [][]prv = this.dynamic.get(key1);
								String key2 = this.idStack.pop();
								if(prv != null) {
									this.currentId = key2;
									this.dynamic.put(key2, prv);
									this.dynamic.put("temp", prv);
									this.idStack.push("temp");
								}else {
									throw new Exception("error: "+ key1+ " undefined");
								}
							}else { //	a = [1,2,3;4,5,6;7,8,9]
								int [] dim = getDim(); 
								int row = dim[0];
								int col = dim[1];
								int [][] newMat  = new int[row][col];
								for(int i=0; i< row;i++) {
									for(int j=0;j<col;j++) {
										newMat[i][j] = this.consQueue.remove();
									}
								}
								String key = this.idStack.pop();
								this.currentId = key;
								this.dynamic.put(key, newMat);
								this.dynamic.put("temp", newMat);
								this.idStack.push("temp");
							}
					}
				}
			}
			this.dynamic.keySet().forEach((key) -> { 
				if(key == "temp") {
					this.currentMat = this.dynamic.get(key);
				}else {
					this.memory.put(key, this.dynamic.get(key));
				}
			});
		}
	}
}