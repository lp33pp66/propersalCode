import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

class MemoryLogger {
	
	// the only instance  of this class (this is the "singleton" design pattern)
	private static MemoryLogger instance = new MemoryLogger();

	// variable to store the maximum memory usage
	private double maxMemory = 0;
	
	/**
	 * Method to obtain the only instance of this class
	 * @return instance of MemoryLogger
	 */
	public static MemoryLogger getInstance(){
		return instance;
	}
	
	/**
	 * To get the maximum amount of memory used until now
	 * @return a double value indicating memory as megabytes
	 */
	public double getMaxMemory() {
		return maxMemory;
	}

	/**
	 * Reset the maximum amount of memory recorded.
	 */
	public void reset(){
		maxMemory = 0;
	}
	
	/**
	 * Check the current memory usage and record it if it is higher
	 * than the amount of memory previously recorded.
	 */
	public void checkMemory() {
		double currentMemory = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory())
				/ 1024d / 1024d;
		if (currentMemory > maxMemory) {
			maxMemory = currentMemory;
		}
	}
} 

class Item
{
	String itemid;
	TreeSet<String> customerlist=new TreeSet<>();
	TreeMap<String,Customer> objcust=new TreeMap<>();
	boolean bchange;
	//int itemlength=1;
	
	public void Setbchange(boolean bValue){
		this.bchange=bValue;
	}
	
	public void setitemid(String itemid)
	{
		this.itemid=itemid;
	}
	//data->data[1]客戶id, day->data[0];
	public void AddCustomer(String data,String day,double minsupcount)
	{  
		
		Setbchange(true);
		if(customerlist.contains(data)==false)
		{
			customerlist.add(data);
			Customer cc=new Customer();
			cc.setCusid(data);
			cc.Addday(day);
			objcust.put(data, cc);
			
		}
		else
		{		
			objcust.get(data).Addday(day);
		}
		
	}

	public void show()
	{
		//System.out.println(customerlist);
		//System.out.println(itemid+"\n"+"count:"+customerlist.size()+"\n"+"長度: "+itemlength);
		System.out.println(itemid+"\n"+"count:"+customerlist.size());
	//System.out.println(itemid+":"+bchange);
		for(String k:objcust.keySet())
		{
			  //System.out.println(k);
			
			 objcust.get(k).show();
		}
	
	}
	
}

class Customer
{
	String cid;
	ArrayList<String>daylist=new ArrayList<String>();

	public void setCusid(String custid)
	{
		this.cid=custid;
	}
	
	public void Addday(String day)
	{
		
		if(daylist.contains(day)==false)
		{
			
			daylist.add(day);
		}
		
	}
	
	public void show()
	{
			System.out.println(cid+":"+daylist);
		
	}
}

public class G3Span 
{

