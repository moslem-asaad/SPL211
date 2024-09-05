package bgu.spl.net.Messages;

public class LOGOUTMessage extends Message {

    private short opcode;
    private String username;

    public LOGOUTMessage(){
        opcode=4;
        username="";
    }

    @Override
    // this function logged out the user (if it can) and retuens ack message,
    //  else returns error message
    public ServerMessage act(Messaging_Protocol msgprotocol) {
        username = msgprotocol.getUser();
        if (username==null) return new ErrorMessage(opcode);
        if (database.getAdminuser_pass().containsKey(username) || database.getStudentuser_pass().containsKey(username)) {
            if (database.getAdminuser_pass().containsKey(username)) {
                if (database.getLogin().get(username).equals(true)) {
                    database.getLogin().replace(username, true, false);
                    return new AckMessage(opcode);
                }
            }
            else{
                if (database.getLogin().get(username).equals(true)) {
                    database.getLogin().replace(username, true, false);
                    return new AckMessage(opcode);
                }
            }
        }
        return new ErrorMessage(opcode);
    }

    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public Object decodeNextByte(byte nextByte) {
        return this;
    }

}
