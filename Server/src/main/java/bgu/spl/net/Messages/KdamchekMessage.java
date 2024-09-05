package bgu.spl.net.Messages;

public class KdamchekMessage extends Message {

    private Short opcode;
    private boolean finishreading;
    private byte[] bytes;
    private int index;
    private short coursenumber;

    public KdamchekMessage(){
        opcode=6;
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
    public ServerMessage act(Messaging_Protocol msgprotocol) {
        if (msgprotocol.getUser()==null) return new ErrorMessage(opcode);
        else if(!database.getCoursenum_kdamlist().containsKey(coursenumber)) return new ErrorMessage(opcode);
        return new ACKKDAMCHECKMessage(opcode,coursenumber);
    }


}
