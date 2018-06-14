package tree;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

@SuppressWarnings("unused")
class FinSeq
{
	String Seq_id;
	int count=0;
	HashSet<Integer> SECID=new HashSet<>();
	
}
class Sequence
{
	String Customer_id;
	HashSet<String> Sequence_count=new HashSet<>();//???
	ArrayList<String> Customer_Sequence=new ArrayList<String>();//?@??@?��??~,??C
	ArrayList<String> DataPosition=new ArrayList<String>();
	public void setCustomer_id(String Customer_id)//data[1]
	{
		this.Customer_id=Customer_id;
	}

	public void AddSequence(String data0,String data1,String data2)//data[0],data[1],data[2]
	{   
		//Customer_Sequence.add(data2);
		int data0toint=Integer.parseInt(data0);
		
		/*while(Customer_Sequence.size() < 1) {
			Customer_Sequence.add(Customer_Sequence.size(), null);
			}*/
		if(data1==Customer_id)
		{
			// DataPosition.add(0,data1);
			Customer_Sequence.add(0,data1);
		}
		
		
		if(Sequence_count.contains(data0)==false)
		{	
			 
			Sequence_count.add(data0);
			
			DataPosition.add(data0);
			
			//Customer_Sequence.add(data2);
			int k=DataPosition.indexOf(data0)+1;
			Customer_Sequence.add(k," ".concat(data2) );
		}
		else
		{    
			int k=DataPosition.indexOf(data0)+1;
			Customer_Sequence.set(k,Customer_Sequence.get(k).concat(" ").concat(data2));
		}
		
		
		
	}
	public void show()
	{	
		System.out.println(Customer_Sequence);
	}
}



class Headtable_item
{
	String Headtable_itemid;
	//HashSet<String> itemset=new HashSet<>();
	HashSet<String> Customerset=new HashSet<>();//??~??COUNT
	HashSet<Node> Position=new HashSet<Node>();
	int count=0;
	public void setHeadtable_itemid(String itemid)
	{
		this.Headtable_itemid=itemid;
	}		
	
	public void Addtable(String data1)
	{
			if(Customerset.contains(data1)==false)
			{
				Customerset.add(data1);
				count=Customerset.size();
			}
		
	}

	public void show(double min_sup)
	{
		
			//System.out.println(headtable_itemid);
			if(Customerset.size()>=min_sup)
			{
				System.out.println(Headtable_itemid+":"+count);
				
				
			}
		
			//System.out.println("Customerset"+Customerset);
	}
	
	public void show2(double new_min_sup)
	{
		
			//System.out.println(headtable_itemid);
			//if(Customerset.size()>=new_min_sup)
			//{
				System.out.println(Headtable_itemid+":"+count);
				
			//}
			
	}
	
	
}

