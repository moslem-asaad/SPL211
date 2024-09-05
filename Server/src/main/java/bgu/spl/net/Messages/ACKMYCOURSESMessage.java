package bgu.spl.net.Messages;
import bgu.spl.net.srv.Database;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class ACKMYCOURSESMessage implements ServerMessage  {
    private Database database =Database.getInstance();
    private LinkedList<Short> mycourseslist;
    private String username;
    private final short opcode;
    private final short acop;
    private byte[] bytes;
    private String out;
    private byte[] tosend;
    private int index;


    public ACKMYCOURSESMessage(short op , String username) {
        opcode=op;
        acop=12;
        this.username=username;
        mycourseslist=database.getRegestedcourses().get(this.username);
        bytes = new byte[1<<13];
        index=4;
        out="[";
    }

    @Override
    public byte[] encode() {
        byte[] b = shortToBytes(acop);
        bytes[0]=b[0];
        bytes[1]=b[1];
        b=shortToBytes(opcode);
        bytes[2]=b[0];
        bytes[3]=b[1];

        makethestringbytes();
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
    private void makemycourses_string(){
        order_the_list();
        for (int i=0;i<mycourseslist.size();i++){
            out=out+mycourseslist.get(i)+",";
        }
        if (mycourseslist.size()>0) {
            out = out.substring(0, out.length() - 1);
        }
        out=out+"]";

    }
    private void makethestringbytes(){
        makemycourses_string();
        byte[] list = out.getBytes(StandardCharsets.UTF_8);

        for (int i=0;i<list.length;i++){
            bytes[index]= list[i];
            index++;
        }
        bytes[index]=0;
    }
}
