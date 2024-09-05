package bgu.spl.net.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class STUDENTREGMessage extends Message {

    private short opcode;
    private String username;
    private String password;
    private byte[] studentreg;
    private boolean finishreadingtheusername;
    private boolean finishbooleanthepassword;
    private boolean finshreading;
    private int index;

    public STUDENTREGMessage(){
        opcode=2;
        username="";
        password="";
        studentreg=new byte[1<<10];
        finishreadingtheusername=false;
        finishbooleanthepassword=false;
        finshreading=false;
        index=0;
    }


    public Object decodeNextByte(byte nextByte) {
        if (!finshreading){
            if (!finishreadingtheusername){
                if (nextByte!='\0'){
                    studentreg[index]=nextByte;
                    index++;
                }
                else {
                    index++;
                    finishreadingtheusername = true;
                    username = new String(studentreg, 0, index--, StandardCharsets.UTF_8);
                }
            }
            else{
                if (!finishbooleanthepassword){
                    if (nextByte!='\0') {
                        studentreg[index] = nextByte;
                        index++;
                    }
                    else {
                        finishbooleanthepassword = true;
                        password = new String(studentreg, username.length()-1, index - 1, StandardCharsets.UTF_8);
                        finshreading=true;
                        return this;
                    }
                }
            }
        }
        return null;
    }


    @Override
    // if the student could register it self ,
    // this function register him and returns ack message , else returns error message
    public synchronized ServerMessage act(Messaging_Protocol msgprotocol) {
        if (username==null) return new ErrorMessage(opcode);
        if (database.getStudentuser_pass().containsKey(username) | database.getAdminuser_pass().containsKey(username)){
            return new ErrorMessage(opcode);
        }
        if (msgprotocol.getUser()!=null) {
            if (database.getLogin().get(msgprotocol.getUser()))
            return new ErrorMessage(opcode);
        }
        if (password!=null && !database.getStudentuser_pass().containsKey(username)) {
            database.getStudentuser_pass().put(username, password);
            database.getLogin().putIfAbsent(username, false);
            database.getRegestedcourses().putIfAbsent(username,new LinkedList<>());
            return new AckMessage(opcode);
        }
        return new ErrorMessage(opcode);
    }

    @Override
    public short getOpcode() {
        return opcode;
    }

    public String getUsername() {
        return username;
    }
}
