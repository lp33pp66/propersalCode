package tree;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
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
class Node {
	 String id;
	 ArrayList<Node> children = new ArrayList<>();
	 //ArrayList<Node> insection = new ArrayList<>();
	 TreeMap<String,Node> childrenS=new TreeMap<>();
	 TreeMap<String,Node> childrenI=new TreeMap<>();
	 Node parent;
	 int count=0;
	 int level=0;
	 HashSet<Integer> CID=new HashSet<>();
	 
	 boolean btogether;
	 
	 public Node getRoot() {
	  if(parent == null){
	   return this;
	  }
	  return parent.getRoot();
	 }
	 /* public int count()
	  {
		  return count;  
	  }*/
	 public Node deleteRootNode() 
	 {
		  if (parent != null) 
		  {
		   throw new IllegalStateException("delete root node can be only called on the root node of tree");
		  }
		  	Node newParent = null;
		  if (!getChildren().isEmpty()) 
		  {
		   newParent = getChildren().get(0);
		   newParent.setParent(null);
		   getChildren().remove(0);
		   
		   	for (Node each : getChildren()) 
		   	{
		   		each.setParent(newParent);
		    }
		   		newParent.getChildren().addAll(getChildren());
		  }
		  		this.getChildren().clear();
		  		return newParent;
	}
	
	public void deleteNode(TreeMap<String,Headtable_item> Headtable) 
	{
		
		if (parent != null) 
		{
			int index = this.parent.getChildren().indexOf(this);
			//System.out.println("index"+index);
		    this.parent.getChildren().remove(this);
		    for (Node each : getChildren()) 
		    {
		    	//System.out.println("����"+each.id);
		    	each.setParent(this.parent);
		    	each.level=this.level;
		    	//System.out.println("getChildre"+this.getChildren());
		    	//System.out.println("parent.getChildren()"+this.parent.getChildren());
		    	
		    }
		  
		    for(int j=0;j<this.parent.getChildren().size();j++)
	    	{
	    		for(int k=0;k<this.getChildren().size();k++)
	    		{
	    			if(this.level==1 && this.btogether==true)
	    			{
	    				this.btogether=false;
	    			}
	    			
		    		if((this.parent.getChildren().get(j).id.equals(this.getChildren().get(k).id )) && (this.parent.getChildren().get(j).btogether==this.getChildren().get(k).btogether))
		    		{
		    			
		    			if(this.getChildren()!=null)
		    			{
		    				//System.out.println(this.getChildren().get(k));
		    				//System.out.println(this.parent.getChildren().get(j)+"dsogjogjso"+this.getChildren().get(k));
		    				//this.parent.getChildren().get(j).count=this.parent.getChildren().get(j).count + this.getChildren().get(k).count;
		    				//this.parent.getChildren().get(j).CID.add(this.parent.getChildren().get(j).CID);
		    				
		    				this.parent.getChildren().get(j).CID.addAll(this.getChildren().get(k).CID);
		    				this.parent.getChildren().get(j).count=this.parent.getChildren().get(j).CID.size();
		    				//Headtable.get(this.parent.getChildren().get(k).id).Position.remove(this.getChildren().get(j));
		    				//Headtable.get(this.getChildren().get(k).id).Position.remove(this.getChildren().get(k));
		    				this.getChildren().remove(this.getChildren().get(k));

		    				//Headtable.get(this.parent.getChildren().get(j).id).Position.add(this.parent.getChildren().get(j));
		    				
		    			}
		    		}
	    		}
	    	}   
		    	
		    	this.parent.getChildren().addAll(index, this.getChildren());
		    	
		    	
		}
				this.getChildren().clear();
	}
	public Node(Node parent) 
	{
	  this.parent = parent;
	}
	 
	public String getId() 
	{
	  return id;
	}
	 
	public void setId(String id)
	{
	  this.id = id;
	}
	 
	public ArrayList<Node> getChildren() 
	{
	  return children;
	}
	 
	public void btogether(boolean btogether) 
	{
		 this.btogether = btogether; 
	}
	public void setParent(Node parent) 
	{
		  this.parent = parent;
	}
	 @Override
	public String toString() 
	{
	  return "Node[id:" + getId() + "]";
	}
	public Node getParent()
	{
	  return parent;
	}
	 
	}