public class Fusp
{   
	public static HashMap<String,FinSeq> Resualt=new HashMap<>(); 
	public static HashSet<Integer> CID=new HashSet<>();
	public static HashSet<Integer> PAUSE=new HashSet<>();
	public static ArrayList<Node> Seqlist = new ArrayList<Node>();//?n???R????C??l
	 public static void BuiltTree(ArrayList<Node> NodeList,TreeMap<Integer,Sequence> Customer_ID,Node treeRootNode,Integer k,ArrayList<String> list,TreeMap<String,Headtable_item> Headtable)
	 {
		    NodeList.removeAll(NodeList);
			int firstnodei=1,firstnodej=1;
			int num=0;
			boolean check=false;
			int len=Customer_ID.get(k).Customer_Sequence.size();
			//System.out.println(len);
			
			for(int i=1;i<len;i++)
			{
			    String element[]=Customer_ID.get(k).Customer_Sequence.get(i).split(" ");
			   
			   
				for(int j=1;j<element.length;j++)
				{
					
					if(list.contains(element[j]))
					{
						num++;
						//System.out.println("element"+element[j]+ i+":"+j);
						//System.out.println("element.length"+element.length);
						//System.out.println("num: "+num);
					
						
		            if(i>=firstnodei && j>=firstnodej && num==1)
		            {
						firstnodei=i;
						firstnodej=j;
		            }
		         //   System.out.println("YA"+firstnodei+firstnodej);
		            
		            if(j!=1 &&j==element.length-1)
		            {
		            	int el=0;
		            	for(int k1=j;k1>0;k1--)
						{
							
							if(list.contains(element[k1])==false)
							{
								el++;
							}
							if(el+1==j)
							{
								 check=true;
							}
						}
					}
		            
					//level1 ?L???@??
		        	if(num==1 && i==firstnodei && j==firstnodej &&( treeRootNode.children.isEmpty()==true || treeRootNode.childrenS.containsKey(element[j])==false))
		        	{
		        	    Node childNode1= addChild(treeRootNode, element[firstnodej]);
		        	    //childNode1.count=1;
		        	    treeRootNode.childrenS.put(element[firstnodej],childNode1 );
		        	    treeRootNode.childrenS.get(element[firstnodej]).CID.add(k);
		        	    treeRootNode.childrenS.get(element[firstnodej]).count=treeRootNode.childrenS.get(element[firstnodej]).CID.size();
		        	    treeRootNode.childrenS.get(element[firstnodej]).level=1;
		        	    NodeList.add(0, childNode1);
		        	    if(Headtable.containsKey(childNode1.id)==true )
		        	    {
		        	    	Headtable.get(childNode1.id).Position.add(childNode1);
		        	    	//System.out.println("childNode1.id : "+childNode1.id);
		        	    }
		        	    
		        	    //	System.out.println("123456");
		        	    	
		        	}//level1 ??n?@??
		        	else if(num==1 && i==firstnodei && j==firstnodej  && ( treeRootNode.children.isEmpty()==false || treeRootNode.childrenS.containsKey(element[j])==true))
	        	    {
		             
		                treeRootNode.childrenS.get(element[firstnodej]).CID.add(k);
		                treeRootNode.childrenS.get(element[firstnodej]).count= treeRootNode.childrenS.get(element[firstnodej]).CID.size();
		                treeRootNode.childrenS.get(element[firstnodej]).level=1;
		                NodeList.add(0,treeRootNode.childrenS.get(element[firstnodej]));
		                if(Headtable.containsKey(treeRootNode.childrenS.get(element[firstnodej]).id)==true && Headtable.get(treeRootNode.childrenS.get(element[firstnodej]).id).Position.contains(treeRootNode.childrenS.get(element[firstnodej]))==false)
		        	    {
		        	    	Headtable.get(treeRootNode.childrenS.get(element[firstnodej]).id).Position.add(treeRootNode.childrenS.get(element[firstnodej]));
		        	    	//System.out.println("childNode1.id : "+treeRootNode.childrenS.get(element[firstnodej]).id);
		        	    }
		                	// System.out.println("7654321");
		                	 
	        	    }
		        	
		        	 // level2-> end
		            if(i!=firstnodei || j!=firstnodej && num>1 )
		            { 
		                	 
		               
		               if(NodeList.get(num-2).childrenS.containsKey(element[j])==true  && NodeList.get(num-2).childrenS.get(element[j]).btogether==false &&( j==1 || check==true))//1
		               {
		            	   //NodeList.get(num-2).childrenS.get(element[j]).count++;
		            	   NodeList.get(num-2).childrenS.get(element[j]).CID.add(k);
		            	   NodeList.get(num-2).childrenS.get(element[j]).count=NodeList.get(num-2).childrenS.get(element[j]).CID.size();
		            	   NodeList.get(num-2).childrenS.get(element[j]).level=num;
		            	   NodeList.add(num-1,NodeList.get(num-2).childrenS.get(element[j]));
		                   //System.out.println("1");
		            	   if(Headtable.containsKey(NodeList.get(num-2).childrenS.get(element[j]).id)==true && Headtable.get(NodeList.get(num-2).childrenS.get(element[j]).id).Position.contains(NodeList.get(num-2).childrenS.get(element[j]))==false)
			        	    {
			        	    	Headtable.get(NodeList.get(num-2).childrenS.get(element[j]).id).Position.add(NodeList.get(num-2).childrenS.get(element[j]));
			        	    	//System.out.println("childNode1.id : "+NodeList.get(num-2).childrenS.get(element[j]).id);
			        	    }	
		                		
		                		
		                }
		                else if(NodeList.get(num-2).childrenS.containsKey(element[j])==false && ( j==1 || check==true) )//2
		                {
		                	Node childNode2= addChild(NodeList.get(num-2), element[j]);
			                NodeList.add(childNode2); 
	 						NodeList.get(num-1).btogether=false;
	 						NodeList.get(num-1).CID.add(k);
	 						NodeList.get(num-1).count=NodeList.get(num-1).CID.size();
	 						NodeList.get(num-1).level=num;
	 						NodeList.get(num-2).childrenS.put(element[j],childNode2);	
	 						if(Headtable.containsKey(childNode2.id)==true && Headtable.get(childNode2.id).Position.contains(childNode2)==false)
			        	    {
			        	    	Headtable.get(childNode2.id).Position.add(childNode2);
			        	    	//System.out.println("childNode2.id : "+childNode2.id);
			        	    }
			                // System.out.println("2");
			                	
		                }
		                
		                if(NodeList.get(num-2).childrenI.containsKey(element[j])==true && NodeList.get(num-2).childrenI.get(element[j]).btogether==true &&(j>1 && check==false))//3
		                {
		                	//NodeList.get(num-2).childrenI.get(element[j]).count++;
		                	NodeList.get(num-2).childrenI.get(element[j]).CID.add(k);
		                	NodeList.get(num-2).childrenI.get(element[j]).count=NodeList.get(num-2).childrenI.get(element[j]).CID.size();
		                	NodeList.get(num-2).childrenI.get(element[j]).level=num;
		                	NodeList.add(num-1,NodeList.get(num-2).childrenI.get(element[j]));
		                	// System.out.println("3");
		                	 if(Headtable.containsKey(NodeList.get(num-2).childrenI.get(element[j]).id)==true && Headtable.get(NodeList.get(num-2).childrenI.get(element[j]).id).Position.contains(NodeList.get(num-2).childrenI.get(element[j]))==false)
				        	 {
				        	    Headtable.get(NodeList.get(num-2).childrenI.get(element[j]).id).Position.add(NodeList.get(num-2).childrenI.get(element[j]));
				        	    //System.out.println("childNode1.id : "+NodeList.get(num-2).childrenI.get(element[j]).id);
				        	 }	
		                }
		                else if(NodeList.get(num-2).childrenI.containsKey(element[j])==false  && (j>1 && check==false))//4
		                {
		                	
		                	Node childNode2= addChild(NodeList.get(num-2), element[j]);
			                NodeList.add(childNode2);	
	 						NodeList.get(num-1).btogether=true;
	 						NodeList.get(num-1).CID.add(k);
	 						NodeList.get(num-1).count=NodeList.get(num-1).CID.size();
	 						NodeList.get(num-1).level=num;
	 						NodeList.get(num-2).childrenI.put(element[j], childNode2);
	 						//System.out.println(NodeList.get(num-2).id+"\\"+NodeList.get(num-2).childrenI.get(element[j]).btogether+"\\"+NodeList.get(num-2).childrenI);
	 						//System.out.println(NodeList.get(num-2).id+"\\"+NodeList.get(num-2).childrenS.get(element[j]).btogether+"\\"+NodeList.get(num-2).childrenS);
	 						// System.out.println("4");	
	 						if(Headtable.containsKey(childNode2.id)==true && Headtable.get(childNode2.id).Position.contains(childNode2)==false )
			        	    {
			        	    	Headtable.get(childNode2.id).Position.add(childNode2);
			        	    	//System.out.println("childNode2.id : "+childNode2.id);
			        	    }
		                }
		                	
		                	
		             }
						
					 }
					
			     }
		    } 
			//System.out.println(" NodeList"+ NodeList);
	 }
	 public static void BubbleSort(ArrayList<String> list,TreeMap<String,Headtable_item> Headtable)
	 {
	        for (int i = list.size()-1; i > 0; --i)
	            for (int j = 0; j < i; ++j)
	                if (Headtable.get(list.get(j)).count < Headtable.get(list.get(j+1)).count)
	                {
	                    Swap(list, j, j + 1);
	                }
	 }
	 
