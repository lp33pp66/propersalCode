package fusp;



import fusp.FUSPNode;
import fusp.FUSPTree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * fusp growth()
 * 
 *  
 * 
 * 
 * 
 */




public class main_FUSP{
    //private long startT = 0;
    //private long endT = 0;
    //private int transcationcount = 0;
    private int itemsetCount; 

    //fuspGrowth
/*    private FUSPNode[] fuspNodeTempBuffer = null;
    private int maxPatternLength = 1000;
    BufferedWriter writer = null;
    private int[] itemsetOutputBuffer = null;
    private int[] itemsetBuffer = null;
    final int BUFFER_SIZE = 2000;
*/




    public static double minsup = 0.05;
    public static int nowday = 1;
    public static Set<Integer> CIDSet = new HashSet<>();

    //original customer sequence
    public static TreeMap<Integer, ArrayList<ArrayList<Integer>>> OCS = new TreeMap<>();

    //newly insertd customer sequence
    //public static TreeMap<Integer, ArrayList<ArrayList<Integer>>> NCS = new TreeMap<>();

    //K:item, V:CID  size()=supcount
    public static HashMap<Integer, HashSet<Integer>> itemsup = new HashMap<>();

    //temp lastitemsup
    //public static HashMap<Integer, HashSet<Integer>> lastitemsup = new HashMap<>();
    
    //Set<Integer> CIDset = new HashSet<>();

    //newlyTran.size = new data cid
    public static HashMap<Integer, ArrayList<Integer>> newlyTran = new HashMap<>();

    //new item sup   <item, supcount>
    public static HashMap<Integer, Integer> newlysup = new HashMap<>();

    //計算新sup
    public static void CountNewSup(int data0, int data1, int data2){
        if(newlysup.containsKey(data2)){
            newlysup.put(data2, newlysup.get(data2)+1);
        }else{
            newlysup.put(data2, 1);
        }
    }

    //確認item在同一顧客的舊序列中有出現過，則不計數
    public static void CheckItemInOldSeq(int data0, int data1, int data2){
        if(itemsup.containsKey(data2)){
            if(itemsup.get(data2).contains(data1)){
                newlysup.put(data2, newlysup.get(data2)-1);
            }else{
                //沒有出現過
            }
        }else{
            //沒有出現過
        }
    }

    //新增no資料庫
    public static void NewlyDatabase(int data0, int data1, int data2){
        if(newlyTran.containsKey(data1)){
            newlyTran.get(data1).add(data2);
        }else{
            ArrayList<Integer> arrlist = new ArrayList<>();
            arrlist.add(data2);
            newlyTran.put(data1, arrlist);
        }
    }

    //add to itemsup total sup
    public static void AddTotalSup(int data0, int data1, int data2){
        if(itemsup.containsKey(data2)){
            itemsup.get(data2).add(data1);
        }else{
            HashSet<Integer> hset = new HashSet<>();
            hset.add(data1);
            itemsup.put(data2, hset);
        }          
    }

    public static void AddtoOCS(HashMap<Integer, ArrayList<Integer>> newlyTran){
        
        for(Integer k : newlyTran.keySet()){
            if(OCS.containsKey(k)){
                OCS.get(k).add(newlyTran.get(k));
            }else{
                ArrayList<ArrayList<Integer>> aalist = new ArrayList<ArrayList<Integer>>();
                aalist.add(newlyTran.get(k));
                OCS.put(k, aalist);
            }
        }
        
    }

    public static HashMap<Integer, ArrayList<ArrayList<Integer>>> merage(HashMap<Integer, ArrayList<Integer>> newlyTran){
        HashMap<Integer, ArrayList<ArrayList<Integer>>> temp = new HashMap<>();
        for(Integer k : newlyTran.keySet()){
            if(OCS.containsKey(k)){
                //clone
                
                ArrayList<ArrayList<Integer>> aall = new ArrayList<>(OCS.get(k));
                
                aall.add(newlyTran.get(k));

                
                temp.put(k, aall);
                
            }else{
                ArrayList<ArrayList<Integer>> aalll = new ArrayList<ArrayList<Integer>>();
                aalll.add(newlyTran.get(k));
                temp.put(k, aalll);
            }   
        }
        return temp;
    }

