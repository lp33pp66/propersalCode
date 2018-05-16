package spstream;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import spstream.SeqID;

class SequenceStruct{
    SeqID SeqId = null; //sequence
    HashSet<Integer> CIDList= new HashSet<>();//CIDList.size() = supcount
    TreeMap<Integer, TreeSet<Integer>> CIDDay = new TreeMap<>();//(k, v) = (cid, daylist)
    int Seqlengh =1; 






    
    public SequenceStruct(){

    }
    public SequenceStruct(int day, int Cid, int ID){
        SeqID seqid = new SeqID();
        seqid.setItemid(ID);
        this.SeqId = seqid;
        CIDList.add(Cid);
        TreeSet<Integer> tset = new TreeSet<>(CIDDay.get(Cid));
        tset.add(day);
        CIDDay.put(Cid, tset);
    }
    
    public void AddCID(){
        
    }

}