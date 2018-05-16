package fusp;

import java.util.ArrayList;

public class test{
    public static void main(String[] args) {
        FUSPTree Tree = new FUSPTree();
        ArrayList<ArrayList<Integer>> aalist = new ArrayList<>();
        ArrayList<Integer> a1 = new ArrayList<>(); a1.add(10);a1.add(20);a1.add(30);
        ArrayList<Integer> a2 = new ArrayList<>(); a2.add(20);
        aalist.add(a1);
        ArrayList<ArrayList<Integer>> aalist1 = new ArrayList<>();
        FUSPNode n = new FUSPNode();
        //Tree.addTranscation(aalist);
        
        aalist1.add(a2);
        
        System.out.println("aalist1: " + aalist1);
        System.out.println("check : " + aalist1.get(0).isEmpty());
        
        Tree.addTranscation(aalist1);
        Tree.addTranscation(aalist);
        System.out.println("Tree.root : " + Tree.root.childs.get(1).itemID );
    }
}