    //sort arraylist<int>
    public static ArrayList<Integer> SortArrayList(ArrayList<Integer> alist){
        Collections.sort(alist,
            new Comparator<Integer>(){
                public int compare(Integer id1, Integer id2){
                    return itemsup.get(id1).size() - itemsup.get(id2).size();
                }
            }
        );
        return alist;        
    }
    private void fuspGrowth(FUSPTree tree) {
        for(Integer k : tree.headerList){
            FUSPNode currNode = tree.mapItemNodes.get(k);
            String str = "";
            while(currNode != null){
                str = str +currNode.itemID + ":" + currNode.nodeLink + " --- ";
                currNode = currNode.headerLink;
            }
            System.out.println(str);
        }
    }
    
    //main 
    public static void main(String[] args) throws IOException{
        HashMap<Integer, HashSet<Integer>> lastitemsup = new HashMap<>();
        BufferedReader bfr = new BufferedReader(
            new FileReader("../../DataSet/TestData/S4I2_5N100D100_SPStream.csv"));
            
        String line = "";
        boolean Endday = false; 
        //HashMap<Integer, ArrayList<Integer>> daytran = new HashMap<>();
        //ArrayList<Integer> itemlist = new ArrayList<Integer>();
        //ArrayList<ArrayList<Integer>> tran = new ArrayList<>();
        
        //item sup
        //HashMap<Integer, Integer> merageitemsup = new HashMap<>(); 
        
        long startT = 0;
        long endT = 0;
        
        
        FUSPTree Tree = new FUSPTree();
        HashMap<Integer, Integer> mapSupport = new HashMap<>();
        startT = System.currentTimeMillis();
        while(Endday == false){
            //System.out.println("lastitemsup0 : " + lastitemsup);
            
            while( (line = bfr.readLine())!= null){
                //data0 = day , data1 = CID , data2 = item
                String[] data = line.split(",");
                int data0 = Integer.parseInt(data[0]);//day
                int data1 = Integer.parseInt(data[1]);//cid
                int data2 = Integer.parseInt(data[2]);//item
                
                if(data0 == nowday){
                    CIDSet.add(data1);
                    //計算新sup
                    CountNewSup(data0, data1, data2);
                    
                    //確認item在同一顧客的舊序列中有出現過，則不計數
                    CheckItemInOldSeq(data0, data1, data2);
                    
                    //新增no資料庫
                    NewlyDatabase(data0, data1, data2);
                    
                    //add to itemsup total sup
                    AddTotalSup(data0, data1, data2);
                    
                    
                    
                }else if(data0 > nowday ){
                    System.out.println("day : " + nowday);
                    
                    //merage
                    HashMap<Integer, ArrayList<ArrayList<Integer>>> newlymerage = merage(newlyTran);
                    List<Integer> Insert_Seq = new ArrayList<>();
                    List<Integer> Rescan_Seq = new ArrayList<>();
                    List<Integer> Remove_Seq = new ArrayList<>();
                    
                    
                    //alg刪除inf--> case
                    int OldSup = (int) Math.ceil(OCS.size() * minsup) ;
                    int NewSup = (int) Math.ceil(newlymerage.size() * minsup);
                    int NowSup = (int) Math.ceil(CIDSet.size() * minsup);
                    
                    
                    //case1 case2 case3
                    for(Integer k : newlysup.keySet()){
                        
                        //case3 need rescan old on paper
                        if(lastitemsup.get(k)==null){
                            if(newlysup.get(k) >= NowSup){//case3
                                Insert_Seq.add(k);
                                Rescan_Seq.add(k);
                                //System.out.println("case3 : " + k);
                            }else{
                                //System.out.println("case4 : " + k);
                                //case4
                                //do nothing
                            } 
                        }else if(lastitemsup.get(k).size() < OldSup){//case3
                            int temp = lastitemsup.get(k).size() + newlysup.get(k);
                            if(temp > NowSup){
                                //System.out.println("case3 : " + k);
                                Insert_Seq.add(k);
                                Rescan_Seq.add(k);
                            }else{
                                //System.out.println("case4 : " + k);
                                //case4
                                //do nothing
                            }
                        }else if(lastitemsup.get(k).size() >= OldSup){//case1 case2
                            int temp = lastitemsup.get(k).size() + newlysup.get(k);
                            if(temp >= NowSup){
                                //System.out.println("case12 : " + k);
                                Insert_Seq.add(k);
                                
                            }else{
                                //remove
                                //System.out.println("case2 : " + k);
                                Tree.deleteNode(k);
                                //int idx = Tree.headerList.indexOf(k);
                                //Tree.headerList.indexOf(idx);
                                Remove_Seq.add(k);
                            }
                        }
                    }
                    
                    //System.out.println("rs " +Rescan_Seq);
                    //sort
                    Collections.sort(Rescan_Seq,
                        new Comparator<Integer>(){
                            public int compare(Integer id1, Integer id2){
                                return newlysup.get(id1) - newlysup.get(id2);
                            }
                        }
                    );
                    //System.out.println("RSS " + Rescan_Seq);
                    //System.out.println("is "  + Insert_Seq);
                    

 
 
                    //rescan 

                    //insert bug  need create addRescanTranscan() function
                    for(Integer k : Rescan_Seq){//for every rescanlist item
                        for(Integer c : itemsup.get(k)){//for every cid have this item k
                            ArrayList<ArrayList<Integer>> RescanTran = new ArrayList<>();
                            if(OCS.get(c) == null){
                                //do nothing
                            }else{
                                for(ArrayList<Integer> al : OCS.get(c)){ 
                                    ArrayList<Integer> RescanItem = new ArrayList<>();
                                    for(Integer it : al){
                                        if(itemsup.get(it).size() >= NowSup){
                                            RescanItem.add(it);
                                        }    
                                    }
                                    RescanItem = SortArrayList(RescanItem);
                                    RescanTran.add(RescanItem);
                                }  
                                //System.out.println("RESCAN : " + RescanTran);
                                Tree.addRescanTranscation(RescanTran, k);
                            }
                        }
                    }

                    //check tree----------------------------------
                    

                    //insert
                    for(Integer cid : newlymerage.keySet()){
                        ArrayList<ArrayList<Integer>> InsertTran = new ArrayList<>();
                        for(ArrayList<Integer> alist : newlymerage.get(cid)){
                            ArrayList<Integer> all = new ArrayList<>();
                            for(Integer nitem : alist){
                                if(Insert_Seq.contains(nitem)){
                                    all.add(nitem);
                                    //System.out.println("null999" + nitem);
                                }else{
                                    // do not insert
                                }
                            }
                            if(!all.isEmpty())
                            InsertTran.add(all);
                        }
                        //promblem***************************************
                        //System.out.println("InsertTran : " + InsertTran);
                        Tree.addTranscation(InsertTran);
                    }    
                    
                    //createHeader mapsupport
                    //HashMap<Integer, Integer> mapSupport = new HashMap<>();
                    for(Integer k : itemsup.keySet()){
                        //System.out.println("k ; " + k);
                        if(Insert_Seq.contains(itemsup.get(k))){
                        }
                        if(itemsup.get(k).size() >= NowSup){
                            mapSupport.put(k, itemsup.get(k).size());
                        }
                    }  
                    //System.out.println("itemsup : " + itemsup);   
                    //System.out.println("mapsupport : "+mapSupport);
                    Tree.creatHeaderList(mapSupport);
                    
                    

                    //fp-fgrowth
                    
                    //func(data0, data1, data2);


                    //add to old database
                    //System.out.println("day : "+nowday);
                   /* System.out.println("newlysup : " + newlysup);
                    System.out.println("newlytran : " + newlyTran);
                    System.out.println("newlymerage : " + newlymerage);
                    *///System.out.println("Itemsup : " + itemsup);
                    //System.out.println("lastitemsup : " + lastitemsup);
                    //lastitemsup = new HashMap<Integer, HashSet<Integer>>(itemsup);
                    for(Integer k : itemsup.keySet()){
                        HashSet<Integer> set = new HashSet<>(itemsup.get(k));
                        lastitemsup.put(k, set);
                    }
                    

                    //System.out.println("lastitemsup :: " + lastitemsup);
                    AddtoOCS(newlyTran);
                    //System.out.println("ocs : " + OCS);
                    System.out.println("-----------------------------------");
                    
                    newlysup.clear();
                    newlyTran.clear();
                    
                    //System.out.println("lastitemsup0 : " + lastitemsup);
                    //next day first
                    //計算新sup
                    CIDSet.add(data1);
                    CountNewSup(data0, data1, data2);
                    
                    //確認item在同一顧客的舊序列中有出現過，則不計數
                    CheckItemInOldSeq(data0, data1, data2);
                    
                    //新增no資料庫
                    NewlyDatabase(data0, data1, data2);
                    
                    //add to itemsup total sup
                    AddTotalSup(data0, data1, data2);
                    
                    nowday++;
                    break;
                }
            }
            if(line == null){
                Endday = true;
                bfr.close();
                System.out.println("day : " + nowday);
                //last day
                //merage
                HashMap<Integer, ArrayList<ArrayList<Integer>>> newlymerage = merage(newlyTran);
                List<Integer> Insert_Seq = new ArrayList<>();
                List<Integer> Rescan_Seq = new ArrayList<>();
                List<Integer> Remove_Seq = new ArrayList<>();
                
                //alg刪除inf--> case
                int OldSup = (int) Math.ceil(OCS.size() * minsup);
                int NewSup = (int) Math.ceil(newlymerage.size() * minsup);
                int NowSup = (int) Math.ceil(CIDSet.size() * minsup);
                
                //case1 case2 case3
                for(Integer k : newlysup.keySet()){
                    
                    //case3 need rescan old on paper
                    if(lastitemsup.get(k)==null){
                        if(newlysup.get(k) >= NowSup){//case3
                            Insert_Seq.add(k);
                            Rescan_Seq.add(k);
                            //System.out.println("case3 : " + k);
                        }else{
                            //System.out.println("case4 : " + k);
                            //case4
                            //do nothing
                        } 
                    }else if(lastitemsup.get(k).size() < OldSup){//case3
                        int temp = lastitemsup.get(k).size() + newlysup.get(k);
                        if(temp >= NowSup){
                            //System.out.println("case3 : " + k);
                            Insert_Seq.add(k);
                            Rescan_Seq.add(k);
                        }else{
                            //System.out.println("case4 : " + k);
                            //case4
                            //do nothing
                        }
                    }else if(lastitemsup.get(k).size() >= OldSup){//case1 case2
                        int temp = lastitemsup.get(k).size() + newlysup.get(k);
                        if(temp >= NowSup){
                            //System.out.println("case12 : " + k);
                            Insert_Seq.add(k);
                            
                        }else{
                            //remove
                            Tree.deleteNode(k);
                            Remove_Seq.add(k);
                        }
                    }
                }
                
                //System.out.println("rs " +Rescan_Seq);
                //sort
                Collections.sort(Rescan_Seq,
                    new Comparator<Integer>(){
                        public int compare(Integer id1, Integer id2){
                            return newlysup.get(id1) - newlysup.get(id2);
                        }
                    }
                );
                //System.out.println("RSS " + Rescan_Seq);
                //System.out.println("is "  + Insert_Seq);                
            
                //rescan
                
                // insert bug  need create addRescanTranscan() function
                for(Integer k : Rescan_Seq){//for every rescanlist item
                    for(Integer c : itemsup.get(k)){//for every cid have this item k
                        ArrayList<ArrayList<Integer>> RescanTran = new ArrayList<>();
                        if(OCS.get(c) == null){
                            //do nothing
                        }else{
                            for(ArrayList<Integer> al : OCS.get(c)){ 
                                ArrayList<Integer> RescanItem = new ArrayList<>();
                                for(Integer it : al){
                                    if(itemsup.get(it).size() >= NowSup){
                                        RescanItem.add(it);
                                    }    
                                }
                                RescanItem = SortArrayList(RescanItem);
                                RescanTran.add(RescanItem);
                            }  
                            //System.out.println("RESCAN : " + RescanTran);
                            Tree.addRescanTranscation(RescanTran, k);
                        }
                    }
                }

                //check tree----------------------------------


                //insert
                for(Integer cid : newlymerage.keySet()){
                    ArrayList<ArrayList<Integer>> InsertTran = new ArrayList<>();
                    for(ArrayList<Integer> alist : newlymerage.get(cid)){
                        ArrayList<Integer> all = new ArrayList<>();
                        for(Integer nitem : alist){
                            if(Insert_Seq.contains(nitem)){
                                all.add(nitem);
                                //System.out.println("null999" + nitem);
                            }else{
                                // do not insert
                            }
                        }
                        if(!all.isEmpty())
                        InsertTran.add(all);
                    }
                    //ppp
                    //System.out.println("InsertTran : " + InsertTran);
                    Tree.addTranscation(InsertTran);
                }
                //HashMap<Integer, Integer> mapSupport = new HashMap<>();
                for(Integer k : itemsup.keySet()){
                    if(itemsup.get(k).size() > NowSup){
                        mapSupport.put(k, itemsup.get(k).size());
                    }
                }     
                for(Integer k : itemsup.keySet()){
                    //System.out.println("k ; " + k);
                    if(Insert_Seq.contains(itemsup.get(k))){
                    }
                    if(itemsup.get(k).size() >= NowSup){
                        mapSupport.put(k, itemsup.get(k).size());
                    }
                }  
                //System.out.println("itemsup : " + itemsup);   
                //System.out.println("mapsupport : "+mapSupport);
                Tree.creatHeaderList(mapSupport);
                //fp-fgrowth
                
                //func(data0, data1, data2);

                //add to old database
                //System.out.println("day : "+nowday);
                /*System.out.println("newlysup : " + newlysup);
                System.out.println("newlytran : " + newlyTran);
                System.out.println("newlymerage : " + newlymerage);
                *///System.out.println("Itemsup : " + itemsup);
                //System.out.println("lastitemsup : " + lastitemsup);

                //lastitemsup = new HashMap<Integer, HashSet<Integer>>(itemsup);
                for(Integer k : itemsup.keySet()){
                    HashSet<Integer> set = new HashSet<>(itemsup.get(k));
                    lastitemsup.put(k, set);
                }
                //System.out.println("lastitemsup :: " + lastitemsup);
                AddtoOCS(newlyTran);
                newlysup.clear();
                newlyTran.clear();
            }

        }
        endT = System.currentTimeMillis();   
        System.out.println("--------------");
        System.out.println("done");
        long t = (endT - startT);
        System.out.println("Time : "+t + " miltime");

        //System.out.println("itemsup : "+itemsup);
        //System.out.println("SUP : " + CIDSet.size()*minsup) ;
        /*for(Integer k: Tree.headerList){
            System.out.println(k );
        }*/  
    }

    


/*    private void fuspGrowth(FUSPTree tree, int[] prefix, int prefixLength, int prefixSupport, Map<Integer, Integer> mapSupport, int minSupportRelative ) throws IOException {
        if(prefixLength == maxPatternLength){
            return;
        }

        boolean singlePath = true;

        int position = 0;

        if(tree.root.childs.size() > 1){
            singlePath = false;
        }else{
            FUSPNode currNode = tree.root.childs.get(0);
            while(true){
                if(currNode.childs.size() > 1){
                    singlePath = false;
                    break;
                }
                fuspNodeTempBuffer[position] = currNode;
                position++;
                if(currNode.childs.size() == 0){
                    break;
                }
                currNode = currNode.childs.get(0);
            }
        }

        if(singlePath){
            //func to get single item
        }else{
            for(int i = tree.headerList.size()-1 ; i <= 0 ; i--){
                Integer item = tree.headerList.get(i);

                int support  = mapSupport.get(item);
                prefix[prefixLength] = item;
                int betaSupport = (prefixSupport < support) ? prefixSupport: support;
                
                //saveItem()

                if(prefixLength + 1 < maxPatternLength){
                    List<List<FUSPNode>> prefixPaths = new ArrayList<List<FUSPNode>>();
                    FUSPNode path = tree.mapItemNodes.get(item);
                    Map<Integer, Integer> mapSupportBeta = new HashMap<Integer, Integer>();

                    while(path != null){
                        if(path.parant.itemID != -1){
                            List<FUSPNode> prefixPath = new ArrayList<FUSPNode>();
                            prefixPath.add(path);

                            int pathCount = path.supCount;

                            FUSPNode parent = path.parant;
                            
                            while(parent.itemID != 1){
                                prefixPath.add(parent);

                                if(mapSupportBeta.get(parent.itemID) == null){
                                    mapSupportBeta.put(parent.itemID, pathCount);
                                }else{
                                    mapSupportBeta.put(parent.itemID, mapSupportBeta.get(parent.itemID) + pathCount);
                                }
                                parent = parent.parant;
                            }
                            prefixPaths.add(prefixPath);
                        }
                        path = path.headerLink;
                    }
                    FUSPTree treeBeta = new FUSPTree();

                    for(List<FUSPNode> prefixPath : prefixPaths){
                        treeBeta.addPrefixpath(prefixPath, mapSupportBeta, minSupportRelative);
                    }
                    

                    if(treeBeta.root.childs.size() > 0){
                        treeBeta.creatHeaderList(mapSupportBeta);
                        fuspGrowth(treeBeta, prefix, prefixLength + 1, betaSupport, mapSupportBeta, minSupportRelative);
                    }
                    
                }
                

            }
        }
    }
    private void saveAllCombinationsOfPrefixPath(FUSPNode[] fpNodeTempBuffer, int position, int[] prefix, int prefixLength) throws IOException{
        int support = 0;
loop1:  for(long i = 1, max = 1 << position; i < max; i++){
            int newPrefixLength = prefixLength;  
            for (int j = 0; j < position; j++) {
                // check if the j bit is set to 1
                int isSet = (int) i & (1 << j);
                // if yes, add the bit position as an item to the new subset
                if (isSet > 0) {
                    if(newPrefixLength == maxPatternLength){
                        continue loop1;
                    }
                    
                    prefix[newPrefixLength++] = fpNodeTempBuffer[j].itemID;
                    if(support == 0) {
                        support = fpNodeTempBuffer[j].supCount;
                    }
                }
            }
            saveItemset(prefix, newPrefixLength, support);
        }
    }

    private void saveItemset(int [] itemset, int itemsetLength, int support)throws IOException{
        /*itemsetCount++;

        if(writer != null){
            System.arraycopy(itemset, 0, itemsetOutputBuffer, 0, itemsetLength);
            Arrays.sort(itemsetOutputBuffer, 0, itemsetLength);

            StringBuilder buffer = new StringBuilder();
            for(int i = 0; i < itemsetLength ; i ++){
                buffer.append(itemsetOutputBuffer[i]);
                if(i != itemsetLength-1){
                    buffer.append(" ");
                }
            }

            buffer.append(" #SUP: ");
            buffer.append(support);
            writer.write(buffer.toString());
            writer.newLine();
        }else{
            int[] itemsetArray = new int[itemsetLength];
			System.arraycopy(itemset, 0, itemsetArray, 0, itemsetLength);
			
			// sort the itemset so that it is sorted according to lexical ordering before we show it to the user
			Arrays.sort(itemsetArray);
			
			Itemset itemsetObj = new Itemset(itemsetArray);
			itemsetObj.setAbsoluteSupport(support);
			patterns.addItemset(itemsetObj, itemsetLength);
        }

    }*/


}