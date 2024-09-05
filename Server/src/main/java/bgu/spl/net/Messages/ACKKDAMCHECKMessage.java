package bgu.spl.net.Messages;

import bgu.spl.net.srv.Database;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class ACKKDAMCHECKMessage implements ServerMessage {

    private Database database =Database.getInstance();
    private LinkedList<Short> kdamlist;
    private final short opcode;
    private final short acop;
    private byte[] bytes;
    private String kdam ;
    int index;
    private byte [] tosend;

    public ACKKDAMCHECKMessage(short op , short coursenumber){
        kdamlist= database.getCoursenum_kdamlist().get(coursenumber);
        opcode=op;
        acop=12;
        bytes= new byte[1<<13];
        kdam="[";
        index =4;
    }


    @Override
    public byte[] encode() {
        byte[] b = shortToBytes(acop);
        bytes[0]=b[0];
        bytes[1]=b[1];
        b=shortToBytes(opcode);
        bytes[2]=b[0];
        bytes[3]=b[1];

        makethestringbytes(kdam);
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
    private String kdamlist_string(){

        for (int i=0;i<kdamlist.size();i++){
            kdam=kdam+kdamlist.get(i)+",";
        }
        if (kdamlist.size()>0) {
            kdam = kdam.substring(0, kdam.length() - 1);
        }
        kdam=kdam+"]";
        return kdam;
    }
    private void makethestringbytes(String out){
        out= kdamlist_string();
        byte[] list = out.getBytes(StandardCharsets.UTF_8);

        for (int i=0;i<list.length;i++){
            bytes[index]= list[i];
            index++;
        }
        bytes[index]=0;
    }
}