	 private static void Swap(ArrayList<String> list, int indexA, int indexB)
	 {
	        String tmp = list.get(indexA);
	        list.set(indexA,list.get(indexA).replace(list.get(indexA),list.get(indexB))) ;
	        list.set(indexB,list.get(indexB).replace(list.get(indexB), tmp)) ;
	       
	 }
	 //------------------------------------------------------------------------------------------------
	 
	public static void SortSupport(double min_sup,ArrayList<String> list,TreeMap<String,Headtable_item> Headtable,String k)
	{
		
		
			if(Headtable.get(k).count >=min_sup)
			{
				list.add(k);
			  
				BubbleSort(list, Headtable);
			    
			}
			
	
			
	}
	
	private static Node addChild(Node parent, String id) {
		   Node node = new Node(parent);
		   node.setId(id);
		   parent.getChildren().add(node);
		 /*  if(node.btogether==false)
		    {
			   parent.childrenS.put(id, node);
		    }else if(node.btogether==true)
		    {
		    	parent.childrenI.put(id, node);
		    }
		    	*/
		   return node;
		 }
		 
		
		private static void printTree(Node node, String appender) {
		  System.out.println(appender + node.getId()+"|t:"+node.btogether+"|count:"+node.count+"|CID:"+node.CID+"|LEVEL:"+node.level);
		  //System.out.println(node.childrenS.keySet());
		  for (Node each : node.getChildren()) {
		   printTree(each,  " "+ appender);
		  }
		 }
		private static void removeSelectedNodes(Node node,ArrayList<String> toBedeletedNodes, int startFrom,TreeMap<String,Headtable_item> Headtable) 
		 {  
			for (int i = startFrom; i < node.getChildren().size(); i++) 
			{
				//System.out.println(i);
				Node each = node.getChildren().get(i);
				// System.out.println("2313"+node.getChildren().get(i));
				if (toBedeletedNodes.contains(each.id)) 
				{
					
					 each.deleteNode(Headtable);
					 if(Headtable.get(each.id).Position.contains(each)==true)
					 {
					   Headtable.get(each.id).Position.remove(each);
					   
					 }
					 removeSelectedNodes(node, toBedeletedNodes, i,Headtable);
					   break;
				   
				 
				}
				
				   removeSelectedNodes(each ,toBedeletedNodes, 0,Headtable);
			}
		}
		private static void removeCidNodes(Node node,HashSet<Integer> removecid, int startFrom,TreeMap<String,Headtable_item> Headtable) 
		 {  
			for (int i = startFrom; i < node.getChildren().size(); i++) 
			{
				//System.out.println(i);
				Node each = node.getChildren().get(i);
				//System.out.println(removecid);
				//System.out.println("2313"+node.getChildren().get(i));
				HashSet<Integer> deletecid= new HashSet<Integer> ();
				boolean excist=false;
				for(Integer k:each.CID)
				{
					if(removecid.contains(k))
					{
						deletecid.add(k);
						excist=true;
					}
				}
				if (excist==true) 
				{
					if(each.CID.size()>1)
					{
						for(Integer k:deletecid)
						{
							each.CID.remove(k);
						}
						each.count=each.CID.size();
					}
					else if(each.CID.size()==1)
					{
					 if(Headtable.get(each.id).Position.contains(each)==true)
					 {
						  Headtable.get(each.id).Position.remove(each);
						   
					 }
					  each.deleteNode(Headtable);
					}
					
					 removeCidNodes(node, removecid, i,Headtable);
					   break;
				   
				 
				}
				
				removeCidNodes(each ,removecid, 0,Headtable);
			}
		}
		
	
	public  static void main(String[] args) throws IOException 
	{
		
		String line="";
		String[] data;
		double min_sup=0;
		HashSet<String> Cust01=new HashSet<>();//?H
		TreeMap<Integer,Sequence> Customer_ID=new TreeMap<>();//?X???C,?X????
		double N=0.017;
		//-----------------------------------------------------------------
		HashSet<String> item01=new HashSet<>();//??~
		TreeMap<String,Headtable_item> Headtable=new TreeMap<>();
		ArrayList<String> list = new ArrayList<String>();//[B, C, E, A, I]
		double time1, time2, time3,time4,time5;
		time1 =time3 = System.currentTimeMillis();
		Runtime runtime = Runtime.getRuntime();
		long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\FUSP.txt"), "UTF8"));
		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Gino\\Desktop\\??s????\\????????\\FUSP\\1-5plus6\\1-5.csv"), "UTF8"));
		//----------------------------------------------------------------------------------------------
		int a=0;
		HashSet<String> item02=new HashSet<>();//??~ 
		TreeMap<String,Headtable_item> LOLN=new TreeMap<>();
		TreeMap<String,Headtable_item> SOLN=new TreeMap<>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> Rescan = new ArrayList<String>();
		ArrayList<String> newlist = new ArrayList<String>();
		HashSet<String> newCust=new HashSet<>();
		ArrayList<String> toBedeletedNodes = new ArrayList<String>();
		ArrayList<Node> toBeFind = new ArrayList<Node>();
		double new_min_sup=0;
		double all_new_sup=0;
		HashSet<String> Cust02=new HashSet<>();//?s?W???H
		HashSet<String> Newcust=new HashSet<>();//?s?W???H~(?s?W??????)
		TreeMap<Integer,Sequence> NewCustomer_ID=new TreeMap<>();
		boolean bEnd = false;
		int nNowDay = 1;
		int nOldDay = 1;
		LinkedList<String> print=new LinkedList<>();
		//-------------------------------------------------------------------------------------------------
		
	     
	     
	   
		while(bEnd == false)
		{
			System.out.print("??"+nNowDay+"??");
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\12-1.txt"), "UTF8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Gino\\Desktop\\??s????\\????????\\12-4test\\S10I2N1KD1K-3.csv"), "UTF8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Gino\\Desktop\\??s????\\????????\\12-4test\\S4I2N1KD1K-5.csv"), "UTF8"));

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("S4I2N1KD1K-3.csv"), "UTF8"));
			FileWriter writer = new FileWriter("FUSP421.txt");

