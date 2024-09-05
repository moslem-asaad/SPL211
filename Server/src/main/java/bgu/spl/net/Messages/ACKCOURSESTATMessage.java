package bgu.spl.net.Messages;

import bgu.spl.net.srv.Database;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ACKCOURSESTATMessage implements ServerMessage {

    private Database database =Database.getInstance();
    private ConcurrentLinkedQueue<String> listofstudents;
    private  LinkedList<String> liststudents;
    private final short opcode;
    private final short acop;
    private final short coursenumber;
    private byte[] bytes;
    private int index;
    private String student_list;
    private final String available_max;
    private final String coursenum_coursename;
    private byte[] tosend;
    private String user;

    public ACKCOURSESTATMessage(short op , short coursenumber ,String user){
        this.coursenumber=coursenumber;
        this.user=user;
        listofstudents=database.getCoursenum_studentreg().get(this.coursenumber);
        opcode=op;
        acop=12;
        bytes= new byte[1<<13];
        index=4;
        liststudents= new LinkedList<>();
        student_list="Students Registered: [";
        available_max = "Seats Available: "+(database.getCoursenum_maxNumOfSeat().get(coursenumber)-database.getCoursenum_numofregesters().get(this.coursenumber).get())+
                "/"+database.getCoursenum_maxNumOfSeat().get(coursenumber)+'\n';
        coursenum_coursename = "Course: ("+this.coursenumber+") "+database.getCoursenum_name().get(coursenumber)+'\n';
    }


    @Override
    public byte[] encode() {
        byte[] b = shortToBytes(acop);
        bytes[0]=b[0];
        bytes[1]=b[1];
        b=shortToBytes(opcode);
        bytes[2]=b[0];
        bytes[3]=b[1];

        makethestringbytes(coursenum_coursename);
        makethestringbytes(available_max);
        studintlist_string();
        makethestringbytes(student_list);

        tosend = new byte[index+1];
        for (int i=0;i<tosend.length;i++){
            tosend[i]=bytes[i];
        }
        return tosend;
    }
    private void movestudents(){
        ConcurrentLinkedQueue<String> l = new ConcurrentLinkedQueue<>();
        while (!listofstudents.isEmpty()){
            l.add(listofstudents.remove());
        }
        while (!l.isEmpty()) {
            liststudents.addLast(l.peek());
            listofstudents.add(l.remove());
        }
    }
    private void order_the_list(){
        liststudents.sort(Comparator.comparing(String::toString));
    }
    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private void studintlist_string(){
        movestudents();
        order_the_list();
        for (int i=0;i<liststudents.size();i++){
            student_list=student_list+liststudents.get(i)+",";
        }
        if (liststudents.size()>0) {
            student_list = student_list.substring(0, student_list.length() - 1);
        }
     //   student_list=student_list.replaceAll(" ","");
        student_list=student_list+"]";
     //   System.out.println("the user is: "+ user + " the student that regested to :"+ coursenumber+" "+ student_list);
    }
    private void makethestringbytes(String out){
        byte[] list = out.getBytes(StandardCharsets.UTF_8);
        for (int i=0;i<list.length;i++){
            bytes[index]= list[i];
            index++;
        }
    }
}
