package bgu.spl.net.Messages;

public class MYCOURSESNessage extends Message {
    private short opcode;
//    private boolean finishreading;
//    private byte[] bytes;
//    private int index;
    private String user;

    public MYCOURSESNessage(){
        opcode = 11;
//        finishreading=false;
//        bytes = new byte[1<<10];
//        index=0;
        user="";
    }


    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public Object decodeNextByte(byte nextByte) {
            return this;
    }

    @Override
    public ServerMessage act(Messaging_Protocol msgprotocol) {
        user=msgprotocol.getUser();
        if (user==null) return new ErrorMessage(opcode);
        if (database.getAdminuser_pass().containsKey(user)) return new ErrorMessage(opcode);
        else if (!database.getLogin().containsKey(user)) return new ErrorMessage(opcode);
        else if (database.getLogin().get(user).equals(false)) return new ErrorMessage(opcode);
        else
        return new ACKMYCOURSESMessage(opcode,user);
    }
}
