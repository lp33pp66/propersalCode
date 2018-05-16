package fusp;


import fusp.FUSPNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FUSPTree{
    //header table
    List<Integer> headerList = null;

    Map<Integer, FUSPNode> mapItemNodes = new HashMap<>();
    Map<Integer, FUSPNode> mapItemLastNodes = new HashMap<>();

    FUSPNode root = new FUSPNode();

    public FUSPTree(){
        
    }

    void show(){

    }
    
    public void showTree(FUSPNode ptr){
        ptr = this.root;
        if(!ptr.childs.isEmpty()){
            for(FUSPNode each : ptr.childs){
                showTree(each);
            }
        }else{
            System.out.println("t : "+ptr.itemID);
        }

    }

    
    // add a:0, b:1, c:0
    public void addRescanTranscation(ArrayList<ArrayList<Integer>> retran, Integer item)throws IOException{
        FUSPNode currNode =  root;
        //一條一條家
        //System.out.println("test");
        //System.out.println("retran"+retran);
        for (ArrayList<Integer> itemList : retran) {
            // 買一個
            //System.out.println("itemlist : " + itemList);
            //System.out.println(itemList.size());
            if(itemList.size()<1) break;
            FUSPNode child = currNode.getChildWithID((int)itemList.get(0), "S");
            if (child == null) {
                System.out.println("check");
                FUSPNode newNode = new FUSPNode();
                newNode.itemID = (int)itemList.get(0);
                if(newNode.itemID != item){
                    newNode.supCount = 0;
                }
                newNode.parant = currNode;
                currNode.childs.add(newNode);
                currNode = newNode;
                fixNodeLinks((int)itemList.get(0), currNode);
            } else {
                child.supCount++;
                if(child.itemID != item){
                    child.supCount--;
                }
                currNode = child;   
            }
            
            //或多個
            if(itemList.size() > 1){
                //System.out.println("more");
                for(int i = 1; i < itemList.size(); i++){
                    //System.out.println("itemList.get(i)"+itemList.get(i));
                    child = currNode.getChildWithID((int)itemList.get(i), "I");
                    //System.out.println("child : " + child);
                    //System.out.println("child2 "+child);
                    if (child == null) {
                        FUSPNode newNode = new FUSPNode();
                        newNode.itemID = (int)itemList.get(i);
                        if(newNode.itemID != item){
                            newNode.supCount =0;
                        }
                        newNode.nodeLink = "I";
                        newNode.parant = currNode;
                        currNode.childs.add(newNode);
                        currNode = newNode;
                        fixNodeLinks((int)itemList.get(i), currNode); 
                        
                    } else {
                        child.supCount++;
                        if(child.itemID != item){
                            child.supCount--;
                        }
                        currNode = child;   
                    }
                }
            }
        }
        System.out.println("ADD RESCANTION");

    }

    public void addTranscation(ArrayList<ArrayList<Integer>> transcation){       
        FUSPNode currNode =  root;
        //System.out.println("ROOT");
        //一條一條家
        for (ArrayList<Integer> itemList : transcation) {
            //System.out.println("itemList " + itemList);
            // 買一個
            FUSPNode child = currNode.getChildWithID((int)itemList.get(0), "S");
            if (child == null) {
                FUSPNode newNode = new FUSPNode();
                newNode.itemID = (int)itemList.get(0);
                newNode.parant = currNode;
                currNode.childs.add(newNode);
                currNode = newNode;
                fixNodeLinks((int)itemList.get(0), currNode);
            } else {
                child.supCount++;
                currNode = child;   
            }
            //或多個
            if(itemList.size() > 1){
                for(int i = 1; i < itemList.size(); i++){
                    child = currNode.getChildWithID((int)itemList.get(i), "I");
                    if (child == null) {
                        FUSPNode newNode = new FUSPNode();
                        newNode.itemID = (int)itemList.get(i);
                        newNode.nodeLink = "I";
                        newNode.parant = currNode;
                        currNode.childs.add(newNode);
                        currNode = newNode;
                        fixNodeLinks((int)itemList.get(i), currNode); 
                        
                    } else {
                        child.supCount++;
                        currNode = child;   
                    }
                }
            }
            System.out.println("Addtranscation OK");
        }  
    }

        private void fixNodeLinks(Integer item, FUSPNode newnode){
            FUSPNode lastNode = mapItemLastNodes.get(item);
            if(lastNode != null){
                lastNode.headerLink = newnode;
            }
            mapItemLastNodes.put(item, newnode);

            FUSPNode headernode = mapItemNodes.get(item);
            if(headernode == null){
                mapItemNodes.put(item, newnode);
            }
        }

        void creatHeaderList(final Map<Integer, Integer> mapSupport)throws IOException{          
            /*System.out.println("mapsup : "+mapSupport);
            System.out.println("mapitemNode : " + mapItemNodes.keySet());
            System.out.println("headerlist : " + headerList);*/
            headerList = new ArrayList<Integer>(mapItemNodes.keySet());
            //System.out.println("headerLisr2: "+ headerList);
            if(headerList.size() > 1){
                Collections.sort(headerList, new Comparator<Integer>(){
                    public int compare(Integer id1, Integer id2){
                        int compare = mapSupport.get(id2) - mapSupport.get(id1);
                        return (compare == 0) ? (id1 - id2) : compare;
                    }
                });
            }
        }

        //傳入要刪除的Item 
        void deleteNode(Integer delItemNode){   
            FUSPNode delNode = mapItemNodes.get(delItemNode);      //find the item node   
                //FUSPNode nextDelNode = delNode.headerLink;     // next del node
              
                while(delNode != null){

                    //last node no child
                    if(delNode.isLastNodeinPath()){
                        delNode.parant.childs.remove(delNode);
                    }else{
                        //找出節點的位置
                        int index = delNode.parant.childs.indexOf(delNode);
                        
                        for(FUSPNode currchild : delNode.childs){
                            delfunc(currchild, delNode.parant);
                            delNode.parant.childs.remove(index);
                        }         
                    }
                    delNode = delNode.headerLink;//next
                }
                //System.out.println("mapitem : " + mapItemNodes);
                mapItemNodes.remove(delItemNode);
                //System.out.println("mapitem2 : " + mapItemNodes);
        }

        private void delfunc(FUSPNode node, FUSPNode Pnode){
            FUSPNode comparenode = node.iscontain(Pnode.childs);
            if(comparenode != null){
                //data++
                comparenode.supCount += node.supCount;//data++
                
                //headerLink
                FUSPNode first= mapItemNodes.get(comparenode.itemID);
                while(first.headerLink != node){
                    first = first.headerLink;
                }
                first.headerLink = node.headerLink;

                //relrase
                FUSPNode Tnode = node;
                node = null;
                for(FUSPNode each : node.childs){
                    delfunc(each, comparenode);
                }
            }else{
                Pnode.childs.add(node);
                node.parant = Pnode;
            }
            
        }

        void addPrefixPath(List<FUSPNode> prefixPath, Map<Integer, Integer> mapSupportBeta, int relativeMinsupp){
            // the first element of the prefix path contains the path support
            int pathCount = prefixPath.get(0).supCount;  
                    
            FUSPNode currentNode = root;
            // For each item in the transaction  (in backward order)
            // (and we ignore the first element of the prefix path)
            for(int i = prefixPath.size() -1; i >=1; i--){ 
                FUSPNode pathItem = prefixPath.get(i);
                // if the item is not frequent we skip it
                if(mapSupportBeta.get(pathItem.itemID) >= relativeMinsupp){

                    // look if there is a node already in the FP-Tree
                    FUSPNode child = currentNode.getChildWithID(pathItem.itemID, "S");
                    if(child == null){ 
                        // there is no node, we create a new one
                        FUSPNode newNode = new FUSPNode();
                        newNode.itemID = pathItem.itemID;
                        newNode.parant = currentNode;
                        newNode.supCount = pathCount;  // set its support
                        currentNode.childs.add(newNode);
                        currentNode = newNode;
                        // We update the header table.
                        // and the node links
                        fixNodeLinks(pathItem.itemID, newNode);		
                    }else{ 
                        // there is a node already, we update it
                        child.supCount += pathCount;
                        currentNode = child;
                    }
                }
            }
        }
    

}
    