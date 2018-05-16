package fusp;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FUSPNode{
     int itemID = -1;
     int supCount = 1;
     FUSPNode parant = null;
     List<FUSPNode> childs = new ArrayList<FUSPNode>();
     String nodeLink = "S";   // S-step or I-step
     FUSPNode headerLink ;
     //list size = count1
     List<Integer> cidpathlist = new ArrayList<Integer>();
     
     public FUSPNode(){

     }

     //check node in child
     FUSPNode getChildWithID(int id, String nodelink){
         for (FUSPNode child : childs) {
             if(child.itemID == id && child.nodeLink == nodelink){
                 return child;
             }   
         }
         return null;
     }

     List<FUSPNode> getChilds(){
        return childs;
     }

     boolean isEqueal(FUSPNode currNode){
         if(this.itemID == currNode.itemID && this.nodeLink == currNode.nodeLink ){
             return true;
         }else{
             return false;
         }
     }

     FUSPNode iscontain(List<FUSPNode> lst){
        FUSPNode bool = null;
        for(FUSPNode node : lst){
            if(this.itemID == node.itemID && this.nodeLink == node.nodeLink){
                bool = node;
            }
        }
        return bool;
     }

     boolean isLastNodeinPath(){
         return (this.childs.isEmpty()) ? true:false;
     }

     FUSPNode getParent(){
         return parant;
     }
     List<FUSPNode> getChild(){
         return childs;
     }
     int getItemID(){
         return itemID;
     }



}

