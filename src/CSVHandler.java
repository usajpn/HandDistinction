import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CSVHandler {
	private String fileName;
	
	public CSVHandler(String f) {
		this.fileName = f;
	}
	
	public WindowFrames csvToWindowFrames() {
		BufferedReader br = null;
		String[] strArray;
		int[] intArray;
		WindowFrames windowFrames = new WindowFrames();
		
		try { 
            br = new BufferedReader(new FileReader(fileName));
            while (br.ready()) { 
                String line = br.readLine(); 
                
                strArray = line.split(","); 
                intArray = new int[strArray.length];
                for (int i = 0; i < strArray.length; i++) {
                	try {
	                	intArray[i] = Integer.parseInt(strArray[i]);
                	} catch (Exception e) { 
                		intArray[i] = 0; 
            		}
            	}
                windowFrames.add(intArray);
            } 
        }catch (FileNotFoundException e){ 
            System.out.println("No file error"); 
            e.printStackTrace(); 
        }catch (IOException e){ 
            System.out.println("I/O error"); 
            e.printStackTrace(); 
        }finally{ 
            if(br != null){ 
                try{ 
                    br.close(); 
                }catch(IOException e) { 
                    System.out.println("I/O error"); 
                    e.printStackTrace(); 
                } 
            } 
        } 
		return windowFrames; 
	}
}
