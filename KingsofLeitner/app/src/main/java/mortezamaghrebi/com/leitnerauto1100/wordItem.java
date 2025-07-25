package mortezamaghrebi.com.leitnerauto1100;


public class wordItem {
    int id;
    String word="",persian="",definition="",review="",pronounce="",sound="",example="",examplefa="";
    int started=0,day=0,finished=0,lastheart=0;
    public void setparam(String param,String value)
    {
        switch (param)
        {
            case "id":
                id=Integer.parseInt(value);
                break;
            case "word":
                word = value;
                break;
            case "day":
                day = Integer.parseInt(value);
                 break;
            case "persian":
                persian = value;
                break;
            case "definition":
                definition = value;
                break;
            case "pronounce":
                pronounce = value;
                break;
            case "examplefa":
                examplefa = value.replace("\r","");
                break;
            case "example":
                example = value;
                break;
            case "review":
                review = value;
                break;
            case "started":
                started = Integer.parseInt(value);
                break;
            case "finished":
                finished = Integer.parseInt(value);
                break;
            case "lastheart":
                lastheart = Integer.parseInt(value);
                break;
        }
    }
    @Override
    public String toString() {
        return "wordItem{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", day='" + day + '\'' +
                ", persian='" + persian + '\'' +
                ", pronounce='" + pronounce + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }

    public int box(){ //returns 1 to 16 box levels for the word
        String review=this.review;
        int box=0;
        for(int i=0;i<review.length();i++)
        {
            String c= String.valueOf(review.charAt(i));
            if(c.equals("t"))box++;
            else if(c.equals("a")){ if(box>0) box--;} //help1
            else if(c.equals("b")){ if(box>0) box--;} //help2
            else if(c.equals("c")){ if(box>1) box-=2; else if(box>0) box--;} //help3
            else if(c.equals("m"))box++; //matches
            else if(c.equals("e")){ if(box>0) box--;} //empty
            else if(c.equals("w")){ if(box>1) box-=2; else if(box>0) box--;} //wrong
            else if(c.equals("f"))
            {
                if(box<=4)box=0;
                else if (box<=8) box=4;
                else if (box<=11) box=8;
                else if (box<=13) box=11;
                else if (box<15) box=13;
            }
            if(box>=15) break;
        }
        return box;
    }

    public int wrongpercent(){
        String review=this.review;
        int wrong=0;
        int count=review.length();
        if(count==0)return 0;
        for(int i=0;i<review.length();i++)
        {
            String c= String.valueOf(review.charAt(i));
            if(c.equals("w")){wrong++;}
            else if(c.equals("f")) {wrong++;}
            else if(c.equals("e")) {wrong++;}
        }
        return wrong*1000/count;
    }
}