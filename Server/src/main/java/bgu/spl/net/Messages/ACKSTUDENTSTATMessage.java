package bgu.spl.net.Messages;

import bgu.spl.net.srv.Database;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;

public class ACKSTUDENTSTATMessage implements ServerMessage {

    private Database database =Database.getInstance();
    private LinkedList<Short> mycourseslist;
    private String username;
    private final short opcode;
    private final short acop;
    private byte[] bytes;
    private String courses;
    private int index;
    private byte[] tosend;

    public ACKSTUDENTSTATMessage(short op , String username){
        this.username=username;
        if (database.getRegestedcourses().get(this.username)!=null) {
            mycourseslist = database.getRegestedcourses().get(this.username);
        }
        else{
            mycourseslist= new LinkedList<>();
        }
        opcode=op;
        acop=12;
        bytes=new byte[1<<13];
        courses="Courses: [";
        index=4;
    }


    @Override
    public byte[] encode() {
        byte[] b = shortToBytes(acop);
        bytes[0]=b[0];
        bytes[1]=b[1];
        b=shortToBytes(opcode);
        bytes[2]=b[0];
        bytes[3]=b[1];
        makethestringbytes("Student: "+ this.username+"\n");
        makethestringbytes(makemycourses_string());
        bytes[index]=0;

        tosend = new byte[index+1];
        for (int i=0;i<tosend.length;i++){
            tosend[i]=bytes[i];
        }
        return tosend;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private void order_the_list(){
        LinkedList<Short> l = database.getCourses();
        LinkedList<Short> order = new LinkedList<>();
        for (int i =0 ;i<l.size();i++){
            if (mycourseslist.contains(l.get(i))) order.addLast(l.get(i));
        }
        mycourseslist=order;
    }
    private String makemycourses_string(){
        order_the_list();
        for (int i=0;i<mycourseslist.size();i++){
            courses=courses+mycourseslist.get(i)+",";
        }
        if (mycourseslist.size()>0) {
            courses = courses.substring(0, courses.length() - 1);
        }
        courses=courses+"]";
        return courses;
    }

    private void makethestringbytes(String out){
        byte[] list = out.getBytes(StandardCharsets.UTF_8);
        for (int i=0;i<list.length;i++){
            bytes[index]= list[i];
            index++;
        }
    }


}
