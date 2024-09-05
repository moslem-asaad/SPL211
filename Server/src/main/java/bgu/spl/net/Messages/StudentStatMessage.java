package bgu.spl.net.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class StudentStatMessage extends Message {

    private String username;
    private short opcode;
    private boolean finishreadingtheusername;
    private boolean finshreading;
    private byte[] bytes;
    private int index;

    public StudentStatMessage(){
        username="";
        opcode=8;
        finshreading=false;
        finishreadingtheusername=false;
        bytes=new byte[1<<10];
        index=0;
    }


    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public Object decodeNextByte(byte nextByte) {
        if (!finshreading) {
            if (!finishreadingtheusername) {
                if (nextByte != '\0') {
                    bytes[index] = nextByte;
                    index++;
                } else {
                    index++;
                    finishreadingtheusername = true;
                    username = new String(bytes, 0, index--, StandardCharsets.UTF_8);
                    finshreading = true;
                    return this;
                }
            }
        }
        return null;
    }
    @Override
    public ServerMessage act(Messaging_Protocol msgprotocol) {
        if (msgprotocol.getUser()==null)  return new ErrorMessage(opcode);
        else if (database.getStudentuser_pass().containsKey(msgprotocol.getUser()))
            return new ErrorMessage(opcode);
        else if (!database.getStudentuser_pass().containsKey(username))
            return new ErrorMessage(opcode);

        return new ACKSTUDENTSTATMessage(opcode,username);
    }


}