			while ((line = br.readLine()) != null) 	
			{
				int c = line.charAt(0);
				if(c == 65279)
				{
				   line = line.substring(1, line.length());
				}
				data=line.split(",");
				//System.out.println(line);
				int Readday = Integer.parseInt(data[0]);
				if(Readday == nNowDay && Readday ==1)
				{
					if(Cust01.contains(data[1])==false)
					{
						Cust01.add(data[1]);
						Sequence ss=new Sequence();
						ss.setCustomer_id(data[1]);
						ss.AddSequence(data[0],data[1],data[2]);
						Customer_ID.put(Integer.parseInt(data[1]),ss);
					}
					else
					{
						Customer_ID.get(Integer.parseInt(data[1])).AddSequence(data[0],data[1],data[2]);
					}
					min_sup=Cust01.size()*N;		
					//--------------------------------------------------------------------------------------
					if(item01.contains(data[2])==false)
					{
						item01.add(data[2]);
						Headtable_item ii=new Headtable_item();//item_id,????
						ii.Addtable(data[1]);
						ii.setHeadtable_itemid(data[2]);
						Headtable.put(data[2], ii);
					}
					else
					{
						Headtable.get(data[2]).Addtable(data[1]);	
					}
				}
				else if(Readday == nNowDay && Readday!=1)
				{
					
					if(Cust01.contains(data[1])==false && Cust02.contains(data[1])==false )
					{
						//Cust01.add(data[1]);
						Sequence ss=new Sequence();
						ss.setCustomer_id(data[1]);
						ss.AddSequence(data[0],data[1],data[2]);
						Customer_ID.put(Integer.parseInt(data[1]),ss);
						Cust02.add(data[1]);
						NewCustomer_ID.put(Integer.parseInt(data[1]),ss);
					}
					else
					{
						Customer_ID.get(Integer.parseInt(data[1])).AddSequence(data[0],data[1],data[2]);
						Cust02.add(data[1]);
						NewCustomer_ID.put(Integer.parseInt(data[1]),Customer_ID.get(Integer.parseInt(data[1])));
					}
				
					//--------------------------------------------------------------------------------------------
					if(item02.contains(data[2])==false )
					{
						
						item02.add(data[2]);
						Headtable_item ii2=new Headtable_item();//item_id,????
						ii2.Addtable(data[1]);
						ii2.setHeadtable_itemid(data[2]);
						if(Headtable.containsKey(data[2])==true)
						{
							Headtable.get(data[2]).Addtable(data[1]);	
						}
						else
						{
							Headtable.put(data[2], ii2);
						}
					
					}
					else
					{
						
						Headtable.get(data[2]).Addtable(data[1]);	
					}
					
				}
				
				else if(Readday>nNowDay)
				{
					
					System.out.println();
					nNowDay++;
					break;
				}
		
			}
			if((line = br.readLine()) == null)
			{
			 //time5 = System.currentTimeMillis();
			
				bEnd = true;
				br.close();
			}
			Node treeRootNode = new Node(null);
			treeRootNode.setId("root");
			treeRootNode.count=0;
			ArrayList<Node> NodeList = new ArrayList<>();
			if(nNowDay==2)
			{
				for(String k:Headtable.keySet())
				{		
					
					//Headtable.get(k).show(min_sup);
					SortSupport(min_sup, list, Headtable,k);
				
				}
				
					//System.out.println("item: "+list);
					System.out.println("min_sup: "+min_sup);
					System.out.println("---------------------------------------");
					
			
				for(Integer k:Customer_ID.keySet())
				{	
					//Customer_ID.get(k).show();	
					BuiltTree( NodeList,Customer_ID,treeRootNode,k,list,Headtable);	
				}
					printTree(treeRootNode, "   --->");	
					
				for(int i=0;i<list.size();i++)
			    {	
				 if(Headtable.get(list.get(i)).Position.isEmpty()==false)
	    		 {
	        		 
	    			 for(Node Node :Headtable.get(list.get(i)).Position)
	    			{
	    				
	    				if(Node.getRoot().equals(treeRootNode)==true)
	    				{
	    				    toBeFind.add(Node);
	    				   
	    				 }
	    				 
	    			} 
	    			
	    			
	    			// System.out.println("Seqlist"+newlist.get(i));
	    			 if( toBeFind.isEmpty()==false)
	    			 {
	    				 for(int i2=0;i2<toBeFind.size();i2++)
	    				 {
	    			//	System.out.println(toBeFind.get(i2));
	    			
	    				
	    				 FindParent(toBeFind.get(i2),Seqlist,i2,toBeFind);
	    				
	    				 if(Seqlist.isEmpty()==false)
	    				 {	 
	    				//	System.out.println("Seqlist"+Seqlist);
	    				    // System.out.println("2312"+Seqlist.get(0));
	    					// ///System.out.println("789"+toBeFind.get(i2).id);
	    					 	//
	    					 String Sequence=null;
	    					 	
	    						FindSeq(toBeFind.get(i2).id,toBeFind.get(i2),Sequence,Seqlist.size(),0,toBeFind.get(i2),0);
	    						Sequence=null;
	    						// System.out.println("size:"+Seqlist.size());
	    						//System.out.println(Customer_ID.get(10622).Customer_Sequence);
	    						//System.out.println(Customer_ID.get(16781).Customer_Sequence);
	    						//System.out.println(Customer_ID.get(1562).Customer_Sequence);
	    						//System.out.println(Customer_ID.get(10137).Customer_Sequence);
	    						Seqlist.removeAll(Seqlist);
	    				
	    				 }
	    			 } 
	    			 toBeFind.removeAll(toBeFind);
	    			 }
	    		 }
	    			
	        }
				time5= System.currentTimeMillis();
				String a2=null;
				a2=nNowDay-1+"??l = "+  (time5-time1)/1000 + "??";
				print.add(a2);
			}
			
