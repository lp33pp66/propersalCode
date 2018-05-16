import java.awt.BufferCapabilities;
import java.awt.List;
import java.nio.file.FileSystemNotFoundException;
import java.io.*;
import java.sql.Array;
import java.util.*;
import java.util.stream.Stream;
import jdk.jfr.events.FileWriteEvent;

/**
 * 轉換資料格式
 * customer, day, item
 * 再用ＥＸＣＬＥ換成 day, customer, items
 * 
 */


public class tran_dataset{

    public static int AllDay = 168;                                            //總天數


    public static void main(String[] args) throws IOException{
        /*BufferedReader bfr = new BufferedReader(
            new FileReader("../Dataset/GenData/S4I2_5N100D100.data"));*/
        BufferedReader bfr = new BufferedReader(
            new FileReader("../Dataset/GenData/S4I2_5N1KD1K.data"));

        String line ;
        int CID = 0;                                                     //顧客編號
        HashMap<Integer, String> CusSeq = new HashMap<Integer, String>();//Customer sequence 
        HashMap<Integer, String> CusDay = new HashMap<Integer, String>();//Customer day
        
        while((line = bfr.readLine()) != null){
            String[] data = line.split(" ");
            String Sequence = "";
            int len = Integer.parseInt(data[0]);                          //seq len    
            int x = Integer.parseInt(data[1]); 
            int idx = 1;
            int count = 0;
/*********************************************************************************************/            
            while(idx<data.length){
                while(count < len){
                    for(int i = idx; i < idx + Integer.parseInt(data[idx]); i++){
                        Sequence += " "+data[i+1];
                    }
                    idx = idx + Integer.parseInt(data[idx]) + 1;
                    count++;
                    Sequence += " " + "-1";
                }
            }
            CusSeq.put(CID, Sequence.trim());

/**********************************************************************************************/
            TreeSet<Integer> hset = new TreeSet<Integer>();
            
            while( hset.size() != len){
                int r = (int)(Math.random() * AllDay)+1;
                hset.add(r);
            }

            Iterator iterator = hset.iterator();
            String Daylist = " ";
            while(iterator.hasNext()){
                Daylist += " " + iterator.next() ;
            }    
            CusDay.put(CID,Daylist.trim());
            CID++;
        }
        bfr.close();

        /*BufferedWriter bfw = new BufferedWriter(
            new FileWriter("../DataSet/TestData/S4I2_5N100D100_SPStream.csv"));*/
        BufferedWriter bfw = new BufferedWriter(
            new FileWriter("../DataSet/TestData/S4I2_5N1KD1K_SPStream.csv"));



        HashMap<Integer, String> CDI = new HashMap<Integer, String>();//Customer day item
        ArrayList<String> Alist = new ArrayList<String>();
        ArrayList<String> Blist = new ArrayList<String>();
        for(int i = 0; i <CID ; i++){
            String[] S = CusSeq.get(i).split("-1"); //x -1 t y -1
            String[] D = CusDay.get(i).split(" ");  //1 2 3 
            if(S.length == D.length){
                for(int y =0; y<S.length ; y++){
                    String str = i + "," + D[y].trim() + "," + S[y].trim() + " \r\n";
                    
                    Alist.add(str);
                } 
            }else{
                System.out.println(CID + ": F" );
            }
        }

         for(String str:Alist){                                                     //change
            String[] it = str.trim().split(",");
            String[] item = it[2].trim().split(" ");
            for(int i = 0; i < item.length; i++){
                String str1 = it[0] + "," + it[1]+ "," + item[i]+ "\r\n";
                bfw.append(it[0] + "," + it[1]+ "," + item[i] + " \r\n");
                Blist.add(str1);
            }  
        }
        
        for(String str:Blist){
            String[] it = str.trim().split(",");
        }
        System.out.println(Blist);

        
        

        bfw.flush();
        bfw.close();
        System.out.println("done : " );


/*        for(int k:CusDay.keySet()){
            System.out.println(k + ":" + CusDay.get(k) );
        }
*/


    }
}