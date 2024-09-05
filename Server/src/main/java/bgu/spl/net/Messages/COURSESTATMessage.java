package bgu.spl.net.Messages;

public class COURSESTATMessage extends Message{

    private short opcode;
    private boolean finishreading;
    private byte[] bytes;
    private int index;
    private short coursenumber;

    public COURSESTATMessage(){
        opcode = 7;
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
        if (!database.getCourses().contains(coursenumber)) return new ErrorMessage(opcode);
        if (database.getStudentuser_pass().containsKey(msgprotocol.getUser())) return new ErrorMessage(opcode);
        return new ACKCOURSESTATMessage(opcode,coursenumber , msgprotocol.getUser());
    }
}
