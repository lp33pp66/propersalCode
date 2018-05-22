package spstream;
/**
 * sup 要改
 * 總客數要改
 * 
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import spstream.SeqID;
import spstream.SequenceStruct;
import spstream.MemoryLogger;






public class main_SPStream_deletion_v1{
    public static int nowday = 1;
    public static double minsup = 0.5;
    public static HashSet<Integer> CIDList = new HashSet<>();
    public static HashSet<Integer> itemList = new HashSet<>();// All 1-item set
    //public static ArrayList<SequenceStruct> SeqStructList = new ArrayList<>(); // all seqstruct
    public static HashMap<String, SequenceStruct> StructMap = new HashMap<>();
    public static HashSet<String> New2Candate = new HashSet<>();//put 2-candidate
    
    
    //del func*****************************************????????????????????????????>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<
    public static int nowdelday =  1;
    public static int afterMinSupCount = 0 ;
    //check minsup and del item
    //seq, cid:daylist
    private static TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> delItemMap = new TreeMap<>();
    //put f > inf .seq
    private static ArrayList<String> delinftemp = new ArrayList<>();

    
    //return candidate
    private static ArrayList<String> findcandidatefunc(ArrayList<String> f2infseq){
        ArrayList<String> removelist = new ArrayList<>();
        for(String seq : f2infseq){
            String str = seq.substring(0, seq.length()-1);
            for(String candi : StructMap.keySet()){
                if(StructMap.get(candi).Seqlengh == StructMap.get(seq).Seqlengh +1){
                    //String candidate = funcfirst(candi);
                    if(candi.contains(str)){
                        removelist.add(candi);
                    }
                }
            }
        }
        //System.out.println(removelist);
        return removelist;
    }
    //return delInfo : map(seq , map(cid, daylist)) 
    private static TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> deldaylistfunc(ArrayList<String> f2fseq, TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> delItemdaylistInfoMap){
        TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> delInfomap = new TreeMap<>();
        
        for(String seq : f2fseq){
            
            String candic = seq.substring(0, seq.length()-1);
            
            for(String candidate: StructMap.keySet()){
                if(StructMap.get(candidate).Seqlengh == StructMap.get(seq).Seqlengh+1){
                    String str = funclast(candidate);
                    if(str.equals(candic)){
                        TreeMap<Integer, TreeSet<Integer>> tmap = delItemdaylistInfoMap.get(seq);
                        //System.out.println("seq: " + seq+ " candidata : "+ candidate +" tmap : "+tmap);
                        TreeMap<Integer, TreeSet<Integer>> rtmap = deldaylist(seq, candidate, tmap);
                        if(rtmap.size() != 0){
                            delInfomap.put(candidate, rtmap);
                        }
                    }
                }
            }
        }
        return delInfomap;
    }

    //input seq, candidate, delinfo  return candidate delInfomation
    private static TreeMap<Integer, TreeSet<Integer>> deldaylist(String seq, String candidate, TreeMap<Integer, TreeSet<Integer>> tMap){
        System.out.println("func deldaylist!!!!!!!!!!!!!!!!!!");
        System.out.println("seq: " + seq);
        System.out.println("candidate: " + candidate);
        System.out.println("tMap(delinfo): " + tMap);
        TreeMap<Integer, TreeSet<Integer>> delInfo = new TreeMap<>();

        SequenceStruct seqstruct = StructMap.get(seq);
        SequenceStruct candistruct = StructMap.get(candidate);
        
        System.out.println("seq.strcuct: "+ StructMap.get(seq).CIDDay);
        System.out.println("can.strcuct: "+ StructMap.get(candidate).CIDDay);


        Iterator<Integer> iterator = tMap.keySet().iterator();

        while(iterator.hasNext()){
            Integer cid = iterator.next();
            if(candistruct.CIDList.contains(cid)){
                TreeSet<Integer> candidayset = candistruct.CIDDay.get(cid);
                TreeSet<Integer> origdayset = seqstruct.CIDDay.get(cid);
                System.out.println("seq: "+ seq);
                System.out.println("seqstruct.CIDDay: " + seqstruct.CIDDay);
                System.out.println("candidate: "+ candidate);
                System.out.println("cid: " + cid);
                System.out.println("origdayset: " + origdayset);
                String[] check = candidate.split("#");
                if(check[check.length-1].contains("&")){//不是獨立項目及
                    TreeSet<Integer> tset = func_togerdel(tMap.get(cid), origdayset);
                    if(tset != null){
                        delInfo.put(cid, tset);
                    }
                }else{//candistruct is 獨立項目及
                    TreeSet<Integer> tset = func_onlydel(tMap.get(cid).last(), candidayset, origdayset);
                    if(tset != null){
                        delInfo.put(cid, tset);
                    }
                }
            }else{
                //do nothing
            }
        }
        
        return delInfo;
    }

    private static void delmarkdaylist(TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> delcandidatelistInfoMap){
        for(String seq : delcandidatelistInfoMap.keySet()){
            SequenceStruct deltemp = StructMap.get(seq);

            TreeMap<Integer, TreeSet<Integer>> seqdelInfo = delcandidatelistInfoMap.get(seq);
            
            for(Integer cid : seqdelInfo.keySet()){
                
                for(Integer day : seqdelInfo.get(cid)){
                    deltemp.CIDDay.get(cid).remove(day);
                    if(deltemp.CIDDay.get(cid).size()==0){
                        deltemp.CIDList.remove(cid);;
                    }
                }
            }
            
        }


    }
     
    private static void delnulldaylist(String seq){
        SequenceStruct temp = StructMap.get(seq);
        TreeMap<Integer, TreeSet<Integer>> tmap = temp.CIDDay;
        Iterator<Integer> iterator = tmap.keySet().iterator();

        while(iterator.hasNext()){
            Integer key = iterator.next();
            TreeSet<Integer> tset = tmap.get(key);
            //System.out.println("func check"+tset);
            if( tset.size()==0 ){
                //iterator.remove();
                //System.out.println("func check in loop"+tset);
                iterator.remove();
            }
        }
    }

    private static void delmarkfunc(SequenceStruct seqstruct, TreeMap<Integer, TreeSet<Integer>> delInfo){
        //System.out.println(seqstruct.CIDDay);
        //System.out.println(seqstruct.CIDList);
        //ArrayList<Integer> realist = new ArrayList<>();
        
        for(Integer cid : delInfo.keySet()){
            seqstruct.CIDList.remove(cid);
            for(Integer day: delInfo.get(cid)){
                seqstruct.CIDDay.get(cid).remove(day);
            }
        }

        
    }
    
    private static void createfreqfunc(ArrayList<String> inf2fList, Integer lv){
       
        ArrayList<String> freqlist = new ArrayList<>();//put equal lv 
        for(String seq : StructMap.keySet()){
            if(StructMap.get(seq).Seqlengh == lv ){
                if(StructMap.get(seq).CIDList.size() >= afterMinSupCount){
                    
                    freqlist.add(seq);
                }
            }
        }

        //System.out.println(inf2fList);
        //System.out.println(freqlist);
        for(String inf2fseq : inf2fList){
            for(String fseq : freqlist){
                //System.out.println("x: " + inf2fList+ " y: "+ fseq);
                comparefnc(inf2fseq, fseq, lv);
                comparefnc(fseq, inf2fseq, lv);
            }
        }
    }

    private static void comparefnc(String x, String y, Integer lv){
        if(lv == 1 ){
            
            if(x.equals(y)){
                SeqID xx = new SeqID();
                if(StructMap.containsKey(xx.ID)){

                }else{
                    xx.setItemid(x, y);
                    SequenceStruct yy = new SequenceStruct();
                    yy.SeqId = xx;
                    SequenceStruct t = StructMap.get(y);
                    for(Integer cid : t.CIDList){
                        if(t.CIDDay.get(cid).size()>1){
                            TreeSet<Integer> set = new TreeSet<>(t.CIDDay.get(cid));
                            set.remove(set.first());
                            yy.CIDList.add(cid);
                            yy.CIDDay.put(cid, set);
                        }
                    }
                    yy.Seqlengh++;
                    StructMap.put(xx.ID, yy);
                    //System.out.println("create : " + xx.ID);            

                }
            }
            if(!x.equals(y)){

                SeqID xy = new SeqID(); xy.setItemid(x, y);
                SeqID xny = new SeqID(); xny.setItemidn(x, y);
                
                //sequecne
                SequenceStruct x1y = new SequenceStruct(); 
                x1y.SeqId = xy;
                HashSet<Integer> samecid = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                //System.out.println("samecid : " + samecid);
                if(samecid.size()>0){
                    //對每個相同ＣＩＤ
                    for(Integer cid : samecid){
                        TreeSet<Integer> adddayset = new TreeSet<>();
                        for(Integer yday : StructMap.get(y).CIDDay.get(cid)){
                            if(yday > StructMap.get(x).CIDDay.get(cid).first()){//大於Ｘ的第一天
                                adddayset.add(yday);
                            }
                        }
                        if(adddayset.size()>0){
                            x1y.CIDList.add(cid);
                            x1y.CIDDay.put(cid, adddayset);
                        }  
                    }
                    x1y.Seqlengh++;
                    StructMap.put(xy.ID, x1y);
                    //System.out.println("create: " + xy.ID);
                }

                //togeter
                String[] intx = x.split("#");
                String[] inty = y.split("#");
                int xxxx = Integer.parseInt(intx[0]);
                int yyyy = Integer.parseInt(inty[0]);

                if(xxxx < yyyy){
                    SequenceStruct x2y = new SequenceStruct(); 
                    x2y.SeqId = xny;
                    //System.out.println("ID : "+xny.ID);
                    HashSet<Integer> samecid1 = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                    //System.out.println("sameID : " + samecid);
                    if(samecid.size()>0){
                        //對每個相同ＣＩＤ
                        for(Integer cid : samecid1){
                            //System.out.println("cid : "+cid);
                            TreeSet<Integer> adddayset = new TreeSet<>();

                            for(Integer yd : StructMap.get(y).CIDDay.get(cid)){
                                for(Integer xd : StructMap.get(x).CIDDay.get(cid)){
                                    //System.out.println("CID : " + cid + " yd : " + yd + " xd : " + xd);
                                    if(yd.equals(xd)){
                                        adddayset.add(yd);
                                    }
                                }
                                //System.out.println("addset : "+adddayset) ;
                                //System.out.println("OK");
                                if(adddayset.size()>0){
                                    x2y.CIDList.add(cid);
                                    x2y.CIDDay.put(cid, adddayset);
                                }
                            }
                        }
                        x2y.Seqlengh++;
                        StructMap.put(xny.ID, x2y);
                        //System.out.println("creratr: " + xny.ID);

                    } 
                }
            }
        }else{
            String prefixseq = funcfirst(x);
            String postfixseq = funclast(x);
            String prefixcom = funcfirst(y);
            String postfixcom = funclast(y);
            
            //"bc" cb
            if(prefixseq.equals(postfixcom)){
                String[] check = y.split("#"); 
                if(check[check.length-1].contains("&")){//不是獨立
                    String candidate = candiIdTogeter(x, y);

                    SequenceStruct temp = new SequenceStruct();
                    SeqID tid = new SeqID();
                    tid.ID = candidate;
                    temp.SeqId = tid;
                    temp.Seqlengh = lv+1;
                  
                    HashSet<Integer> set = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                    
                    //System.out.println("set1: "+set);
                    if(set.size()>0){
                        for(Integer cid : set){

                            TreeSet<Integer> adddayset = new TreeSet<>();

                            //System.out.println(StructMap.get(seq).CIDDay.get(cid));
                            //System.out.println(StructMap.get(compseq).CIDDay.get(cid));
                            

                            for(Integer xd : StructMap.get(x).CIDDay.get(cid)){
                                for(Integer yd : StructMap.get(y).CIDDay.get(cid)){
                                    if(xd != null && yd != null){
                                        if(yd.equals(xd)){
                                            adddayset.add(yd);
                                        }
                                    }
                                }
                                if(adddayset.size()>0){
                                    temp.CIDList.add(cid);
                                    temp.CIDDay.put(cid, adddayset);
                                    //System.out.println("cid: "+cid+" adddaylist: "+adddayset);
                                }
                            }
                        }
                        StructMap.put(candidate, temp);
                        //System.out.println("create: " + candidate);
                    }
                }else{//獨立
                    String candidate = candiIdStream(x, y);
                    SequenceStruct temp = new SequenceStruct();
                    //System.out.println("checkcandidate:"+candidate);
                    SeqID tid = new SeqID();
                    tid.ID = candidate;
                    temp.SeqId = tid;
                    temp.Seqlengh = lv+1;
                    //System.out.println("lv"+ temp.Seqlengh);
                    
                    HashSet<Integer> set = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                    //System.out.println("set:"+ set);
                    if(set.size()>0){
                        for(Integer cid : set){
                            //System.out.println("cid = "+cid);
                            TreeSet<Integer> adddayset = new TreeSet<>();
                            for(Integer yd : StructMap.get(y).CIDDay.get(cid)){
                                //System.out.println("yd : "+yd);
                                //System.out.println("first: "+StructMap.get(seq).CIDDay.get(cid).first());
                                if(StructMap.get(x).CIDDay.containsKey(cid)){
                                    if(yd > StructMap.get(x).CIDDay.get(cid).first()){
                                        adddayset.add(yd);
                                    }
                                }
                            }
                            
                            if(adddayset.size()>0){
                                temp.CIDList.add(cid);
                                temp.CIDDay.put(cid, adddayset);
                                //System.out.println("cid: "+cid+" add : "+adddayset);
                            }
                            
                        }
                        //System.out.println("check");
                        StructMap.put(candidate, temp);
                       //System.out.println("creat: "+ candidate);

                    }
                }
            }
            //cb "bc"
            if(prefixcom.equals(postfixseq)){
                String[] check = x.split("#");
                if(check[check.length-1].contains("&")){//不是獨立
                    String candidate = candiIdTogeter(y, x);
                    SequenceStruct temp = new SequenceStruct();
                    SeqID tid = new SeqID();
                    tid.ID = candidate;
                    temp.SeqId = tid;
                    temp.Seqlengh = lv+1;
                    HashSet<Integer> set = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                    //System.out.println(seq +" : "+StructMap.get(seq).CIDDay);
                    //System.out.println(compseq +" : "+StructMap.get(compseq).CIDDay);
                    //System.out.println("set: "+set);
                    if(set.size()>0){
                        for(Integer cid : set){
                            TreeSet<Integer> adddayset = new TreeSet<>();
                            for(Integer xd : StructMap.get(y).CIDDay.get(cid)){
                                for(Integer yd : StructMap.get(x).CIDDay.get(cid)){
                                    if(yd.equals(xd)){
                                        adddayset.add(yd);
                                    }
                                }
                                if(adddayset.size()>0){
                                    temp.CIDList.add(cid);
                                    temp.CIDDay.put(cid, adddayset);
                                }
                            }
                        }
                        StructMap.put(candidate, temp);
                        //System.out.println("create: " + candidate);
                    }

                }else{//獨立
                    String candidate = candiIdStream(y, x);
                    SequenceStruct temp = new SequenceStruct();
                    SeqID tid = new SeqID();
                    tid.ID = candidate;
                    temp.SeqId = tid;
                    temp.Seqlengh = lv+1;
                    /*System.out.println("tid.id: "+temp.SeqId.ID);
                    System.out.println("seqlist"+StructMap.get(seq).CIDDay);
                    System.out.println("comlist"+StructMap.get(compseq).CIDDay);*/
                    HashSet<Integer> set = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                    
                    //System.out.println("set: "+set);
                    
                    if(set.size()>0){
                        for(Integer cid : set){
                            TreeSet<Integer> adddayset = new TreeSet<>();
                            for(Integer yd : StructMap.get(x).CIDDay.get(cid)){
                                //System.out.println("yd: "+yd);
                                if(yd > StructMap.get(y).CIDDay.get(cid).first()){
                                    adddayset.add(yd);
                                }
                            }
                            if(adddayset.size()>0){
                                temp.CIDList.add(cid);
                                temp.CIDDay.put(cid, adddayset);
                                //System.out.println("cid: "+cid+"add: "+adddayset);
                            }
                            
                        }
                        StructMap.put(candidate, temp);
                        //System.out.println("candida: "+candidate+" temp: "+temp.CIDDay);
                        //System.out.println("creatr: " + candidate);
                    }
                }
            }
        }
    }



    private static TreeSet<Integer> func_onlydel(int lastdelday, TreeSet<Integer> comptset, TreeSet<Integer> canditset){
        //System.out.println("lastdelday: " + lastdelday);
        //System.out.println("comptset: "+ comptset);
        //System.out.println("canditset: " + canditset);
        comptset.remove(lastdelday);
        //System.out.println("remove: " + comptset);
        TreeSet<Integer> removeset = new TreeSet<>();
        for(Integer day : comptset){
            //System.out.println(day);
            //System.out.println(comptset.first());
            if(day >= lastdelday && comptset.first() <= day){
                removeset.add(day);
            }
        }
        //System.out.println("del day: "+ removeset);
        //System.out.println("");
        //有可能return null
        return removeset;
    }
    private static TreeSet<Integer> func_togerdel(TreeSet<Integer> lastdelday, TreeSet<Integer> canditset){
        TreeSet<Integer> removeset = new TreeSet<>();
        System.out.println("lastdelday: " + lastdelday);
        System.out.println("canditset: " + canditset);
        for(Integer day : canditset){
            for(Integer k : lastdelday){
                if(day == k){
                    removeset.add(day);
                }

            }
        }
        //System.out.println("del day: "+ removeset);

        return removeset;
    }


    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    private static SequenceStruct CreatSeqStucFunc(int data0, int data1, int data2){
        SequenceStruct temp = new SequenceStruct();
        SeqID item = new SeqID();
        item.setItemid(data2);
        temp.SeqId = item;
        temp.CIDList.add(data1);
        TreeSet<Integer> set = new TreeSet<>();
        set.add(data0);
        temp.CIDDay.put(data1, set);
        return temp;
    }
    private static void UpdeSeqStucFunc(int data0, int data1, int data2, SequenceStruct struct){
        if(struct.CIDList.contains(data1)){
            TreeSet<Integer> set = struct.CIDDay.get(data1);
            set.add(data0);
            struct.CIDDay.put(data1, set);
        }else{
            struct.CIDList.add(data1);
            TreeSet<Integer> set = new TreeSet<>();
            set.add(data0);
            struct.CIDDay.put(data1, set);
        }
    }

    private static HashSet<Integer> SameCidFunc(HashSet<Integer> x, HashSet<Integer> y){
        HashSet<Integer> set = new HashSet<>();
        for(Integer q : x){
            for(Integer p : y){
                if(q == p && p == q){
                    set.add(q);
                }
            }
        }
        return set;
    }
    
    private static HashSet<Integer> funchasnewday(String y, int nowday){
        HashSet<Integer> set = new HashSet<>();
        SequenceStruct t = StructMap.get(y);
        for(Integer i : t.CIDDay.keySet()){
            if(t.CIDDay.get(i).contains(nowday)){
                set.add(i);
            }
        }
        return set;
    }

    private static HashSet<Integer> funchasnewday(String x, String y, int nowday){
        HashSet<Integer> set = new HashSet<>();
        SequenceStruct X = StructMap.get(x);
        SequenceStruct Y = StructMap.get(y);
        //System.out.println("funcseq: "+StructMap.get(x).CIDDay);
        //System.out.println("funccom: "+ StructMap.get(y).CIDDay);

        for(Integer i : X.CIDDay.keySet()){
            for(Integer j : Y.CIDDay.keySet()){
                if(i == j && X.CIDDay.get(i).contains(nowday) && Y.CIDDay.get(i).contains(nowday)){
                    set.add(i);
                }
            }
        }
        return set;
    }

    //x y = xy, (xy)
    private static void Compare(String x, String y, int MinSupCount, int nowday)throws IOException{
        //System.out.println(x + "x.sup : " + StructMap.get(x).CIDList.size());
        //System.out.println(y + "y.sup : " + StructMap.get(y).CIDList.size());
        
        if(StructMap.get(x).CIDList.size() >= MinSupCount && StructMap.get(y).CIDList.size() >= MinSupCount){
            //candidate
            
            //System.out.println("freq : "+ x);
            //System.out.println("freq : "+ y);
            SeqID xy = new SeqID(); xy.setItemid(x, y);
            SeqID xny = new SeqID(); xny.setItemidn(x, y);
            
            //System.out.println("xy : "+xy.ID);
            //System.out.println("(xy) : "+xny.ID);
            
            //stream
            if(StructMap.containsKey(xy.ID)){
                //update
                //System.out.println("update"+x+y);
                SequenceStruct temp = StructMap.get(xy.ID);
                
                HashSet<Integer> set = funchasnewday(y, nowday);//put y.有新家天數的cid
                //System.out.println("set"+set);
                if(set.size() > 0){
                    //System.out.println(">0");
                    for(Integer cid : set){//for has newday.cid
                        //System.out.println("cidlist" +temp.SeqId.ID);
                        if(temp.CIDList.contains(cid)){//bc.cid = c.cid
                            //System.out.println("contain");
                            temp.CIDDay.get(cid).add(nowday);//add nowday
                        }else{//compare b.daylist
                            //System.out.println("incontain");
                            if(StructMap.get(x).CIDList.contains(cid)){//if has same cid
                                TreeSet<Integer> sset = new TreeSet<>();

                                for(Integer cday : StructMap.get(y).CIDDay.get(cid)){//every y.cid.day compare
                                    if(cday > StructMap.get(x).CIDDay.get(cid).first()){
                                        sset.add(cday);
                                    }
                                }
                                if(sset.size()>0){
                                    temp.CIDDay.put(cid, sset);
                                    temp.CIDList.add(cid);
                                }
                            }
                        }
                    }
                    StructMap.put(xy.ID, temp);
                    New2Candate.add(xy.ID);
                }
            }else{ 
                SequenceStruct x1y = new SequenceStruct(); 
                x1y.SeqId = xy;
                HashSet<Integer> samecid = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                //System.out.println("samecid : " + samecid);
                if(samecid.size()>0){
                    //對每個相同ＣＩＤ
                    for(Integer cid : samecid){
                        TreeSet<Integer> adddayset = new TreeSet<>();
                        for(Integer yday : StructMap.get(y).CIDDay.get(cid)){
                            if(yday > StructMap.get(x).CIDDay.get(cid).first()){//大於Ｘ的第一天
                                adddayset.add(yday);
                            }
                        }
                        if(adddayset.size()>0){
                            x1y.CIDList.add(cid);
                            x1y.CIDDay.put(cid, adddayset);
                        }  
                    }
                    x1y.Seqlengh++;
                    StructMap.put(xy.ID, x1y);
                    New2Candate.add(xy.ID);
                }
                //System.out.println("cidday"+x1y.CIDList);
                
                //System.out.println(StructMap.keySet());
            }
            //togeter***********************************************************
            String[] intx = x.split("#");
            String[] inty = y.split("#");
            int xxxx = Integer.parseInt(intx[0]);
            int yyyy = Integer.parseInt(inty[0]);
            if(xxxx < yyyy){
                if(StructMap.containsKey(xny.ID)){
                    //update
                    //System.out.println("ID: "+xny.ID);
                    SequenceStruct temp = StructMap.get(xny.ID);
                    //System.out.println("YYYYYYYYY"+temp.CIDList);
                    HashSet<Integer> sameCid = funchasnewday(x, y, nowday);//x.cid = y.cid & = nowday
                    //System.out.println("sameCID : " + sameCid);
                    if(sameCid.size()>0){
                        for(Integer cid : sameCid){//for every same cid
                            //System.out.println("nowCID : "+cid);
                            if(temp.CIDList.contains(cid)){
                                if(StructMap.get(x).CIDDay.get(cid).contains(nowday) && StructMap.get(y).CIDDay.get(cid).contains(nowday))
                                temp.CIDDay.get(cid).add(nowday);
                            }else{
                                TreeSet<Integer> addset = new TreeSet<>();
                                for(Integer dayx : StructMap.get(x).CIDDay.get(cid)){
                                    for(Integer dayy : StructMap.get(y).CIDDay.get(cid)){
                                        //System.out.println("cid : "+ cid+" dayx : "+dayx+" dayy : " + dayy);
                                        if(dayx == dayy){
                                            addset.add(dayx);
                                            
                                        }
                                    }
                                }
                                if(addset.size()>0){
                                    temp.CIDDay.put(cid, addset);
                                    temp.CIDList.add(cid);
                                }
                            }
    
                        }
                        StructMap.put(xny.ID, temp);
                        New2Candate.add(xny.ID);
                    }
                    //System.out.println("@1 : "+StructMap.get("20*30#").CIDDay.get(1));
                    //System.out.println("@3 : "+StructMap.get("20*30#").CIDDay.get(3));
                }else{
                    SequenceStruct x2y = new SequenceStruct(); 
                    x2y.SeqId = xny;
                    //System.out.println("ID : "+xny.ID);
                    HashSet<Integer> samecid = SameCidFunc(StructMap.get(x).CIDList, StructMap.get(y).CIDList);
                    //System.out.println("sameID : " + samecid);
                    if(samecid.size()>0){
                        //對每個相同ＣＩＤ
                        for(Integer cid : samecid){
                            //System.out.println("cid : "+cid);
                            TreeSet<Integer> adddayset = new TreeSet<>();

                            for(Integer yd : StructMap.get(y).CIDDay.get(cid)){
                                for(Integer xd : StructMap.get(x).CIDDay.get(cid)){
                                    //System.out.println("CID : " + cid + " yd : " + yd + " xd : " + xd);
                                    if(yd.equals(xd)){
                                        adddayset.add(yd);
                                    }
                                }
                                //System.out.println("addset : "+adddayset) ;
                                //System.out.println("OK");
                                if(adddayset.size()>0){
                                    x2y.CIDList.add(cid);
                                    x2y.CIDDay.put(cid, adddayset);
                                }
                            }
                        }
                        x2y.Seqlengh++;
                        StructMap.put(xny.ID, x2y);
                        New2Candate.add(xny.ID);
                    }                
                }

            }
            

        }
    }

    public static void CompareSameID(String x, String y, int MinSupCount, int nowday){
        if(StructMap.get(x).CIDList.size()>=MinSupCount && StructMap.get(y).CIDList.size()>=MinSupCount){
            SeqID xx = new SeqID();
            xx.setItemid(x, y);
            //System.out.println(" X : "+x+" Y : "+y);
            if(StructMap.containsKey(xx.ID)){
                SequenceStruct temp = StructMap.get(xx.ID);
                HashSet<Integer> samecid = funchasnewday(y, nowday);
                for(Integer cid : samecid){
                    if(temp.CIDList.contains(cid)){
                        temp.CIDDay.get(cid).add(nowday);
                    }else{
                        if(StructMap.get(x).CIDDay.get(cid).size()>1){
                            TreeSet<Integer> set = new TreeSet<>(StructMap.get(x).CIDDay.get(cid));
                            set.remove(set.first());
                            if(set.size()>0){
                                temp.CIDDay.put(cid, set);
                                temp.CIDList.add(cid);
                            }
                        }
                    }
                    StructMap.put(xx.ID, temp);
                    New2Candate.add(xx.ID);
                }
            }else{
                SequenceStruct yy = new SequenceStruct();
                yy.SeqId = xx;
                SequenceStruct t = StructMap.get(y);
                for(Integer cid : t.CIDList){
                    if(t.CIDDay.get(cid).size()>1){
                        TreeSet<Integer> set = new TreeSet<>(t.CIDDay.get(cid));
                        set.remove(set.first());
                        yy.CIDList.add(cid);
                        yy.CIDDay.put(cid, set);
                    }

                }
                yy.Seqlengh++;
                StructMap.put(xx.ID, yy);
                New2Candate.add(xx.ID);
            }

        }
    }

    private static void Compare2candidate(HashSet<String> canset, int MinSupCount, int lv, int nowday){
        if(canset.size() > 1){
            HashSet<String> freqset = new HashSet<>();
            
            for(String seq : canset){
                 //System.out.println("seq: "+StructMap.containsKey(seq));
                if(StructMap.containsKey(seq)){
                    if(StructMap.get(seq).CIDList.size() >= MinSupCount){
                        freqset.add(seq);
                    } 
                }
            }
            //
            HashSet<String> rcanset =  generation(freqset, lv, nowday, MinSupCount);
            //add delfunc
            lv++;
            HashSet<String> noUpdateCandidateSet = new HashSet<>();
            for(String k : StructMap.keySet()){
                if(StructMap.get(k).Seqlengh == lv && freqset.contains(k)==false){
                    noUpdateCandidateSet.add(k);
                }
            }
            delfunc(noUpdateCandidateSet, freqset, lv, MinSupCount);
            
            Compare2candidate(rcanset, MinSupCount, lv, nowday);

        }else{
            //System.out.println("generation ok!");
        }
        
        //System.out.println("funcOKOKOKO");
        
    }

    private static HashSet<String> generation(HashSet<String> freqset, int lv, int nowday, int MinSupCount){
        HashSet<String> canset = new HashSet<>();//put candidate
        HashSet<String> lvset = new HashSet<>();
        for(String k : StructMap.keySet()){
            if(StructMap.get(k).Seqlengh == lv && StructMap.get(k).CIDList.size() >= MinSupCount){
                lvset.add(k);
            }
        }

        /*String curr = "20#40#";
        if(freqset.contains(curr)){
            System.out.println(StructMap.get(curr).SeqId.ID);
            System.out.println(StructMap.get(curr).CIDList);
            System.out.println(StructMap.get(curr).CIDDay);
            System.out.println(StructMap.get(curr).Seqlengh);
        }
    
            System.out.println("lvset: "+lvset);
            System.out.println("freqset seq: "+freqset);*/


        for(String seq : freqset){
            String prefixseq = funcfirst(seq);
            String postfixseq = funclast(seq);
            for(String compseq : lvset){
                String prefixcom = funcfirst(compseq);
                String postfixcom = funclast(compseq );
                /******************************************************************** */
                //System.out.println("????????????????????????????????????????????????????????????");
                /*System.out.println("seq: "+seq+" compseq: "+compseq);
                System.out.println("prefixseq: "+prefixseq);
                System.out.println("postfixseq: "+postfixseq);
                System.out.println("prefixcom: "+prefixcom);
                System.out.println("postfixcom: "+postfixcom);*/
                //if(seq.contains("20#40#")) System.out.println("seqhave prob");
                //if(compseq.contains("20#40#")) System.out.println("comseq/ prob");
                
                if(prefixseq.equals(postfixcom)){//"bc" cb
                    //System.out.println("eql: "+prefixseq+" "+postfixcom);
                    
                    //2case
                    String[] check = compseq.split("#");

                    if(check[check.length-1].contains("&")){//x2.last不是獨立項目及
                        
                        String candidate = candiIdTogeter(seq, compseq);
                        //System.out.println("x2.last不是獨立項目及 : "+candidate);
                        
                        if(StructMap.containsKey(candidate)){
                            SequenceStruct temp = StructMap.get(candidate);
                            HashSet<Integer> set = funchasnewday(seq, compseq, nowday);
                            //System.out.println("set:"+set);
                            for(Integer cid : set){
                                if(temp.CIDDay.keySet().contains(cid)){
                                    temp.CIDList.add(cid);
                                    TreeSet<Integer> tset = new TreeSet<>();
                                    tset = temp.CIDDay.get(cid);
                                    tset.add(nowday);
                                    temp.CIDDay.put(cid, tset);
                                }else{
                                    TreeSet<Integer> addset = new TreeSet<>();
                                    for(Integer dayx : StructMap.get(seq).CIDDay.get(cid)){
                                        for(Integer dayy : StructMap.get(compseq).CIDDay.get(cid)){
                                            //System.out.println("cid : "+ cid+" dayx : "+dayx+" dayy : " + dayy);
                                            if(dayx == dayy){
                                                addset.add(dayx);
                                            }
                                        }
                                    }
                                    if(addset.size()>0){
                                        temp.CIDDay.put(cid, addset);
                                        temp.CIDList.add(cid);
                                    }
                                }
                                
                            }           
                            StructMap.put(candidate, temp);
                            canset.add(candidate);
                        }else{
                            SequenceStruct temp = new SequenceStruct();
                            SeqID tid = new SeqID();
                            tid.ID = candidate;
                            temp.SeqId = tid;
                            temp.Seqlengh = lv+1;

                            //System.out.println("candidate: "+candidate);
                            //System.out.println("seq: "+seq+" day: "+StructMap.get(seq).CIDDay);
                            //System.out.println("compseq: "+compseq+" day: "+StructMap.get(compseq).CIDDay);
                            //System.out.println(StructMap.get(seq).CIDList);
                            //System.out.println(StructMap.get(compseq).CIDList);
                            
                            HashSet<Integer> set = SameCidFunc(StructMap.get(seq).CIDList, StructMap.get(compseq).CIDList);
                            
                            //System.out.println("set1: "+set);
                            if(set.size()>0){
                                for(Integer cid : set){

                                    TreeSet<Integer> adddayset = new TreeSet<>();

                                    //System.out.println(StructMap.get(seq).CIDDay.get(cid));
                                    //System.out.println(StructMap.get(compseq).CIDDay.get(cid));
                                    

                                    for(Integer xd : StructMap.get(seq).CIDDay.get(cid)){
                                        for(Integer yd : StructMap.get(compseq).CIDDay.get(cid)){
                                            if(xd != null && yd != null){
                                                if(yd.equals(xd)){
                                                    adddayset.add(yd);
                                                }
                                            }
                                        }
                                        if(adddayset.size()>0){
                                            temp.CIDList.add(cid);
                                            temp.CIDDay.put(cid, adddayset);
                                            //System.out.println("cid: "+cid+" adddaylist: "+adddayset);
                                        }
                                    }
                                }
                                StructMap.put(candidate, temp);
                                canset.add(candidate);
                            }
                        }
                        
                    }else{//x2.last獨立項目及
                        //System.out.println("x2.last");
                        
                        
                        String candidate = candiIdStream(seq, compseq);
                        
                        //System.out.println("candidatex2.last獨立項目及: "+candidate);
                        
                        if(StructMap.containsKey(candidate)){
                            SequenceStruct temp = StructMap.get(candidate);
                            HashSet<Integer> set = funchasnewday(compseq ,nowday);
                            //System.out.println("candida: "+candidate+" temp: "+temp.CIDDay);
                            if(set.size() > 0){ 
                                for(Integer cid : set){
                                    if(temp.CIDList.contains(cid)){
                                        //System.out.println("cid: "+cid+" value "+temp.CIDDay.get(cid));
                                        temp.CIDDay.get(cid).add(nowday);
                                        //System.out.println("cid: "+cid+" day: "+nowday);
                                    }else{
                                        if(StructMap.get(seq).CIDList.contains(cid)){
                                            TreeSet<Integer> sset = new TreeSet<>();
                                            for(Integer cday : StructMap.get(compseq).CIDDay.get(cid)){
                                                if(StructMap.get(seq).CIDDay.containsKey(cid)){
                                                    if(cday > StructMap.get(seq).CIDDay.get(cid).first()){
                                                        sset.add(cday);
                                                        //System.out.println("cid: "+cid+" cday: "+cday);
                                                    }
                                                }
                                            }
                                            if(sset.size()>0){
                                                temp.CIDDay.put(cid, sset);
                                                temp.CIDList.add(cid);
                                            }
                                        }
                                    }
                                }
                                StructMap.put(candidate, temp);
                                //System.out.println("candida: "+candidate+" temp: "+temp.CIDDay);
                                canset.add(candidate);
                            }
                            
                        }else{
                            SequenceStruct temp = new SequenceStruct();
                            //System.out.println("checkcandidate:"+candidate);
                            SeqID tid = new SeqID();
                            tid.ID = candidate;
                            temp.SeqId = tid;
                            temp.Seqlengh = lv+1;
                            //System.out.println("lv"+ temp.Seqlengh);
                            
                            HashSet<Integer> set = SameCidFunc(StructMap.get(seq).CIDList, StructMap.get(compseq).CIDList);
                            //System.out.println("set:"+ set);
                            if(set.size()>0){
                                for(Integer cid : set){
                                    //System.out.println("cid = "+cid);
                                    TreeSet<Integer> adddayset = new TreeSet<>();
                                    for(Integer yd : StructMap.get(compseq).CIDDay.get(cid)){
                                        //System.out.println("yd : "+yd);
                                        //System.out.println("first: "+StructMap.get(seq).CIDDay.get(cid).first());
                                        if(StructMap.get(seq).CIDDay.containsKey(cid)){
                                            if(yd > StructMap.get(seq).CIDDay.get(cid).first()){
                                                adddayset.add(yd);
                                            }
                                        }
                                    }
                                    
                                    if(adddayset.size()>0){
                                        temp.CIDList.add(cid);
                                        temp.CIDDay.put(cid, adddayset);
                                        //System.out.println("cid: "+cid+" add : "+adddayset);
                                    }
                                    
                                }
                                //System.out.println("check");
                                StructMap.put(candidate, temp);
                                canset.add(candidate);

                            }  
                        }
                    }

                }
                //*********************************************************************** */
                if(prefixcom.equals(postfixseq)){//cb "bc"
                    //2case
                    String[] check = seq.split("#");
                    //System.out.println("888 : "+check[check.length-1]);
                    if(check[check.length-1].contains("&")){//x2.last不是獨立項目及
                        String candidate = candiIdTogeter(compseq, seq);
                        
                        if(StructMap.containsKey(candidate)){
                            
                            //System.out.println("candidatex2.last不是獨立項目及 : "+candidate);
                            SequenceStruct temp = StructMap.get(candidate);
                            
                            /*System.out.println("Seq: "+StructMap.get(seq).CIDDay);
                            System.out.println("com: "+StructMap.get(compseq).CIDDay);
                            System.out.println("can"+StructMap.get(candidate).CIDDay);
                            System.out.println("seq: "+ seq);
                            System.out.println("com: "+ compseq);
                            System.out.println("day++: "+nowday);*/
                            HashSet<Integer> set = funchasnewday(compseq, seq, nowday);
                            //System.out.println("set23: "+set);
                            for(Integer cid : set){
                            
                                if(temp.CIDDay.keySet().contains(cid)){
                                    temp.CIDList.add(cid);
                                    TreeSet<Integer> tset = new TreeSet<>();
                                    tset = temp.CIDDay.get(cid);
                                    tset.add(nowday);
                                    if(tset.size()>0){
                                        temp.CIDDay.put(cid, tset);
                                        temp.CIDList.add(cid);
                                    }
                                }else{
                                    TreeSet<Integer> addset = new TreeSet<>();
                                    for(Integer dayx : StructMap.get(compseq).CIDDay.get(cid)){
                                        for(Integer dayy : StructMap.get(seq).CIDDay.get(cid)){
                                            //System.out.println("cid : "+ cid+" dayx : "+dayx+" dayy : " + dayy);
                                            if(dayx == dayy){
                                                addset.add(dayx);
                                            }
                                        }
                                    }
                                }
                            }           
                            StructMap.put(candidate, temp);
                            canset.add(candidate);
                        }else{
                            
                            SequenceStruct temp = new SequenceStruct();
                            SeqID tid = new SeqID();
                            tid.ID = candidate;
                            temp.SeqId = tid;
                            temp.Seqlengh = lv+1;
                            HashSet<Integer> set = SameCidFunc(StructMap.get(seq).CIDList, StructMap.get(compseq).CIDList);
                            //System.out.println(seq +" : "+StructMap.get(seq).CIDDay);
                            //System.out.println(compseq +" : "+StructMap.get(compseq).CIDDay);
                            //System.out.println("set: "+set);
                            if(set.size()>0){
                                for(Integer cid : set){
                                    TreeSet<Integer> adddayset = new TreeSet<>();
                                    for(Integer xd : StructMap.get(compseq).CIDDay.get(cid)){
                                        for(Integer yd : StructMap.get(seq).CIDDay.get(cid)){
                                            if(yd.equals(xd)){
                                                adddayset.add(yd);
                                            }
                                        }
                                        if(adddayset.size()>0){
                                            temp.CIDList.add(cid);
                                            temp.CIDDay.put(cid, adddayset);
                                        }
                                    }
                                }
                                StructMap.put(candidate, temp);
                                canset.add(candidate);
                            }
                        }
                    }else{//x2.last獨立項目及
                        //System.out.println("x2.last99999999");
                        String candidate = candiIdStream(compseq, seq);
                        //System.out.println("first : ");
                        //System.out.println("candidatex2.last獨立項目及 : "+candidate);
                      
                        
                        if(StructMap.containsKey(candidate)){
                            /*System.out.println("contain");
                            System.out.println("candidate: "+ candidate);
                            System.out.println("id: "+ StructMap.get(candidate).SeqId.ID);
                            System.out.println("CIDLIST: "+ StructMap.get(candidate).CIDList);
                            System.out.println("DAYLIST: "+ StructMap.get(candidate).CIDDay);
                            System.out.println("Lengh: "+ StructMap.get(candidate).Seqlengh);*/
                            SequenceStruct temp = StructMap.get(candidate);
                            //System.out.println("candidate: "+candidate);
                            HashSet<Integer> set = funchasnewday(seq, nowday);
                            /*System.out.println("funchaseneq.id : " +seq);
                            System.out.println("this?set: "+set);*/
                            //System.out.println("set: "+set);
                            if(set.size() > 0){ 
                                for(Integer cid : set){
                                    if(temp.CIDList.contains(cid)){
                                        temp.CIDDay.get(cid).add(nowday);
                                    }else{
                                        if(StructMap.get(compseq).CIDList.contains(cid)){
                                            TreeSet<Integer> sset = new TreeSet<>();
                                            for(Integer cday : StructMap.get(seq).CIDDay.get(cid)){
                                                if(StructMap.get(compseq).CIDDay.containsKey(cid)){
                                                    if(cday > StructMap.get(compseq).CIDDay.get(cid).first()){
                                                        sset.add(cday);
                                                    }
                                                }
                                            }
                                            if(sset.size()>0){
                                                temp.CIDDay.put(cid, sset);
                                                temp.CIDList.add(cid);
                                            }
                                        }
                                    }
                                }
                                StructMap.put(candidate, temp);
                                canset.add(candidate);
                            }
                        }else{
                            //System.out.println("nocontain");
                            SequenceStruct temp = new SequenceStruct();
                            SeqID tid = new SeqID();
                            tid.ID = candidate;
                            temp.SeqId = tid;
                            temp.Seqlengh = lv+1;
                            /*System.out.println("tid.id: "+temp.SeqId.ID);
                            System.out.println("seqlist"+StructMap.get(seq).CIDDay);
                            System.out.println("comlist"+StructMap.get(compseq).CIDDay);*/
                            HashSet<Integer> set = SameCidFunc(StructMap.get(seq).CIDList, StructMap.get(compseq).CIDList);
                            
                            //System.out.println("set: "+set);
                            
                            if(set.size()>0){
                                for(Integer cid : set){
                                    TreeSet<Integer> adddayset = new TreeSet<>();
                                    for(Integer yd : StructMap.get(seq).CIDDay.get(cid)){
                                        //System.out.println("yd: "+yd);
                                        if(yd > StructMap.get(compseq).CIDDay.get(cid).first()){
                                            adddayset.add(yd);
                                        }
                                    }
                                    if(adddayset.size()>0){
                                        temp.CIDList.add(cid);
                                        temp.CIDDay.put(cid, adddayset);
                                        //System.out.println("cid: "+cid+"add: "+adddayset);
                                    }
                                    
                                }
                                StructMap.put(candidate, temp);
                                //System.out.println("candida: "+candidate+" temp: "+temp.CIDDay);

                                canset.add(candidate);
                            }
                        }
                    }
                }
            }    
        }
        //System.out.println("retrun canset: "+canset);
        return canset;//return canset
    }

    // prefix x 
    private static String funcfirst(String seq){
        int idx1 = seq.indexOf("#");
        int idx2 = seq.indexOf("&");

        if(idx2 != -1){
            int idx = (idx1 < idx2) ? idx1 : idx2;
            return seq.substring(idx + 1, seq.length()-1);
        }else{
            int idx = idx1;
            return seq.substring(idx + 1, seq.length()-1);
        }
    }

    // postfix x 
    private static String funclast(String seq){
        String str = seq.substring(0, seq.length()-1);
        int idx1 = str.lastIndexOf("#");
        int idx2 = str.lastIndexOf("&");
        int idx = (idx1 > idx2) ? idx1 : idx2;
        return str.substring(0, idx);
    }
    
    private static String candiIdTogeter(String x, String y){
        //x(y.last)
        String[] str = y.split("[#&]");
        String xx = x.substring(0, x.length()-1);
        String candi = xx + "&" + str[str.length-1] + "#";

        return candi;
    }

    private static String candiIdStream(String x, String y){
        //xy.last
        String[] str = y.split("[&#]");
        String candi = x + str[str.length-1] + "#";
        
        return candi;
    }


    //input 2.candidatehas noupdate
    private static void delfunc(HashSet<String> noUpdateCandidateSet, HashSet<String> frequentseq, int lv, int MinSupCount){
        
        //沒有更新的才有機會 f==>inf
        for(String noUpCandidate : noUpdateCandidateSet){
            if(SubsequenceCheck(noUpCandidate, frequentseq) == true){
                //do nothing
            }else{
                StructMap.remove(noUpCandidate);
                //System.out.println("remove " + noUpCandidate);
            } 
        }

    }
    //subsequence is frequent return T
    private static boolean SubsequenceCheck(String seq, HashSet<String> frequentseq){
        String x = "";
        String y = "";
        
        String temp = seq.substring(0, seq.length()-1);
        int idx1 = temp.lastIndexOf("#");
        int idx2 = temp.lastIndexOf("&");
        int idx = (idx1 > idx2) ? idx1 : idx2;
        x = temp.substring(0, idx)+"#";
        
        String[] data = seq.split("[&#]");
        int idy1 = seq.indexOf("#");
        int idy2 = seq.indexOf("&");
        if(idy2 != -1){
            int idy = (idy1 < idy2) ? idy1 : idy2;
            y = seq.substring(idy + 1, seq.length()-1)+"#";
            
        }else{
            int idy = idy1;
            y = seq.substring(idy + 1, seq.length()-1)+"#";
            
        }

        if(frequentseq.contains(x) && frequentseq.contains(y)){
            return true;
        }else{
            return false;
        }
    }

    

    public static void main(String[] args) throws IOException {

        
        double startT = 0;
        double endT = 0;
        
        HashSet<Integer> additem = new HashSet<>();
        BufferedReader bfr = new BufferedReader(
            new FileReader("../../DataSet/TestData/testdel.csv")
        );

        String line = "";
        boolean EndDay = false;
        startT = System.currentTimeMillis();
        
        MemoryLogger.getInstance().reset();
        
        
        while(EndDay == false){
            System.out.println("-------------");
            System.out.println("Day : " + nowday);

            while((line = bfr.readLine())!= null){
                String[] data = line.split(",");
                int data0 = Integer.parseInt(data[0]);//day
                int data1 = Integer.parseInt(data[1]);//cid
                int data2 = Integer.parseInt(data[2]);//item
                
                CIDList.add(data1);

                if(data0 == nowday){
                    //create seq struct
                    String it = Integer.toString(data2)+"#";
                    if(StructMap.containsKey(it)){
                        UpdeSeqStucFunc(data0, data1, data2, StructMap.get(it));
                    }else{
                        SequenceStruct ss = CreatSeqStucFunc(data0, data1, data2);
                        StructMap.put(it, ss);
                    }
                    itemList.add(data2);
                    additem.add(data2);
                    

                }else if(data0 > nowday){
                    //  do some thing
                    int MinSupCount = (int) Math.ceil(CIDList.size()* minsup);

                    for(Integer x : additem){
                        for(Integer y : itemList){
                            //compare
                            //System.out.println("""inx:"+x"");
                            //System.out.println(":");
                            if(x!=y){
                                String strx = Integer.toString(x)+"#";
                                String stry = Integer.toString(y)+"#";
                                Compare(strx, stry, MinSupCount, nowday);
                            }
                            if(x==y){
                                String strx = Integer.toString(x)+"#";
                                String stry = Integer.toString(y)+"#";
                                CompareSameID(strx,stry,  MinSupCount, nowday);
                            }
                            //System.out.println("x :" + x + " y : "+y); 
                        }
                    }


                    //del********************
                    //找出１－frequent
                    HashSet<String> freqitemset = new HashSet<>();
                    for(Integer itm : itemList){
                        String stritm = Integer.toString(itm)+"#";
                        if(StructMap.get(stritm).CIDList.size() >= MinSupCount){
                            freqitemset.add(stritm);
                        }
                    }
                    //System.out.println("freqitemset: "+freqitemset);

                    HashSet<String> noUpdateCandidateSet = new HashSet<>();
                    for(String k : StructMap.keySet()){
                        if(StructMap.get(k).Seqlengh == 2 && freqitemset.contains(k)==false ){
                            
                            noUpdateCandidateSet.add(k);
                        }
                    }
                    //System.out.println("noUpdateCandidateSet: "+noUpdateCandidateSet);

                    delfunc(noUpdateCandidateSet, freqitemset, 2, MinSupCount);

                    //del*********************


                
                    Compare2candidate(New2Candate, MinSupCount, 2, nowday);
                    New2Candate.clear();
                    additem.clear();
                    
                    


                    //  create seq struct
                    String it = Integer.toString(data2)+"#";
                    if(StructMap.containsKey(it)){
                        UpdeSeqStucFunc(data0, data1, data2, StructMap.get(it));
                    }else{
                        SequenceStruct ss = CreatSeqStucFunc(data0, data1, data2);
                        StructMap.put(it, ss);
                    }
                    itemList.add(data2);
                    additem.add(data2);
                    nowday++;
                    break;
                }
                                    
            }
            
            if(line == null){
                EndDay = true;
                bfr.close();
                // do some thing
                int MinSupCount = (int) Math.ceil(CIDList.size() * minsup);
                //System.out.println("key : " + StructMap.keySet());
                
                for(Integer x : additem){
                    for(Integer y : itemList){
                        //compare
                        //System.out.println("inx:"+x);
                        if(x !=y ){
                            String strx = Integer.toString(x)+"#";
                            String stry = Integer.toString(y)+"#";
                            Compare(strx, stry, MinSupCount, nowday);
                        }
                        if(x==y){
                            String strx = Integer.toString(x)+"#";
                            String stry = Integer.toString(y)+"#";
                            CompareSameID(strx,stry,  MinSupCount, nowday);
                        }
                        
                    }
                }


                    //del********************
                    //找出１－frequent
                    HashSet<String> freqitemset = new HashSet<>();
                    for(Integer itm : itemList){
                        String stritm = Integer.toString(itm)+"#";
                        if(StructMap.get(stritm).CIDList.size() >= MinSupCount){
                            freqitemset.add(stritm);
                        }
                    }
                    //System.out.println("freqitemset: "+freqitemset);

                    HashSet<String> noUpdateCandidateSet = new HashSet<>();
                    for(String k : StructMap.keySet()){
                        if(StructMap.get(k).Seqlengh == 2 && freqitemset.contains(k)==false){
                            noUpdateCandidateSet.add(k);
                        }
                    }
                    //System.out.println("noUpdateCandidateSet: "+noUpdateCandidateSet);
                    delfunc(noUpdateCandidateSet, freqitemset, 2, MinSupCount);



                    //del*********************

        
                

                Compare2candidate(New2Candate, MinSupCount, 2, nowday);
                New2Candate.clear();
                
            }
            
        }
/*********************************************************************************************************************
                                        create sequence struct
 **********************************************************************************************************************/
        //MemoryLogger.getInstance().checkMemory();
        endT = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
        int i =0;
        
        double t = (endT - startT);
        System.out.println("Time : "+t/1000 + " sec");
        System.out.println( "Memory increased:" + MemoryLogger.getInstance().getMaxMemory() + " MB");
        //System.out.println("SUP: "+CIDList.size());
        //System.out.println("f: "+i);
        System.out.println("befor keyset: "+StructMap.keySet());
        System.out.println("done");
        
        
/*    
 *
 *  
 *                                      start deltion
 * 
 *            
 * 
 * 
*/      
        // put frequent pattern 
        ArrayList<String> sequencepattern = new ArrayList<>();
        
        // start del
        while(nowdelday <3){
            System.out.println("////////////////////////////////////////////////////");
            System.out.println("now del day is " + nowdelday);
            ArrayList<String> beforefseq = new ArrayList<>();
            //put sequence pattern
            System.out.println("check 40#: "+ StructMap.get("40#").CIDDay);
            System.out.println("check 40#: "+ StructMap.get("40#").CIDList.size());
            System.out.println("(int) Math.ceil(CIDList.size()* minsup): " + (int) Math.ceil(CIDList.size()* minsup));
            for(String k : StructMap.keySet()){
                if(StructMap.get(k).CIDList.size() >= (int) Math.ceil(CIDList.size()* minsup) ){
                    beforefseq.add(k);
                }
            }
            
            System.out.println("beforefseq: " + beforefseq);
            
            TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> delItemdaylistInfoMap = new TreeMap<>();
            //check minsup
            
            HashSet<String> supdownCID = new HashSet<>(); //有減少ＳＵＰ的
            HashSet<Integer> Cidset = new HashSet<>();//檢查customer
            for(String seq1 : StructMap.keySet() ){
                if(StructMap.get(seq1).Seqlengh == 1){//對每個長度為１的
                    SequenceStruct temp = StructMap.get(seq1);
                    for(Integer CID : temp.CIDDay.keySet()){//對每個ＣＩＤ的ＤＡＹ
                        TreeSet<Integer> tday = temp.CIDDay.get(CID);
                        if(tday.contains(nowdelday)){
                            //add del item to map
                            if(delItemdaylistInfoMap.containsKey(seq1)){
                                TreeMap<Integer, TreeSet<Integer>> tmap = delItemdaylistInfoMap.get(seq1);
                                TreeSet<Integer> tset = new TreeSet<>();
                                tset.add(nowdelday);
                                tmap.put(CID, tset);
                                delItemdaylistInfoMap.put(seq1, tmap);
                            }else{
                                TreeSet<Integer> tset = new TreeSet<>();
                                tset.add(nowdelday);
                                TreeMap<Integer, TreeSet<Integer>> tmap = new TreeMap<>();
                                tmap.put(CID, tset);
                                delItemdaylistInfoMap.put(seq1, tmap);
                            }
                            
                            if(tday.size() == 1){
                                //no day
                                supdownCID.add(seq1);
                                temp.CIDList.remove(CID);//直接移除structmap的list
                            }else{
                                Cidset.add(CID);
                            }
                        }else{
                            Cidset.add(CID);
                        }
                    }
                }
            }
            afterMinSupCount = (int) Math.ceil(Cidset.size() * minsup);
            System.out.println("afterMinSupCount: " + afterMinSupCount);
            CIDList.clear();
            for(Integer ii: Cidset){
                CIDList.add(ii);
            }
            ArrayList<String> frequentsequencelist = new ArrayList<>();
            int lv = 1;
            System.out.println("check 40#: "+ StructMap.get("40#").CIDDay);
            System.out.println("check 40#: "+ StructMap.get("40#").CIDList);
            //直到組合不出來
            while(/*frequentsequencelist.size() > 1 */lv < 5){
                //put k+1.freqent
                System.out.println("-------------------------------");
                System.out.println("loop: " + lv);
                //find each case 
                ArrayList<String> f2inflist = new ArrayList<>(); 
                
                //f-> inf ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                for(String seq : beforefseq){
                    SequenceStruct temp = StructMap.get(seq);
                    if(temp != null){
                        if(temp.Seqlengh == lv && temp.CIDList.size() < afterMinSupCount ){
                            System.out.println("f -> inf : " + seq);
                            f2inflist.add(seq);
                        }    
                    }
                }
                //del f -> inf candidate
                for(String k: findcandidatefunc(f2inflist)){
                    StructMap.remove(k);
                }
                
                //f-> f++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                ArrayList<String> f2flist = new ArrayList<>();
                
                for(String seq : beforefseq){
                    SequenceStruct temp = StructMap.get(seq);
                    if(temp != null){
                        if(temp.Seqlengh == lv && temp.CIDList.size() >= afterMinSupCount){
                            System.out.println("f -> f : " + seq);
                            f2flist.add(seq);
                            frequentsequencelist.add(seq);
                        }

                    }
                }

                //mark daylist 
                TreeMap<String, TreeMap<Integer, TreeSet<Integer>>> delcandidatelistInfoMap = deldaylistfunc(f2flist, delItemdaylistInfoMap);
                //System.out.println("return : " + delcandidatelistInfoMap);
                
                //del daylist
                delmarkdaylist(delcandidatelistInfoMap);
                
                //del nulllist
                for(String str : delcandidatelistInfoMap.keySet()){
                    delnulldaylist(str);
                }
                
                //del 1-struct
                if(lv == 1){
                    delmarkdaylist(delItemdaylistInfoMap);
                    //del nulllist
                    for(String str : delItemdaylistInfoMap.keySet()){
                        delnulldaylist(str);
                    }
                }
                
                //inf-> f++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                ArrayList<String> inf2flist = new ArrayList<>();
                
                for(String seq : StructMap.keySet()){
                    SequenceStruct temp = StructMap.get(seq);
                    if(temp.Seqlengh == lv && !sequencepattern.contains(seq) && temp.CIDList.size() >= afterMinSupCount){
                        System.out.println("inf -> f : "+ seq);
                        inf2flist.add(seq);
                        frequentsequencelist.add(seq);
                    }
                }
                
                System.out.println("frequentsequencelist: " + frequentsequencelist);
                //create to each other 
                createfreqfunc(inf2flist, lv);

                delItemdaylistInfoMap = delcandidatelistInfoMap;
                
                lv++;
            }
            sequencepattern = frequentsequencelist;
            System.out.println("sequencepattern: " +sequencepattern );
            //next day
            Cidset.clear();
            nowdelday++;
            for(String str : StructMap.keySet()){
                //if(StructMap.get(str).Seqlengh){
                    System.out.println("ID: " + str);
                    System.out.println("sup: " + StructMap.get(str).CIDList.size());
                    System.out.println("CidList: " + StructMap.get(str).CIDList);
                    System.out.println("CiDDay: " + StructMap.get(str).CIDDay);
                    System.out.println(">>>>>>>");
                //}
            }
            
        }


        System.out.println(StructMap.keySet());
        System.out.println("del done");
        

/*
                for(String str : StructMap.keySet()){
                    if(StructMap.get(str).Seqlengh == 3 ){
                        System.out.println("ID: " + str);
                        System.out.println("sup: " + StructMap.get(str).CIDList.size());
                        System.out.println("CidList: " + StructMap.get(str).CIDList);
                        System.out.println("CiDDay: " + StructMap.get(str).CIDDay);
                        System.out.println(">>>>>>>");
                    }
                }
   */             
    }
    
}