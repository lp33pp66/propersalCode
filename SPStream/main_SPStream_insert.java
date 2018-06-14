package spstream;
/**
 * sup 要改
 * 總客數要改
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import spstream.SeqID;
import spstream.SequenceStruct;
import spstream.MemoryLogger;





public class main_SPStream_insert{
    public static int nowday = 1;
    public static double minsup = 0.017;
    public static HashSet<Integer> CIDList = new HashSet<>();
    public static HashSet<Integer> itemList = new HashSet<>();// All 1-item set
    //public static ArrayList<SequenceStruct> SeqStructList = new ArrayList<>(); // all seqstruct
    public static HashMap<String, SequenceStruct> StructMap = new HashMap<>();
    public static HashSet<String> New2Candate = new HashSet<>();//put 2-candidate
    



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
                 System.out.println("seq: "+StructMap.containsKey(seq));
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
                if(StructMap.get(k).Seqlengh == lv && freqset.contains(k)==false){//del condidation is fail
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
                //System.out.println("compseq:"+ compseq + " postfixcom: " + postfixcom);
                //System.out.println("seq"+ seq + " postfixseq: " + postfixseq);
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
            new FileReader("../../DataSet/TestData/S4I2N1KD1K-3.csv")
        );

        String line = "";
        boolean EndDay = false;
        startT = System.currentTimeMillis();
        ArrayList<String> timecode = new ArrayList<>();
        double ttime = startT;
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
                    int MinSupCount = (int) Math.ceil(CIDList.size() * minsup);

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

            if(nowday == 0 || nowday == 7 ||nowday == 14 ||nowday == 21 ||nowday == 28 ||nowday == 35 ||nowday == 42 ||nowday == 49 ||nowday == 56 ||nowday == 63 ||nowday == 70 ){
                double nowtime = System.currentTimeMillis();
                
                double midtime = nowtime - ttime;
                ttime = nowtime;
                timecode.add("Time"+nowday+ " : "+ midtime/1000 + " sec");
                
                
            }
            
        }
        
        MemoryLogger.getInstance().checkMemory();
        endT = System.currentTimeMillis();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
        
        double t = (endT - startT);
        
        System.out.println("Time : "+t/1000 + " sec");
        System.out.println("timecode: " + timecode);
        System.out.println( "Memory increased:" + MemoryLogger.getInstance().getMaxMemory() + " MB");
        System.out.println("SUP: "+CIDList.size());
        //System.out.println("f: "+i);
        System.out.println("done"); 
    
    }
    
    
}

