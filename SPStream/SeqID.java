package spstream;


class SeqID{
    String ID = "";
    
    public void setItemid(int id){
        this.ID= Integer.toString(id)+"#"; 
    }
    //x,y = xy
    public void setItemid(String x, String y){
        this.ID = x+y;
    }
    //x,y = (xy)
    public void setItemidn(String x, String y){
        String[] data = x.split("#");
        this.ID = data[0]+"&"+y;
    }
    
    public void ShowtheSeqID(){
        String[] data = ID.split("#");
        
        for(String str : data ){
            System.out.println(str);
        }
    }
    public String getSeqIDwithid(int id){
        String str = Integer.toString(id)+"#";
        if(str == this.ID){
            return str;
        }else{
            return null;
        }
    }
}