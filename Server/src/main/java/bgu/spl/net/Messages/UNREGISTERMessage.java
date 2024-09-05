package bgu.spl.net.Messages;

import java.util.concurrent.atomic.AtomicInteger;

public class UNREGISTERMessage extends Message {
    private short opcode;
    private boolean finishreading;
    private byte[] bytes;
    private int index;
    private short coursenumber;


    public UNREGISTERMessage(){
        opcode = 10;
        finishreading=false;
        bytes = new byte[1<<10];
        index=0;
    }

    @Override
    public short getOpcode() {
        return opcode;
    }


    @Override
    public Object decodeNextByte(byte nextByte) {
        if (!finishreading){
            if (index<1){
                bytes[index]=nextByte;
                index++;
            }
            else {
                bytes[index]=nextByte;
                finishreading = true;
                coursenumber = bytesToShort(bytes);
                return this;
            }
        }
        return null;
    }


    @Override
    // unregister the student to the specific courser with the number (coursenumber) if can and returns ack message,
    // else returns error message
    public synchronized ServerMessage act(Messaging_Protocol msgprotocol) {
        String username = msgprotocol.getUser();
        if (username==null) return new ErrorMessage(opcode);
        if (database.getRegestedcourses().containsKey(username)){
            if (database.getRegestedcourses().get(username).contains(coursenumber)) {
                database.getRegestedcourses().get(username).remove((Object) coursenumber);
                database.getCoursenum_studentreg().get(coursenumber).remove(username);
                AtomicInteger val=database.getCoursenum_numofregesters().get(coursenumber);
                database.getCoursenum_numofregesters().replace(coursenumber,new AtomicInteger(val.get()-1));
                return new AckMessage(opcode);
            }
        }
        return new ErrorMessage(opcode);
    }
}
