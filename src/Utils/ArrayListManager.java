package Utils;

import User_Classes.Customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ArrayListManager<T extends Comparable>{

    protected ArrayList<T> mylist = new ArrayList<T>();
    protected int mcount;

    public ArrayListManager(){
        mcount = 0;
    }//end of constructor

    public int addAtEnd(T x){
        mylist.add(mcount++,x);
        return mcount;
    }//end of addAtEnd

    public int getMcount(){
        return mcount;
    }//end of getMcount

    public int addInOrder(T x){
        int i;
        if ((mcount==0)|| ((x.compareTo(mylist.get(0)))==-1)||(x.compareTo(mylist.get(0))==0)){
            mylist.add(0,x);
        }
        else if ((x.compareTo(mylist.get(mcount- 1))==1)||(x.compareTo(mylist.get(mcount-1))==0)){
            mylist.add(mcount,x);
        }
        else {
            i=0;
            while((i<mcount)&&(x.compareTo(mylist.get(i))==1))i++;
            mylist.add(i,x);
        }
        mcount++;
        return mcount;
    }//end of addInOrder

    public int addAtFront(T x){
        mylist.add(0,x);
        mcount++;
        return mcount;
    }//end of addAtFront

    public T getValue(int i){
        if (i<mcount) return mylist.get(i);
        else return mylist.get(0);
    }//end of getValue

    public void ManageAndSort(){
        T xsave,ysave,a,b;
        int isw = 1,xlast=mylist.size();
        while(isw==1){
            isw= 0;
            for (int i = 0; i < xlast-2; i++) {
                a=mylist.get(i);
                b=mylist.get(i+1);
                switch (a.compareTo(b)){
                    case 1:
                        break;
                    case -1:
                        xsave=mylist.get(i);
                        ysave=mylist.get(i+1);
                        mylist.remove(i);
                        mylist.add(i,ysave);
                        mylist.remove(i+1);
                        mylist.add(i+1,xsave);
                        isw=1;
                        break;
                    default:
                }//end of switch
            }//end of for
        }//end of while
    }//end of ManageAndSort

    public void removeM(int i){
        if (i>=0&&i<mcount){
            mylist.remove(i);
            mcount--;
        }
    }//end of removeM

}//end of GenericManager