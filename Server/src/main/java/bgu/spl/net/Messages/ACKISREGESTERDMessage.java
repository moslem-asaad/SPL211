package bgu.spl.net.Messages;

import bgu.spl.net.srv.Database;

import java.nio.charset.StandardCharsets;


public class ACKISREGESTERDMessage implements ServerMessage {

    private final Database database =Database.getInstance();
    private final String username;
    private final short opcode;
    private final short acop;
    private byte[] bytes;
    private String isregesterd;
    private short coursnum;

    public ACKISREGESTERDMessage (short op , String username , short coursnum){
        opcode=op;
        acop=12;
        this.username=username;
        bytes= new byte[1<<13];
        isregesterd="";
        this.coursnum=coursnum;
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
        return bytes;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private boolean isregesterd(){
        if (database.getRegestedcourses().get(username).contains(coursnum)){
            return true;
        }
        return false;
    }
    private void makethestringbytes(){
        if (isregesterd()) isregesterd="REGISTERED";
        else isregesterd="NOT REGISTERED";
        byte[] reg = isregesterd.getBytes(StandardCharsets.UTF_8);
        int index=4;
        for (int i=0;i<reg.length;i++){
            bytes[index]= reg[i];
            index++;
        }
        bytes[index]=0;
    }

}
