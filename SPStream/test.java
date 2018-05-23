package spstream;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class test {
    public static ArrayList<SequenceStruct> SeqStructList = new ArrayList<>(); // all seqstruct
    public static HashMap<String, SequenceStruct> StructMap = new HashMap<>();
    private static HashSet<Integer> SameCidFunc(HashSet<Integer> x, HashSet<Integer> y){
        HashSet<Integer> set = new HashSet<>();
        for(Integer q : x){
            for(Integer p : y){
                if(q==p){
                    set.add(q);
                }
            }
        }
        return set;
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
        for(Integer i : X.CIDDay.keySet()){
            for(Integer j : Y.CIDDay.keySet()){
                if(i == j && X.CIDDay.keySet().contains(nowday)){
                    set.add(i);
                }
            }
        }
        return set;
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
    private static HashSet<String> generation(HashSet<String> freqset, int lv, int nowday, int MinSupCount){
        HashSet<String> canset = new HashSet<>();//put candidate
        HashSet<String> lvset = new HashSet<>();
        for(String k : StructMap.keySet()){
            if(StructMap.get(k).Seqlengh == lv && StructMap.get(k).CIDList.size() >= MinSupCount){
                lvset.add(k);
            }
        }
        /*System.out.println("lvset: "+lvset);
        System.out.println("freqset: "+freqset);*/

        for(String seq : freqset){
            String prefixseq = funcfirst(seq);
            String postfixseq = funclast(seq);
            for(String compseq : lvset){
                String prefixcom = funcfirst(compseq);
                String postfixcom = funclast(compseq );
                /******************************************************************** */
                /*System.out.println("????????????????????????????????????????????????????????????");
                System.out.println("seq: "+seq+" compseq: "+compseq);
                System.out.println("prefixseq: "+prefixseq);
                System.out.println("postfixseq: "+postfixseq);
                System.out.println("prefixcom: "+prefixcom);
                System.out.println("postfixcom: "+postfixcom);*/
                
                if(prefixseq.equals(postfixcom)){//"bc" cb
                    //System.out.println("eql: "+prefixseq+" "+postfixcom);
                    
                    //2case
                    String[] check = compseq.split("#");

                    if(check[check.length-1].contains("&")){//x2.last不是獨立項目及
                        
                        String candidate = candiIdTogeter(seq, compseq);
                        if(StructMap.containsKey(candidate)){
                            SequenceStruct temp = StructMap.get(candidate);
                            HashSet<Integer> set = funchasnewday(seq, compseq, nowday);
                            for(Integer cid : set){
                                temp.CIDList.add(cid);
                                TreeSet<Integer> tset = new TreeSet<>();
                                tset = temp.CIDDay.get(cid);
                                tset.add(nowday);
                                temp.CIDDay.put(cid, tset);
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
                            if(set.size()>0){
                                for(Integer cid : set){
                                    TreeSet<Integer> adddayset = new TreeSet<>();
                                    for(Integer xd : StructMap.get(seq).CIDDay.get(cid)){
                                        for(Integer yd : StructMap.get(compseq).CIDDay.get(cid)){
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
                        //System.out.println("x2.last");
                        String candidate = candiIdStream(seq, compseq);
                        //System.out.println("candidate: "+candidate);
                        
                        if(StructMap.containsKey(candidate)){
                            SequenceStruct temp = StructMap.get(candidate);
                            HashSet<Integer> set = funchasnewday(compseq ,nowday);
                            if(set.size() > 0){ 
                                for(Integer cid : set){
                                    if(temp.CIDList.contains(cid)){
                                        temp.CIDDay.get(cid).add(nowday);
                                    }else{
                                        if(StructMap.get(seq).CIDList.contains(cid)){
                                            TreeSet<Integer> sset = new TreeSet<>();
                                            for(Integer cday : StructMap.get(compseq).CIDDay.get(cid)){
                                                if(StructMap.get(seq).CIDDay.containsKey(cid)){
                                                    if(cday > StructMap.get(seq).CIDDay.get(cid).first()){
                                                        sset.add(cday);
                                                    }
                                                }
                                            }
                                            temp.CIDDay.put(cid, sset);
                                            temp.CIDList.add(cid);
                                        }
                                    }
                                }
                                StructMap.put(candidate, temp);
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
                                        
                                        if(yd > StructMap.get(seq).CIDDay.get(cid).first()){
                                            adddayset.add(yd);
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
                    String[] check = postfixseq.split("#");
                    
                    if(check[check.length-1].contains("&")){//x2.last不是獨立項目及
                        String candidate = candiIdTogeter(compseq, seq);
                        if(StructMap.containsKey(candidate)){
                            SequenceStruct temp = StructMap.get(candidate);
                            HashSet<Integer> set = funchasnewday(seq, compseq, nowday);
                            for(Integer cid : set){
                                temp.CIDList.add(cid);
                                TreeSet<Integer> tset = new TreeSet<>();
                                tset = temp.CIDDay.get(cid);
                                tset.add(nowday);
                                temp.CIDDay.put(cid, tset);
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
                        
                        
                        if(StructMap.containsKey(candidate)){
                            /*System.out.println("contain");
                            System.out.println("candidate: "+ candidate);
                            System.out.println("id: "+ StructMap.get(candidate).SeqId.ID);
                            System.out.println("CIDLIST: "+ StructMap.get(candidate).CIDList);
                            System.out.println("DAYLIST: "+ StructMap.get(candidate).CIDDay);
                            System.out.println("Lengh: "+ StructMap.get(candidate).Seqlengh);*/
                            SequenceStruct temp = StructMap.get(candidate);
                            
                            HashSet<Integer> set = funchasnewday(seq ,nowday);
                            /*System.out.println("funchaseneq.id : " +seq);
                            System.out.println("this?set: "+set);*/
                            if(set.size() > 0){ 
                                for(Integer cid : set){
                                    if(temp.CIDList.contains(cid)){
                                        temp.CIDDay.get(cid).add(nowday);
                                    }else{
                                        if(StructMap.get(seq).CIDList.contains(cid)){
                                            TreeSet<Integer> sset = new TreeSet<>();
                                            for(Integer cday : StructMap.get(seq).CIDDay.get(cid)){
                                                if(StructMap.get(compseq).CIDDay.containsKey(cid)){
                                                    if(cday > StructMap.get(compseq).CIDDay.get(cid).first()){
                                                        sset.add(cday);
                                                    }
                                                }
                                            }
                                            temp.CIDDay.put(cid, sset);
                                            temp.CIDList.add(cid);
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
                            //System.out.println("tid.id: "+temp.SeqId.ID);
                            HashSet<Integer> set = SameCidFunc(StructMap.get(seq).CIDList, StructMap.get(compseq).CIDList);
                            if(set.size()>0){
                                for(Integer cid : set){
                                    TreeSet<Integer> adddayset = new TreeSet<>();
                                    for(Integer xd : StructMap.get(seq).CIDDay.get(cid)){
                                        for(Integer yd : StructMap.get(compseq).CIDDay.get(cid)){
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
                    }
                }
            }    
        }
        System.out.println("retrun canset: "+canset);
        return canset;//return canset
    }

    private static String funcgenerationcheck(String seq){
        String str = seq.substring(0, seq.length()-1);
        int idx1 = str.lastIndexOf("#");
        int idx2 = str.lastIndexOf("&");
        int idx = (idx1 > idx2) ? idx1 : idx2;
        return str.substring(idx, idx+1);
    }
    private static String funclastitem(String seq){
        String str = seq.substring(0, seq.length()-1);
        int idx1 = str.lastIndexOf("#");
        int idx2 = str.lastIndexOf("&");
        int idx = (idx1 > idx2) ? idx1 : idx2;
        return str.substring(idx+1);
    }


    public static void main(String[] args) {
        String seq = "30&50#";
        String compseq = "30#40#";
        
        String candidata1 = seq + funclastitem(compseq) + "#";
        String candidata2 = compseq +funclastitem(seq) + "#";
        String candidata3 = funclast( seq ) + "#" + funclastitem(( Integer.parseInt(funclastitem(seq)) < Integer.parseInt(funclastitem(compseq))  ) ? seq : compseq ) + "&" + funclastitem(( Integer.parseInt(funclastitem(seq)) > Integer.parseInt(funclastitem(compseq))  ) ? seq : compseq )+"#";
        String candidate4 = funclast( seq ) + "&" + funclastitem(( Integer.parseInt(funclastitem(seq)) < Integer.parseInt(funclastitem(compseq))  ) ? seq : compseq ) + "&" + funclastitem(( Integer.parseInt(funclastitem(seq)) > Integer.parseInt(funclastitem(compseq))  ) ? seq : compseq )+"#";
        System.out.println(candidata1);
        System.out.println(candidata2);
        System.out.println(candidata3);
        System.out.println(candidate4);
        System.out.println( seq + funclastitem(seq) + "#");
        String[] checkc = compseq.split("#");
        String[] checks = seq.split("#");
        String candidate = "";
        if(checkc[checkc.length-1].contains("&")){
            candidate = compseq + funclastitem(seq) + "#" ;
        }else if(checks[checks.length-1].contains("&")){
            candidate = seq + funclastitem(compseq) + "#" ;
        }
        System.out.println(candidate);


    }

    
}