	public static void Compare(Item i,Item i2,int nNowDay,HashSet<String>length2,TreeMap<String,Item> objitem2, HashSet<String> ResultAll, double minsupcount, HashSet<String> Pool) throws IOException
	{   
		
		//LinkedList<String>daylist2=new LinkedList<String>();
		
	
		if(i.customerlist.size()>=minsupcount && i2.customerlist.size()>=minsupcount)
		{
			
		    // System.out.println("i1 :"+i.itemid+"  i2: "+i2.itemid);
			//System.out.println("i1 :"+i.bchange+"  i2: "+i2.bchange);
		//	String[] resulta = i.customerlist.toArray(new String[i.customerlist.size()]);
			//String[] resultb = i2.customerlist.toArray(new String[i2.customerlist.size()]);
			
			//產生長度為2的sequence 並且放入length2
			//String saaBC=i.itemid.concat(i2.itemid);
			//String saaCB=i2.itemid.concat(i.itemid);
			//String saa23="("+i.itemid.concat(i2.itemid)+")";
			
			
			//int kcount7=i.itemid.length();
			//int kcount8=i2.itemid.length();
			String saaBC=null;//AB
			String saaCB=null;//BA
			String saa23=null;//(AB)		
			int inum=0;
			int jnum=0;
			String Stex7[]=i.itemid.split("%");
			String Stex8[]=i2.itemid.split("%");
			
			
			for(int k=0;k<Stex7.length;k++)
			{
				if(Stex7[k].contains("(")==false && Stex7[k].contains(")")==false && Stex7[k].contains(" ")==false )
				{
				inum++;
				}
			}
			for(int j=0;j<Stex8.length;j++)
			{
				if(Stex8[j].contains("(")==false && Stex8[j].contains(")")==false && Stex8[j].contains(" ")==false )
				{
				jnum++;
				}
			}

		  int prefixlength=0;
		  String prefix=null;
		  //長度為1	
	      if(inum==jnum )
		  {
	         if(inum==2 && jnum==2 &&( Stex7.length>Stex8.length || Stex8.length>Stex7.length))
	    	 {
	    		if(Stex7.length>Stex8.length)
	    		{
	    			for(int k=0;k<Stex7.length-1;k++)
	    			{
	    				for(int j=k;j<Stex8.length-1;j++)
	    				{
	    					if(Stex7[k].contains(Stex8[j])==true)
	    					{
	    						prefixlength=k;//prefix的長度
	    						k++;
	    					}
	    					else if(Stex7[k].contains(Stex8[j])==false)
	    					{
	    						if(k<Stex7.length-1 && j <Stex8.length-1)
	    						{
	    						k++;
	    						j--;
	    						}
	    					}
	    					
	    				}
	    						break;
	    			}	
	    		}
	    		else if( Stex8.length>Stex7.length)
	    		{  	
	    			for(int j=0;j<Stex8.length-1;j++)
	    			{
	    				for(int k=j;k<Stex7.length-1;k++)
	    				{
	    					if(Stex8[j].contains(Stex7[k])==true)
	    					{
	    						prefixlength=j;//prefix的長度
	    						//System.out.println(Stex8[j]);	
	    						j++;
	    					}
	    					else if(Stex8[j].contains(Stex7[k])==false)
	    					{
	    						if(j<Stex8.length-1 && k<Stex7.length-1)
	    						{
	    							j++;
	    							k--;
	    						}
	    					}
	    					
	    				}
	    			break;
						
	    			}	
	    		}
	    		
	    	  }
	    	  else if(inum>=2 && jnum>=2 && Stex8.length==Stex7.length )
	    	  {
	    	  	for(int k=0;k<Stex7.length-1;k++)
	    	  	{
	    	  		for(int j=k;j<Stex8.length-1;)
	    	  		{
	    	  			if(Stex7[k].contains(Stex8[j])==true)
	    	  			{
							prefixlength=k;//prefix的長度
	    	  				
	    	  				if(k==0)
	    	  				{
	    	  					prefix=Stex7[k]+"%";
	    	  					
	    	  				}
	    	  				else
	    	  				{
	    	  				prefix=prefix.concat(Stex7[k]+"%");
	    	  				
	    	  				}
	    	  			 }else if(Stex7[k].contains(Stex8[j])==false)
	    	  			 {
	    	  				 k=Stex7.length-1;
	    	  				
	    	  				break;
	    	  				
	    	  			 }
	    	  			
	    	  			break;	
	    	  			}
	    	  		
	    	  	}
	    	  	
	  			//System.out.println("prefix :"+prefix);
	  			//System.out.println(prefixlength);
	    	  }
	         //(bcd),(bc)b->(bcd)b
	    	  
	         //---------------------------------------------------------------------------------------------------------------------
	    	  //分開買
			  if(inum!=1 &&jnum!=1 && inum==jnum && ((inum-prefixlength==2 && jnum-prefixlength==2 && Stex7[Stex7.length-1].contains(")")==false &&  Stex7[0].contains("(")==false  && Stex8[Stex8.length-1].contains(")")==false &&  Stex8[0].contains("(")==false || inum-prefixlength==0 && jnum-prefixlength==0 && Stex7[0].contains("(")==true && Stex7[Stex7.length-1].contains(")")==false && Stex8[0].contains("(")==true && Stex8[Stex8.length-1].contains(")")==false )) )
				{
					
					if(prefixlength==0 && prefix==null)
					{
					return;	
					}

					//if(i.itemid.contains(i2.itemid)==true){
					//	return;
					//}
					
					//System.out.println("1  "+i.itemid+"+"+i2.itemid);
					saaBC=i.itemid.concat("%"+Stex8[prefixlength+1]);//AB 
					saaCB=i2.itemid.concat("%"+Stex7[prefixlength+1]);//BA	
					
					 if(saaBC.contains(saaCB)==false)
					 {
						 saaBC=i.itemid.concat("%"+Stex8[prefixlength+1]);//AB
						
						 saaCB=i2.itemid.concat("%"+Stex7[prefixlength+1]);;//BA	
						
						//saa23
							if(Stex7[prefixlength+1].compareTo(Stex8[prefixlength+1])<0)
							{
								 saa23=prefix.concat("("+"%"+Stex7[prefixlength+1].concat("%"+Stex8[prefixlength+1])+"%"+")");//(AB)
								
							}
							else if(Stex7[prefixlength+1].compareTo(Stex8[prefixlength+1])>0)
							{
								 saa23=prefix.concat("("+"%"+Stex8[prefixlength+1].concat("%"+Stex7[prefixlength+1])+"%"+")");//(AB)
								// System.out.println(saa23);	
							}
					 }
				     else if(saaBC.contains(saaCB)==true /*&& saaBC.contains(")")==false && saaBC.contains("(")==false && saaCB.contains(")")==false && saaCB.contains("(")==false*/)
					 {
						 saaBC=i.itemid.concat("%"+Stex8[prefixlength+1]);//AB
						 saaCB=i.itemid.concat("%"+Stex8[prefixlength+1]);
						 saa23=null;
						// System.out.println(saaBC);
					 }
					 
					 
				}
				else if(inum==1 && jnum==1)
				{
					if(Stex7[0].contains(Stex8[0])==false)
					{
						saaBC=Stex7[0].concat("%"+Stex8[0]);
						saaCB=Stex8[0].concat("%"+Stex7[0]);
						if(Stex7[0].compareTo(Stex8[0])<0)
						{
							saa23="("+"%"+Stex7[0].concat("%"+Stex8[0])+"%"+")";
							//System.out.println(saa23);
						}else if(Stex7[0].compareTo(Stex8[0])>0)
						{
							saa23="("+"%"+Stex8[0].concat("%"+Stex7[0])+"%"+")";
							//System.out.println(saa23);
						}
					}else
					{
						saaBC=Stex7[0].concat("%"+Stex8[0]);
						saaCB=Stex8[0].concat("%"+Stex7[0]);
						saa23=null;
					}
				}
			
				//�@�_�R
				else if(inum==jnum && inum-prefixlength==1 && jnum-prefixlength==1 && (Stex7[Stex7.length-1].contains(")")==true && Stex8[Stex8.length-1].contains(")")==true))
				{
					
					if(i.itemid.contains(i2.itemid)==true)
					{
						return;
					}
					if(prefixlength==0 && prefix==null)
					{
					return;	
					}
					 else if(Stex7[prefixlength+1].compareTo(Stex8[prefixlength+1])<0)
					{
						 saa23=prefix.concat(Stex7[prefixlength+1].concat("%"+Stex8[prefixlength+1])+"%"+")");
						// System.out.println(saa23);
					}else if(Stex7[prefixlength+1].compareTo(Stex8[prefixlength+1])>0)
					{
						saa23=prefix.concat(Stex8[prefixlength+1].concat("%"+Stex7[prefixlength+1])+"%"+")");
						// System.out.println(saa23);
					}
					 saaBC=null;
					 saaCB=null;
				}
				//(ab),ac,(ab)c
				else if(Stex7.length!=Stex8.length && inum==2 && jnum==2)
				{
					
					if(prefixlength==0)
					{
					return;	
					}
					if(Stex7.length>Stex8.length)
					{
						if(prefixlength==1)
						{
					    saaBC=i.itemid.concat("%"+Stex8[prefixlength]);
						}
						else if(prefixlength==2)
						{
						saaBC=i.itemid.concat("%"+Stex8[prefixlength-1]);	
						}
					}
					else if(Stex7.length<Stex8.length)
					{

						if(prefixlength==1)
						{
							saaCB=i2.itemid.concat("%"+Stex7[prefixlength]);//AB
							//System.out.println(saaBC);
						}else if(prefixlength==2)
						{
							saaCB=i2.itemid.concat("%"+Stex7[prefixlength-1]);	
							//System.out.println(saaBC);
						}
					}
					saaCB=null;
					saa23=null;
					
				}
				//(bcd),(bc)a->(bcd)a
				else if(inum==jnum && inum>2 && jnum>2 && Stex8.length==Stex7.length && (Stex7[Stex7.length-1].contains(")")==true &&Stex7[0].contains("(")==true && Stex8[Stex8.length-1].contains(")")==false && Stex8[0].contains("(")==true||Stex7[Stex7.length-1].contains(")")==false && Stex7[0].contains("(")==true && Stex8[Stex8.length-1].contains(")")==true && Stex8[0].contains("(")==true) && prefixlength==inum-1 && prefixlength==jnum-1)
		    	 {
					// System.out.println("gino1  "+i.itemid+"+"+i2.itemid);
		    		  if(Stex7[Stex7.length-1].contains(")")==true &&"%".contains("(")==true && Stex8[Stex8.length-1].contains(")")==false && Stex8[0].contains("(")==true)
		    		  {
		    			  //System.out.println("gino1  "+i.itemid+"+"+i2.itemid);
		    			  saaBC=i.itemid.concat("%"+ Stex8[Stex8.length-1]);
		    			  saaCB=null;
		    			  saa23=null;
		    			  //System.out.println(saaBC);
		    		  }
		    		  else if(Stex7[Stex7.length-1].contains(")")==false && Stex7[0].contains("(")==true && Stex8[Stex8.length-1].contains(")")==true && Stex8[0].contains("(")==true)
		    		  {
		    			  //System.out.println("gino2  "+i.itemid+"+"+i2.itemid);
		    			  saaCB=i2.itemid.concat("%"+ Stex7[Stex7.length-1]);
		    			  saaBC=null;
		    			  saa23=null; 
		    			  //System.out.println(saaCB);
		    		  }
		    		  
	     		 }
		         
		}

			
			//--------------------------
			//saaBC==ii2
	        //saaCB==ii3
	        //saa23==ii4
			//*****比較&組合資料*****
	      //BC
		  Item ii2=new Item();
			ii2.setitemid(saaBC);
			
			//CB
			Item ii3=new Item();
			ii3.setitemid(saaCB);
			
		    //(BC)
			Item ii4=new Item();
			ii4.setitemid(saa23);
			
			//System.out.println("saaBC "+saaBC+" "+"saaCB "+saaCB+" "+"saa23 "+saa23);
	        //1 . A,B,AB,BA,(AB)
	        if(inum==1 &&jnum==1 && (length2.contains(saaBC)==true && length2.contains(saaCB)==true && length2.contains(saa23)==true))
	        { 
	        	
	        	//System.out.println("1  "+i.itemid+"+"+i2.itemid);
	        	//System.out.println("666  "+i.bchange+"+"+i2.bchange);
	        	for(String k:i.customerlist)
				{
					if(i2.customerlist.contains(k)==true)
					{
						int alast=i.objcust.get(k).daylist.size()-1;
						int b2last=i2.objcust.get(k).daylist.size()-1;
						
						if((Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1 && Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==nNowDay-1) ||  (Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==nNowDay-1)||  (Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1))
						{	
							if(Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))>Integer.parseInt(i.objcust.get(k).daylist.get(0)))
							{
								objitem2.get(saaBC).AddCustomer(k,i2.objcust.get(k).daylist.get(b2last),minsupcount);
								length2.add(saaBC);
							    objitem2.put(saaBC, objitem2.get(saaBC));
							}
							 if(Integer.parseInt(i.objcust.get(k).daylist.get(alast))>Integer.parseInt(i2.objcust.get(k).daylist.get(0)))
							{
								//System.out.println("1.2  "+i.itemid+"+"+i2.itemid);
								objitem2.get(saaCB).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
								length2.add(saaCB);
								objitem2.put(saaCB, objitem2.get(saaCB));
							}
							 if(Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==Integer.parseInt(i.objcust.get(k).daylist.get(alast)))
							{
								objitem2.get(saa23).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
								length2.add(saa23);
								objitem2.put(saa23, objitem2.get(saa23));
							}
									
						}
								
					}
							
				}
	        	
						if(objitem2.get(saaBC).customerlist.size()>=minsupcount)
						{
							ResultAll.add(saaBC);
						    Pool.add(saaBC);
						    objitem2.get(saaBC).Setbchange(true);
						}
						 if(objitem2.get(saaCB).customerlist.size()>=minsupcount)
						{
							ResultAll.add(saaCB);
							Pool.add(saaCB);
							objitem2.get(saaCB).Setbchange(true);
						}
						 if(objitem2.get(saa23).customerlist.size()>=minsupcount)
						{
							ResultAll.add(saa23);
							Pool.add(saa23);
							objitem2.get(saa23).Setbchange(true);
						}
						
			}
	        else if(inum==1 && jnum==1 && (length2.contains(saaBC)==true && length2.contains(saaCB)==true && length2.contains(saa23)==false)&&Stex7[0].contains(Stex8[0])==true)
	        {
	        	for(String k:i.customerlist)
	    		{
	        				
	        		if(i2.customerlist.contains(k)==true)
	    			{
	        					
	        			int alast=i.objcust.get(k).daylist.size()-1;
	    				//int b2last=i2.objcust.get(k).daylist.size()-1;
	    						
						//System.out.println(Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay)));
	    				if(Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1 )
	    				{
	    					if(Integer.parseInt(i.objcust.get(k).daylist.get(alast))>Integer.parseInt(i.objcust.get(k).daylist.get(0)))
	    					{
	    						objitem2.get(saaCB).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
	    						length2.add(saaCB);
	    						objitem2.put(saaCB, objitem2.get(saaCB));
	    					}
	    						
	    						
	    					}
	        				if(objitem2.get(saaBC).customerlist.size()>=minsupcount)
							{
								ResultAll.add(saaBC);
								Pool.add(saaBC);
								objitem2.get(saaBC).Setbchange(true);
							}
	        				}
	        				
	    				}
	        }
	        