					/*for(String k:Headtable.keySet())
					{
						//System.out.println("id : "+Headtable.get(k).Headtable_itemid);
						Headtable_item temp=new Headtable_item();
						temp=Headtable.get(k);
						HashSet<Node> customerlist=new HashSet<>();
						//customerlist=temp.Position;
						for(Object Node :temp.Position)
						{
							System.out.println(temp.Headtable_itemid+" : "+temp.Position);
						}
						System.out.println(temp.Position);
					}*/
					//System.out.println("---------------------------------------");
				//-----------------insert-------------------------------------------------------------------------------------
				
			else{	
				
			// Cust01 & Cust02->???t????11,12,13,14
				Newcust.clear();
				Newcust.addAll(Cust02);
				Newcust.removeAll(Cust01);
				new_min_sup=N*(Newcust.size());//?s??databace.support
				all_new_sup=N*Customer_ID.size();
			// Cust01 & Cust02->???p???? 1 ~ 14 
				//result.clear();
		       // result.addAll(set1);
		       // result.addAll(set2);
				
				for(String k2:Headtable.keySet())
				{		
					
					//Headtable2.get(k2).show2(new_min_sup);
					 
					//case 1
					if(list.contains(k2)==true && item02.contains(k2)==true && Headtable.get(k2)!=null)//????Olarge->?s???Olarge
					{
						LOLN.put(Headtable.get(k2).Headtable_itemid,Headtable.get(k2));
					}
					
					if(list.contains(k2)==false && item02.contains(k2)==true && Headtable.get(k2)!=null )//????Osmall->?s???Olarge
					{
						SOLN.put(Headtable.get(k2).Headtable_itemid,Headtable.get(k2));
					}
					
					
				}
				//System.out.println("new_item: "+list2);
				System.out.println("Newcust:new_min_sup: "+new_min_sup);
				System.out.println("ALLcust:all_min_sup: "+all_new_sup);
				
				//System.out.println("item01"+item01);
				//System.out.println("item02"+item02);
				//System.out.println("list"+list);
				//System.out.println("list2"+list2);
				/*System.out.println("---------------------------------------");
				
				for(Integer k:Customer_ID.keySet())
				{	
					Customer_ID.get(k).show();	
				}
				System.out.println("---------------------------------------");
				for(Integer k3:NewCustomer_ID.keySet())
				{	
					NewCustomer_ID.get(k3).show();
				}
				System.out.println("Headtable ");
				for(String k2:Headtable.keySet())
				{
					
					System.out.print(k2+" ");
				}
				System.out.println();*/
				//-------------------------------------------------------------
				//CASE 1
				System.out.println("????Olarge->?s???Olarge:");
				for(String k4:LOLN.keySet())
				{	
				   SortSupport(all_new_sup, list2, LOLN,k4);
				   //System.out.println(LOLN.get(k4).Headtable_itemid);
				   //LOLN.get(k4).show(all_new_sup);
				}
				//System.out.println("list2 :"+list2);
				//CASE 2 ->???? [a,i]
				toBedeletedNodes.clear();
				toBedeletedNodes.addAll(list);
				toBedeletedNodes.removeAll(list2);
				for(String k2:Headtable.keySet())
				{
					if(toBedeletedNodes.contains(k2))
					{
						if(Headtable.get(k2).count>all_new_sup)
						{
							toBedeletedNodes.remove(k2);
							list2.add(k2);
						}
					}
					
				}
		        System.out.println("?t???G(????)"+toBedeletedNodes);
		        removeSelectedNodes(treeRootNode,  toBedeletedNodes, 0,Headtable);
		        
		        System.out.println("n<------- Tree after  deletion of nodes --->");
		       // printTree(treeRootNode, " ");
		       
		       /* for(String k:Headtable.keySet())
				{
					//System.out.println("id : "+Headtable.get(k).Headtable_itemid);
					Headtable_item temp=new Headtable_item();
					temp=Headtable.get(k);
					HashSet<Node> customerlist=new HashSet<>();
					//customerlist=temp.Position;
					for(Object Node :temp.Position)
					{
						System.out.println(temp.Headtable_itemid+" : "+temp.Position);
					}
					System.out.println(temp.Position);
				}
		        */
		        
				System.out.println("---------------------------------------");
				
				System.out.println("????Osmall->?s???Olarge:");
				for(String k5:SOLN.keySet())
				{	
					//System.out.println(SOLN.get(k5).Headtable_itemid);
					//SOLN.get(k5).show(all_new_sup);
					SortSupport(all_new_sup, Rescan, SOLN,k5);
				}
				
				//System.out.println("Rescan"+Rescan);
				//System.out.println("list2"+list2);
				//System.out.println("list"+list);
				//System.out.println("Cust02"+Cust02);
		        newlist.clear();
		        newlist.addAll(list2);
		        newlist.addAll(Rescan);
		       // System.out.println("newlist?G"+newlist);
		        newCust.clear();
		        newCust.addAll(Cust02);
		        newCust.removeAll(Cust01);
		       
		        //case 03
		        HashSet<Integer> removeCID= new HashSet<Integer>();
		        ArrayList<String> Case3 = new ArrayList<String>();
		        for(Integer k:Customer_ID.keySet())
				{
		        	
		        		if(Cust01.contains(Integer.toString(k))==true )
		        		{
		        			Case3.removeAll(Case3);
		        			for(int i=0;i<Rescan.size();i++)
				        	{
		        				int len=Customer_ID.get(k).Customer_Sequence.size();
		        					for(int j=1;j<len;j++)
		        					{	      
		        						if(Customer_ID.get(k).Customer_Sequence.get(j).contains(Rescan.get(i))==true)
		        						{
		        						   if(Case3.contains(Rescan.get(i))==false)
		        						   {
		        							   removeCID.add(k);
		        						   }
		        						   
		        						}
		        						
		        						
		        					}
		        					
				        	}
		        			 
		      		        
		        		}	
				}
		        removeCidNodes(treeRootNode, removeCID, 0,Headtable);
		        ArrayList<String> FINAL = new ArrayList<String>();
		        FINAL.clear();
		        FINAL.addAll(Rescan);
		        FINAL.addAll(list2);
		        for(Integer k:Customer_ID.keySet())
				{	
					BuiltTree( NodeList,Customer_ID,treeRootNode,k,FINAL,Headtable);
				}
		        System.out.println("n<------- Tree after  add of nodes --->");
		       // printTree(treeRootNode, " ");	
		        System.out.println("n<------- release --->");
		        Cust01.addAll(Cust02);
		        Cust02.clear();
		        item01.addAll(item02);
		        item02.clear();
		        newCust.clear();
		        Newcust.clear();
		        LOLN.clear();
		        SOLN.clear();
		        NewCustomer_ID.clear();
		        
		       list.clear();
		       for(String k:Headtable.keySet())
				{		
					
					//Headtable.get(k).show(min_sup);
					SortSupport(all_new_sup, list, Headtable,k);
				
				}
		       	list2.clear();
		        NodeList=null;
		        Rescan.clear();
		        toBedeletedNodes.clear();
		        
		        
		       System.out.println("n<------- Sequence of Tree --->");
		      
		        for(int i=0;i<newlist.size();i++)
		        {
		        	//newlist.get(i);
		        	
		        	//FindSeq(i,newlist,newlist,Headtable, treeRootNode,finlist);
		        	 if(Headtable.get(newlist.get(i)).Position.isEmpty()==false)
		    		 {
		        		 
		    			 for(Node Node :Headtable.get(newlist.get(i)).Position)
		    			{
		    				
		    				if(Node.getRoot().equals(treeRootNode)==true)
		    				{
		    				    toBeFind.add(Node);
		    				   
		    				 }
		    				 
		    				 //toBeFind B:B->B
		    			} 
		    			
		    		
		    			// System.out.println("Seqlist"+newlist.get(i));
		    			 if( toBeFind.isEmpty()==false)
		    			 {
		    				 for(int i2=0;i2<toBeFind.size();i2++)
		    				 {
		    			//	System.out.println(toBeFind.get(i2));
		    			
		    				
		    				 FindParent(toBeFind.get(i2),Seqlist,i2,toBeFind);
		    				
		    				
		    				
		    				 if(Seqlist.isEmpty()==false)
		    				 {	 
		    				//	System.out.println("Seqlist"+Seqlist);
		    				    // System.out.println("2312"+Seqlist.get(0));
		    					// ///System.out.println("789"+toBeFind.get(i2).id);
		    					 	//
		    				String Sequence=null;
		    				
		    				
		    						FindSeq(toBeFind.get(i2).id,toBeFind.get(i2),Sequence,Seqlist.size(),0,toBeFind.get(i2),0);
		    						
		    						Sequence=null;
		    						Seqlist.clear();
		    				
		    				 }
		    				 if(i2==toBeFind.size())
		    				 {
		    					 Headtable.remove(newlist.get(i)); 
		    					 break;
		    				 }
		    			 } 
		    			 toBeFind.clear();
		    			
		    			 }
		    		 }
		        	 newlist.remove(i);	
		        }
		        System.gc();
		       int a3 =0;
		       for(String k9:Resualt.keySet())
		       {
		    	   
		    	   //System.out.println(all_new_sup);
		    	   if(Resualt.get(k9).count>all_new_sup )
		    	   {
		    		   a3++;
		    		// System.out.println(k9+"-->count: "+Resualt.get(k9).count+"-->CID: "+Resualt.get(k9).SECID);
		    		  
		    	   }
		    	   else
		    	   {
		    		   Resualt.remove( Resualt.get(k9));  
		    	   }
		    	   
		    	   
		       }
		       System.out.println("Resualt.size()"+Resualt.size());
		       treeRootNode.deleteRootNode();
		       treeRootNode.deleteNode(Headtable);
		       //Resualt.clear();
		       CID.clear();
		       PAUSE.clear();
		       Seqlist.clear();
		       
		   System.out.println(a3);
		   //if(nNowDay==8 ||nNowDay==15 ||nNowDay==22 || nNowDay==29 ||  nNowDay==36|| nNowDay==43 ||  nNowDay==50 || nNowDay==57 || nNowDay==64 ||  nNowDay==71 || nNowDay==78 ||nNowDay==85 || nNowDay==92 || nNowDay==99|| nNowDay==106 ||nNowDay==113)
			 if(nNowDay==8 ||nNowDay==15 ||nNowDay==22 || nNowDay==29 ||  nNowDay==36|| nNowDay==43 ||  nNowDay==50 || nNowDay==57 || nNowDay==64 ||  nNowDay==71 || nNowDay==78 ||nNowDay==85 || nNowDay==92 || nNowDay==99|| nNowDay==106 ||nNowDay==113||nNowDay==120||nNowDay==127||nNowDay==134||nNowDay==141||nNowDay==148||nNowDay==155||nNowDay==162||nNowDay==169)

			{
				double temp=0;
				
				time4 = System.currentTimeMillis();
				System.out.println(nNowDay+"??l = "+  (time4-time3)/1000 + "??");
				
				
				String a2=null;
				a2=nNowDay-1+"??l = "+  (time4-time3)/1000 + "??";
				print.add(a2);
				//writer.append(nNowDay+"??l = "+  (time4-time3)/1000 + "??");
				//writer.append("\n");
				temp=time4;
				time4=time3;
				time3=temp;
				
			}
		   for(String c3 : print)
			{
				System.out.println(c3);
				writer.append(c3);
				writer.append("\n");
				writer.flush();
			}
		      // System.out.println(p);
		      // System.out.println(Customer_ID.get(10622).Customer_Sequence);
		    //   System.out.println("-----------------------------------------------------");
		      
		      //  for(Node Node : Headtable.get("72").Position)
    			//{
		      //  	 System.out.println(Node.id+" "+Node.btogether);
    			//}
		 	}      
		}
		time2 = System.currentTimeMillis();
		System.out.println("nNowDay"+nNowDay+"???:"+  (time2-time1)/1000 + "??");
		     //   System.out.println("1???:"+  (time2-time1)/1000 + "??");
		long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Memory increased:" + (usedMemoryAfter-usedMemoryBefore)/1024/1024+" MB");
	}

	
		
			
				
			
			
			
	
	

	private static <Seqlist> void FindSeq(String string2,Node node3, String sequence, int i, int j, Node node, int l)
	{
		
		HashSet<Boolean> together=new HashSet<>();
		for(;j<i;j++)
		{
			CID.clear();
			CID.addAll(Seqlist.get(j).CID);
			CID.retainAll(node.CID);
			
			findtogether(together,l,j,Seqlist);
			
			//System.out.println("string2"+string2);
		
			String S2[]=string2.split("%");
			//System.out.println("S2.length"+S2.length);
			if(S2.length==1)
			{
				if(node3.btogether==false || together.contains(false)==true  )
				{
					sequence=Seqlist.get(j).id+"%"+string2;
				}
				else if(node3.btogether==true && together.contains(true)==true &&  together.contains(false)==false || (node3.btogether==true && Seqlist.get(j).level==node3.level-1))
				{
					sequence="("+"%"+Seqlist.get(j).id+"%"+string2+"%"+")";		
				}
			}
			else if(S2.length!=1)
			{
				if((node3.btogether==true && Seqlist.get(j).level==node3.level-1) || node3.btogether==true && together.contains(true)==true &&  together.contains(false)==false )
				{
					if(string2!=null )
					{
						
						String Stex7[]=string2.split("%");
						
						int k=0;
						k=Stex7[0].length();
						if(Stex7[0].contains("(")==true)
						{	
							
							sequence="(%"+Seqlist.get(j).id+string2.subSequence(k,string2.length());
							
							
						}
						else
						{
							sequence="(%"+Seqlist.get(j).id+"%"+Stex7[0]+"%)"+string2.subSequence(k,string2.length());							
						}
					}
					
				}
				else if(node3.btogether==false || together.contains(false)==true )
				{
					sequence=Seqlist.get(j).id+"%"+string2;
				}
				else 
				{
					System.out.println("?????~");
				}
				
			}
			if(Resualt.containsKey(sequence)==false)
			{
				FinSeq s= new FinSeq();
				s.Seq_id=sequence;
				s.SECID.addAll(CID);
				s.count=CID.size();
				Resualt.put(sequence, s);
			}
			else
			{
				PAUSE.clear();
			    PAUSE.addAll(Resualt.get(sequence).SECID);
			    PAUSE.addAll(CID);
				Resualt.get(sequence).SECID.addAll(PAUSE);
				Resualt.get(sequence).count=Resualt.get(sequence).SECID.size();
				
			}
			//System.out.println(sequence);
			/*System.out.println("--------------------------------------");
			System.out.println(sequence);
			System.out.println(CID);
			System.out.println("seqlist"+seqlist);
			System.out.println("nodeid"+node.id);
			System.out.println("seqlist.get(j).id: "+seqlist.get(j).id);
			System.out.println("string2: "+string2);
			System.out.println("node.btogether "+node.btogether);
			System.out.println("node3.btogether "+node3.btogether);
			System.out.println("seqlist.get(j) "+seqlist.get(j).btogether);
			if( seqlist.get(j).level==node3.level-1)
			{
				System.out.println("seqlist.get(j).level==node3.level-1"+"   yes");
			}
			else
			{
				System.out.println("seqlist.get(j).level==node3.level-1"+"   no");
			}
			
			System.out.println("together.contains(false) "+together.contains(false));
			System.out.println("together "+together);
			System.out.println("test"+test);
			System.out.println("--------------------------------------");*/
			
			
			if(j<i && Seqlist.get(j)!=null)
			{
				//together.removeAll(together);
				//test.removeAll(test);
				String a=null;
				a=sequence.toString();
				FindSeq(a,Seqlist.get(j),sequence,i,j+1,node,j+1);
				
			}
			
			
		}
		
		
	}
	
	private static void findtogether(HashSet<Boolean> together, int j, int i,ArrayList<Node> seqlist)
	{
		for(;j<i;j++)
		{
			//System.out.println("id"+seqlist.get(j).id);
			//System.out.println("seqlist.get(j).cid"+seqlist.get(j).CID);
			together.add(seqlist.get(j).btogether);
			
		}
		
	}
	public static void FindParent(Node node,ArrayList<Node> Seqlist,Integer i, ArrayList<Node> toBeFind) 
	{
		
		
		
		if(node.parent.CID.isEmpty()==false && node.parent.id!="root")
		{	
			Seqlist.add(node.parent);
			
			   FindParent(node.parent,Seqlist,i,toBeFind);
			
		}
		else
		{
			return;
		}
	}
	

	
	
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     