	        //2 . AB,AC,ABC,ACB,A(BC)
	        else if(inum!=1 &&jnum!=1 && inum==jnum && (inum-prefixlength==2 && jnum-prefixlength==2 && Stex7[Stex7.length-1].contains(")")==false &&  Stex7[0].contains("(")==false && Stex8[Stex8.length-1].contains(")")==false &&  Stex8[0].contains("(")==false || inum-prefixlength==0 && jnum-prefixlength==0 && Stex7[Stex7.length-1].contains(")")==false &&  Stex7[0].contains("(")==true && Stex8[Stex8.length-1].contains(")")==false &&  Stex8[0].contains("(")==true) && (length2.contains(saaBC)==true && length2.contains(saaCB)==true && length2.contains(saa23)==true))
			{
	        	//System.out.println("2  "+i.itemid+"+"+i2.itemid);
	        	//System.out.println("2   "+"i"+i.itemid+" "+"i2"+i2.itemid);	
	        	for(String k:i.customerlist)
				{
					if(i2.customerlist.contains(k)==true)
					{
						int alast=i.objcust.get(k).daylist.size()-1;
						int b2last=i2.objcust.get(k).daylist.size()-1;
						
						if((Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1 && Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==nNowDay-1)||(Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==nNowDay-1)|| (Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1))
						{	
			
							if(Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))>Integer.parseInt(i.objcust.get(k).daylist.get(0)))
							{
								objitem2.get(saaBC).AddCustomer(k,i2.objcust.get(k).daylist.get(b2last),minsupcount);
								length2.add(saaBC);
								objitem2.put(saaBC,  objitem2.get(saaBC));
						 	}
							if(Integer.parseInt(i2.objcust.get(k).daylist.get(0))<Integer.parseInt(i.objcust.get(k).daylist.get(alast)))
						 	{
						 		
						 		objitem2.get(saaCB).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
						 		length2.add(saaCB);
						 		objitem2.put(saaCB, objitem2.get(saaCB));
						 	}
						 	if(Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==Integer.parseInt(i.objcust.get(k).daylist.get(alast)))
						 	{
						 		objitem2.get(saa23).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
								length2.add(saa23);
								objitem2.put(saa23, objitem2.get(saa23));
						 	}
						 	
						}
						
					}
						if(objitem2.get(saaBC).customerlist.size()>=minsupcount)
						{
							ResultAll.add(saaBC);
							Pool.add(saaBC);
							objitem2.get(saaBC).Setbchange(true);
						}
						if(objitem2.get(saaCB).customerlist.size()>=minsupcount){
							ResultAll.add(saaCB);
							Pool.add(saaCB);
							objitem2.get(saaCB).Setbchange(true);
						}
						if(objitem2.get(saa23).customerlist.size()>=minsupcount){
							ResultAll.add(saa23);
							Pool.add(saa23);
							objitem2.get(saa23).Setbchange(true);
						}
				}
	        	
			}	
	        //3. 一起買
	        else if(inum==jnum && inum-prefixlength==1 && jnum-prefixlength==1 && (Stex7[Stex7.length-1].contains(")")==true && Stex8[Stex8.length-1].contains(")")==true) && (length2.contains(saaBC)==false && length2.contains(saaCB)==false && length2.contains(saa23)==true))
			{
	        	//System.out.println("3  "+i.itemid+"+"+i2.itemid);
	        	
	        	for(String k:i.customerlist)
				{
					if(i2.customerlist.contains(k)==true)
					{
						int alast=i.objcust.get(k).daylist.size()-1;
						int b2last=i2.objcust.get(k).daylist.size()-1;
						//System.out.println("3   "+"i"+i.itemid+" "+"i2"+i2.itemid);
						if(i.bchange==true && i2.bchange==true &&(Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1 && Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==nNowDay-1))
						{	
							objitem2.get(saa23).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
							length2.add(saa23);
							objitem2.put(saa23, objitem2.get(saa23));
						}
					}
					 if(objitem2.get(saa23).customerlist.size()>=minsupcount)
					 {
						ResultAll.add(saa23);
						Pool.add(saa23);
						objitem2.get(saa23).Setbchange(true);
					}
				}
	        	
			}
	        //4. AB,(AC),(AC)B
	        else if(Stex7.length!=Stex8.length && inum==2 && jnum==2 && (length2.contains(saaBC)==true && length2.contains(saaCB)==false && length2.contains(saa23)==false))
			{
	        	
	        	//System.out.println("4  "+i.itemid+"+"+i2.itemid);
	        	if(prefixlength==0)
	        	{
	        		return;	
	        	}
	        		
	        	for(String k:i.customerlist)
				{
	        		//System.out.println("k"+k);
					if(i2.customerlist.contains(k)==true)
					{
						int alast=i.objcust.get(k).daylist.size()-1;
						int b2last=i2.objcust.get(k).daylist.size()-1;
					
						if(Stex7.length>Stex8.length && Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))==nNowDay-1)
						{
							if(Integer.parseInt(i2.objcust.get(k).daylist.get(b2last))>Integer.parseInt(i.objcust.get(k).daylist.get(0)))
							{
								objitem2.get(saaBC).AddCustomer(k,i2.objcust.get(k).daylist.get(b2last),minsupcount);
								length2.add(saaBC);	
								objitem2.put(saaBC, objitem2.get(saaBC));
								
							}
						}
						else if(Stex7.length<Stex8.length && Integer.parseInt(i.objcust.get(k).daylist.get(alast))==nNowDay-1)
						{
							if(Integer.parseInt(i.objcust.get(k).daylist.get(alast))>Integer.parseInt(i2.objcust.get(k).daylist.get(0)))
							{
								
								objitem2.get(saaBC).AddCustomer(k,i.objcust.get(k).daylist.get(alast),minsupcount);
								length2.add(saaBC);	
								objitem2.put(saaBC,objitem2.get(saaBC));
							}
						}
					}
						
					
					if(objitem2.get(saaBC).customerlist.size()>=minsupcount)
					{
						ResultAll.add(saaBC);
					    Pool.add(saaBC);
					    objitem2.get(saaBC).Setbchange(true);
					}
					
				}
	        	
			}
	        
	        
			else if(length2.contains(saaBC)==false || length2.contains(saaCB)==false || length2.contains(saa23)==false )
	     	{  
					for(String k:i.customerlist)
					{
						
						//System.out.println("k1  :"+k);
						if(i2.customerlist.contains(k)==true)
						{
						//	System.out.println("k  :"+k);
							int anum1=0;
							int bnum1=0;
							boolean ahistory=false;
							boolean bhistory=false;
							Caculate(i,i2,k,k,anum1,bnum1,i.objcust.get(k).daylist,i2.objcust.get(k).daylist, ahistory, bhistory, ii2, ii3, ii4, objitem2,length2, minsupcount);
							//break;
						}
					}
					
				
				
			
				
				
				if(length2.contains(ii2.itemid)==true && objitem2.get(ii2.itemid).customerlist.size()>=minsupcount)
				{
					ResultAll.add(saaBC);
					Pool.add(saaBC);
					objitem2.get(saaBC).Setbchange(true);
					
				}
			
			
		    	if(length2.contains(ii3.itemid)==true && objitem2.get(ii3.itemid).customerlist.size()>=minsupcount)
		    	{	
		    		ResultAll.add(saaCB);
		    		Pool.add(saaCB);
		    		objitem2.get(saaCB).Setbchange(true);
		    		
		    	}
		   
		    	if(length2.contains(ii4.itemid)==true && objitem2.get(ii4.itemid).customerlist.size()>=minsupcount)
		    	{	
		    		
		    		ResultAll.add(saa23);
		    		Pool.add(saa23);
		    		objitem2.get(saa23).Setbchange(true);
		    		
		    	}
		    	
			}
	        
	}
		
	}
	
	private static void Caculate(Item i, Item i2, String resulta,String resultb, int anum1, int bnum1, ArrayList<String> daylist,ArrayList<String> daylist2, boolean ahistory, boolean bhistory,Item ii2, Item ii3, Item ii4, TreeMap<String, Item> objitem2,HashSet<String> length2, double minsupcount) 
	{
		if(anum1==daylist.size() && bnum1==daylist2.size())
		{
			return;
		}
		if(anum1==daylist.size())
		{
			for(int j=bnum1;j<=daylist2.size()-1;j++)
			{
				if(Integer.parseInt(daylist2.get(j))>Integer.parseInt(daylist.get(0)))
				{
					if(ii2.itemid!=null && length2.contains(ii2.itemid)==false)
					{
						ii2.AddCustomer(resultb,daylist2.get(j),minsupcount);
						objitem2.put(ii2.itemid, ii2);
						length2.add(ii2.itemid);	
						
					}
					else if(ii2.itemid!=null && length2.contains(ii2.itemid)==true)
				    {
				    	objitem2.get(ii2.itemid).AddCustomer(resultb,daylist2.get(j),minsupcount);
				    	objitem2.put(ii2.itemid, objitem2.get(ii2.itemid));
				    	//length2.add(ii2.itemid);
				    }	
				}
			}	
			return;
		}
		else if(bnum1==daylist2.size())
		{
			for(int j=anum1;j<=daylist.size()-1;j++)
			{
				if(Integer.parseInt(daylist.get(j))>Integer.parseInt(daylist2.get(0)))
				{
					if(ii3.itemid!=null && length2.contains(ii3.itemid)==false)
					{
						ii3.AddCustomer(resulta,daylist.get(j),minsupcount);
						objitem2.put(ii3.itemid, ii3);
						length2.add(ii3.itemid);	
					}
					else if(ii3.itemid!=null && length2.contains(ii3.itemid)==true)
				    {
				    	objitem2.get(ii3.itemid).AddCustomer(resulta,daylist.get(j),minsupcount);
				    	objitem2.put(ii3.itemid, objitem2.get(ii3.itemid));
				    	
				    	//length2.add(ii3.itemid);
				    }	
					
				}
			}
			return;
		}
		//ab
				if(Integer.parseInt(daylist.get(anum1))<Integer.parseInt(daylist2.get(bnum1)))
				{
					
					if(ii2.itemid!=null && length2.contains(ii2.itemid)==false)
					{
						ii2.AddCustomer(resultb,daylist2.get(bnum1),minsupcount);
						objitem2.put(ii2.itemid, ii2);
						length2.add(ii2.itemid);		
					}
					else if(ii2.itemid!=null && length2.contains(ii2.itemid)==true)
				    {
				    	objitem2.get(ii2.itemid).AddCustomer(resultb,daylist2.get(bnum1),minsupcount);
				    	objitem2.put(ii2.itemid, objitem2.get(ii2.itemid));
				    	//length2.add(ii2.itemid);
				    }	
					
					if(Integer.parseInt(daylist.get(anum1))>Integer.parseInt(daylist2.get(0)))
					{
						if(ii3.itemid!=null && length2.contains(ii3.itemid)==false)
						{
							ii3.AddCustomer(resulta,daylist.get(anum1),minsupcount);
							objitem2.put(ii3.itemid, ii3);
							length2.add(ii3.itemid);	
						}
						else if(ii3.itemid!=null && length2.contains(ii3.itemid)==true)
					    {
					    	objitem2.get(ii3.itemid).AddCustomer(resulta,daylist.get(anum1),minsupcount);
					    	objitem2.put(ii3.itemid, objitem2.get(ii3.itemid));
					    	//length2.add(ii3.itemid);
					    }
					}
					anum1++;
					
					ahistory=true;
					bhistory=false;
				
					Caculate(i,i2,resulta,resultb,anum1,bnum1,daylist,daylist2, ahistory, bhistory, ii2, ii3, ii4, objitem2,length2,minsupcount);
					
				}
				//ba
				else if(Integer.parseInt(daylist.get(anum1))>Integer.parseInt(daylist2.get(bnum1)))
				{
					
					
						if(ii3.itemid!=null && length2.contains(ii3.itemid)==false)
						{
							ii3.AddCustomer(resulta,daylist.get(anum1),minsupcount);
							objitem2.put(ii3.itemid, ii3);
							length2.add(ii3.itemid);	
						}
						else if(ii3.itemid!=null && length2.contains(ii3.itemid)==true)
					    {
					    	objitem2.get(ii3.itemid).AddCustomer(resulta,daylist.get(anum1),minsupcount);
					    	objitem2.put(ii3.itemid, objitem2.get(ii3.itemid));
					    	//length2.add(ii3.itemid);
					    }
					
					
					if(Integer.parseInt(daylist2.get(bnum1))>Integer.parseInt(daylist.get(0)))
					{
						if(ii2.itemid!=null && length2.contains(ii2.itemid)==false)
						{
							ii2.AddCustomer(resultb,daylist2.get(bnum1),minsupcount);
							objitem2.put(ii2.itemid, ii2);
							length2.add(ii2.itemid);		
						}
						else if(ii2.itemid!=null && length2.contains(ii2.itemid)==true)
					    {
					    	objitem2.get(ii2.itemid).AddCustomer(resultb,daylist2.get(bnum1),minsupcount);
					    	objitem2.put(ii2.itemid, objitem2.get(ii2.itemid));
					    	//length2.add(ii2.itemid);
					    }	
					}
					bnum1++;
					
					ahistory=false;
					bhistory=true;
					Caculate(i,i2,resulta,resultb,anum1,bnum1,daylist,daylist2, ahistory, bhistory, ii2, ii3, ii4, objitem2,length2,minsupcount);
				
				}
				//(ab)
				else if(Integer.parseInt(daylist.get(anum1))==Integer.parseInt(daylist2.get(bnum1)))
				{
					if(daylist2.get(0).isEmpty()==false && Integer.parseInt(daylist.get(anum1))>Integer.parseInt(daylist2.get(0)))
					{
						if(ii3.itemid!=null && length2.contains(ii3.itemid)==false)
						{
							ii3.AddCustomer(resulta,daylist.get(anum1),minsupcount);
							objitem2.put(ii3.itemid, ii3);
							length2.add(ii3.itemid);	
						}
						else if(ii3.itemid!=null && length2.contains(ii3.itemid)==true)
					    {
					    	objitem2.get(ii3.itemid).AddCustomer(resulta,daylist.get(anum1),minsupcount);
					    	objitem2.put(ii3.itemid, objitem2.get(ii3.itemid));
					    	//length2.add(ii3.itemid);
					    }
						
					}
					if(daylist.get(0).isEmpty()==false && Integer.parseInt(daylist2.get(bnum1))>Integer.parseInt(daylist.get(0)))
					{
						if(ii2.itemid!=null && length2.contains(ii2.itemid)==false)
						{
							ii2.AddCustomer(resultb,daylist2.get(bnum1),minsupcount);
							objitem2.put(ii2.itemid, ii2);
							length2.add(ii2.itemid);		
						}
						else if(ii2.itemid!=null && length2.contains(ii2.itemid)==true)
					    {
					    	objitem2.get(ii2.itemid).AddCustomer(resultb,daylist2.get(bnum1),minsupcount);
					    	objitem2.put(ii2.itemid, objitem2.get(ii2.itemid));
					    	//length2.add(ii2.itemid);
					    	
					    }	
					}
					
					if(ahistory==true && bhistory==false)
					{
						if(ii4.itemid!=null && length2.contains(ii4.itemid)==false)
						{
							ii4.AddCustomer(resulta,daylist.get(anum1),minsupcount);
							objitem2.put(ii4.itemid, ii4);
							length2.add(ii4.itemid);		
						}
						else if(ii4.itemid!=null && length2.contains(ii4.itemid)==true)
					    {
					    	objitem2.get(ii4.itemid).AddCustomer(resulta,daylist.get(anum1),minsupcount);
					    	objitem2.put(ii4.itemid, objitem2.get(ii4.itemid));
					    	//length2.add(ii4.itemid);
					    }	
						bnum1++;
						ahistory=false;
						bhistory=true;
						
						Caculate(i,i2,resulta,resultb,anum1,bnum1,daylist,daylist2, ahistory, bhistory, ii2, ii3, ii4, objitem2,length2, minsupcount);
						
					}
					else if(ahistory==false && bhistory==true)
					{
						if(i.itemid==i2.itemid)
						{
							return;
						}
						if(ii4.itemid!=null && length2.contains(ii4.itemid)==false)
						{
							ii4.AddCustomer(resulta,daylist.get(anum1), minsupcount);
							objitem2.put(ii4.itemid, ii4);
							length2.add(ii4.itemid);		
						}
						else if(ii4.itemid!=null && length2.contains(ii4.itemid)==true)
					    {
					    	objitem2.get(ii4.itemid).AddCustomer(resulta,daylist.get(anum1), minsupcount);
					    	objitem2.put(ii4.itemid, objitem2.get(ii4.itemid));
					    	//length2.add(ii4.itemid);
					    	
					    }	
						anum1++;
						ahistory=true;
						bhistory=false;
							
						Caculate(i,i2,resulta,resultb,anum1,bnum1,daylist,daylist2, ahistory, bhistory, ii2, ii3, ii4, objitem2,length2, minsupcount);
					}
					else if(ahistory==false && bhistory==false)
					{
						if(ii4.itemid!=null && length2.contains(ii4.itemid)==false)
						{
							ii4.AddCustomer(resulta,daylist.get(anum1),minsupcount);
							objitem2.put(ii4.itemid, ii4);
							length2.add(ii4.itemid);	
							
							
						}
						else if(ii4.itemid!=null && length2.contains(ii4.itemid)==true)
					    {
						
					    	objitem2.get(ii4.itemid).AddCustomer(resulta,daylist.get(anum1),minsupcount);
					    	objitem2.put(ii4.itemid, objitem2.get(ii4.itemid));
					    	//length2.add(ii4.itemid);
					    	
					    }	
						anum1++;
						bnum1++;
						ahistory=false;
						bhistory=false;
						Caculate(i,i2,resulta,resultb,anum1,bnum1,daylist,daylist2, ahistory, bhistory, ii2, ii3, ii4, objitem2,length2,minsupcount);
					  
					}
					
				}
				
	}

	

	
	@SuppressWarnings("unchecked")
	public static void main(String[] args)  throws IOException
	{
		MemoryLogger.getInstance().reset();

		String line="";
		String[] data;
		HashSet<String> itemlist=new HashSet<>();
		HashSet<String> length2 =new HashSet<>();//全部的序列
		HashSet<String> ResultAll=new HashSet<>();//長度為2以上且滿足minsupcount
		HashSet<String> Pool=new HashSet<>();//複製ResultAll
		HashSet<String> Copy=new HashSet<>();
		HashSet<String> Result=new HashSet<>();//長度為1且滿足minsupcount
		HashSet<String> countcustomer=new HashSet<String>();
		//LinkedList<Double> mmm=new LinkedList<>();

		TreeMap<String,Item> objitem=new TreeMap<String, Item>();
		TreeMap<String,Item> objitem2=new TreeMap<>();
		LinkedList<String> print=new LinkedList<>();
		int nWindowSize = 0;
		int nNowDay = 1;
		int nOldDay = 1;
		
		double minsupcount=0;
		double minsup=0.019;
		boolean bEnd = false;
		double time1, time2, time3,time4;
		double starttime = System.currentTimeMillis();
		time1 =time3= System.currentTimeMillis();
		Runtime runtime = Runtime.getRuntime();
		long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
				 
		System.out.println("time1 = time3 = "+System.currentTimeMillis());
		while(bEnd == false)
		{
			
			System.out.print("第"+nNowDay+"天");
			
			System.out.print("----------------------------------");
		
			/*if(nNowDay >= nOldDay + nWindowSize)
			{
				//*****�R��nOldDay�����*****
				nOldDay++;
			}*/
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Gino\\Desktop\\��s�Ҹ��\\��������\\12-4test\\S4I2N1KD1K-5.csv"), "UTF8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\12-1.txt"), "UTF8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("../../DataSet/TestData/S4I2N1KD1K-5.csv"), "UTF8"));
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("../../DataSet/TestData/S10I2N1KD1K-3.csv"), "UTF8"));

			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Gino\\Desktop\\��s�Ҹ��\\��������\\12-4test\\S10I2N1KD1K-3.csv"), "UTF8"));

			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Gino\\Desktop\\��s�Ҹ��\\��������\\���e���.csv"), "UTF8"));
			FileWriter writer = new FileWriter("tt.txt");
			for(String k:objitem.keySet())
			{
				objitem.get(k).Setbchange(false);
			}
			for(String k:objitem2.keySet())
			{
				objitem2.get(k).Setbchange(false);
				//System.out.println(objitem2.get(k).itemid+"  :"+objitem2.get(k).bchange);
			}
			
			//length2.removeAll(length2);
			
			while ((line = br.readLine()) != null) 	
			{
				int c = line.charAt(0);
				if(c == 65279)
				{
				   line = line.substring(1, line.length());
				}
				
				data=line.split(",");
				
				int Readday = Integer.parseInt(data[0]);
				
				
				
				//System.out.println("Readday :"+Readday+", nNowDay :"+nNowDay);
				if(Readday == nNowDay)
				{
				
					if(itemlist.contains(data[2])==false)
					{
						itemlist.add(data[2]);
						Item ii=new Item();
						ii.setitemid(data[2]);
					    ii.AddCustomer(data[1],data[0],minsupcount);
					    objitem.put(data[2], ii);
					   
					}
					else
					{
						objitem.get(data[2]).AddCustomer(data[1],data[0],minsupcount);
					}
					
					if(countcustomer.contains(data[1])==false)
					{
						countcustomer.add(data[1]);
					}
					
					minsupcount=/*countcustomer.size()*/997*minsup;
					
						
				}
				
				else if(Readday>nNowDay)
				{
					
					System.out.println();
					nNowDay++;
					break;
				}
		
	
			//System.out.println(minsupcount);
				

		 
		}
		
			 if((line = br.readLine()) == null)
				{
				 //time5 = System.currentTimeMillis();
				 //System.out.println("原始 = "+  (time5-time3)/1000 + "秒");
				 bEnd = true;
					br.close();
				}
			 System.out.println("------------------------------------");
				
				/*for(String k:objitem.keySet())
				{
					
					if(objitem.get(k).customerlist.size()>=minsupcount)
					{
						Result.add(k);
				    }
					//objitem.get(k).show();
					//System.out.println();

					
			    } */
				
				System.out.println();
				System.out.println("------------------------------------");	
			
			
				
			
			for(String k:objitem.keySet())
			{
				for(String k2:objitem.keySet())	
			    { 
						Compare(objitem.get(k),objitem.get(k2), nNowDay, length2, objitem2, ResultAll, minsupcount, Pool);
			    }
				
			}
			
			
			//System.out.println("Pool"+Pool);
			while(Pool.isEmpty()==false )
			{	
				Copy=(HashSet<String>)Pool.clone();
				Pool.clear();	
				for(String c1 : Copy)
				{
					for(String c2 : Copy)
					{ 	
						
						Compare(objitem2.get(c1),objitem2.get(c2), nNowDay, length2, objitem2, ResultAll, minsupcount, Pool);
						
					}
					
					//Pool.removeAll(Pool);
						if(Pool.contains(null)==true)
						{
							Pool.remove(null);
							ResultAll.remove(null);
							//break;
						}
				}
				
				//Pool.removeAll(Pool);
				//System.out.println("Copy"+Copy);
				//System.out.println("Pool2"+Pool);
				//System.out.println("length2"+length2);
				}
			writer.flush();
			
			  // if(nNowDay==8 ||nNowDay==15 ||nNowDay==22 || nNowDay==29 ||  nNowDay==36|| nNowDay==43 ||  nNowDay==50 || nNowDay==57 || nNowDay==64 ||  nNowDay==71 || nNowDay==78 ||nNowDay==85 || nNowDay==92 || nNowDay==99|| nNowDay==106 ||nNowDay==113)
			 if(nNowDay==8 ||nNowDay==15 ||nNowDay==22 || nNowDay==29 ||  nNowDay==36|| nNowDay==43 ||  nNowDay==50 || nNowDay==57 || nNowDay==64 ||  nNowDay==71 || nNowDay==78 ||nNowDay==85 || nNowDay==92 || nNowDay==99|| nNowDay==106 ||nNowDay==113||nNowDay==120||nNowDay==127||nNowDay==134||nNowDay==141||nNowDay==148||nNowDay==155||nNowDay==162||nNowDay==169)

			{
				double temp=0;
				
				time4 = System.currentTimeMillis();
				//System.out.println(nNowDay+"原始 = "+  (time4-time3)/1000 + "秒");
				//mmm.add((time4-time3)/1000);
				//System.out.println(mmm);
				String a=null;
				a=nNowDay-1+"原始 = "+  (time4-time3)/1000 + "秒";
				print.add(a);
				//writer.append(nNowDay+"原始 = "+  (time4-time3)/1000 + "秒");
				//writer.append("\n");
				temp=time4;
				time4=time3;
				time3=temp;
			}
			for(String c3 : print)
			{
				//System.out.println(c3);
				writer.append(c3);
				writer.append("\n");
				writer.flush();
			}
				String[] back = length2.toArray(new String[length2.size()]);
				String reback=null;
				/*for(int l=0;l<back.length;l++)
				{
					reback=back[l];
					if(objitem2.get(reback).customerlist.size()>=minsupcount)
					{
						ResultAll.add(objitem2.get(reback).itemid);
			        //Pool.add(objitem2.get(reback).itemid);	
						
					}
					else if(objitem2.get(reback).customerlist.size()<minsupcount)
					{
						ResultAll.remove(objitem2.get(reback).itemid);
				   // Pool.remove(objitem2.get(reback).itemid);	
					}
					
					
				}
				*/
		
		
		String[] freq = ResultAll.toArray(new String[ResultAll.size()]);
		
	/*	for(int i=0;i<freq.length;i++)
		{
			String candidate=null;
			candidate=freq[i];
			objitem2.get(candidate).show();
			System.out.println();
			
		}*/
		
	    //  System.out.println("length2"+length2);
	      
		 // System.out.println("����1 ���G:"+Result);
		 // System.out.println("���G:"+ResultAll);
		 // System.out.println(ResultAll.size());
		 /* writer.append("\n");
		  writer.append("length2"+length2);
		  writer.append("\n");
		  writer.append("����1 ���G:"+Result);
		  writer.append("\n");
		  writer.flush();*/
		  MemoryLogger.getInstance().checkMemory();

	}
		
	time2 = System.currentTimeMillis();
	System.out.println("cidlist: "+countcustomer.size());
	System.out.println("SUP: "+minsup);
	
	System.out.println("nNowDay"+nNowDay+"時間:"+  (time2-starttime)/1000 + "秒");
	long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
    System.out.println("Memory increased:" + MemoryLogger.getInstance().getMaxMemory()+" MB");
	System.out.println(objitem2.size());
	System.out.println(objitem.size());

	}
	
}
