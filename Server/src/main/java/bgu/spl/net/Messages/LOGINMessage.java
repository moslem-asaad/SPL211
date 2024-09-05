package bgu.spl.net.Messages;

import java.nio.charset.StandardCharsets;

public class LOGINMessage extends Message {

    private short opcode;
    private String username;
    private String password;
    private byte[] loginbytes;
    private int index;
    private boolean finshreading;
    private boolean finishreadingtheusername;
    private boolean finishbooleanthepassword;

    public LOGINMessage(){
        opcode=3;
        username="";
        password="";
        loginbytes = new byte[1<<10];
        index=0;
        finshreading=false;
        finishbooleanthepassword=false;
        finishreadingtheusername=false;
    }



    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public Object decodeNextByte(byte nextByte) {
        if (!finshreading){
            if (!finishreadingtheusername){
                if (nextByte!='\0'){
                    loginbytes[index]=nextByte;
                    index++;
                }
                else {
                    index++;
                    finishreadingtheusername = true;
                    username = new String(loginbytes, 0, index--, StandardCharsets.UTF_8);
                }
            }
            else{
                if (!finishbooleanthepassword){
                    if (nextByte!='\0') {
                        loginbytes[index] = nextByte;
                        index++;
                    }
                    else {
                        finishbooleanthepassword = true;
                        password = new String(loginbytes, username.length()-1, index - 1, StandardCharsets.UTF_8);
                        finshreading=true;
                        return this;
                    }
                }
            }

        }
        return null;
    }

    @Override
    public synchronized ServerMessage act(Messaging_Protocol msgprotocol) {
     //   if (msgprotocol.getUser()==null) return new ErrorMessage(opcode);
        if (!database.getLogin().containsKey(username)) return new ErrorMessage(opcode);
        if(database.getStudentuser_pass().containsKey(username)){
            if (!database.getStudentuser_pass().get(username).equals(password)) return new ErrorMessage(opcode);
        }
        if (database.getAdminuser_pass().containsKey(username)){
            if (!database.getAdminuser_pass().get(username).equals(password)) return new ErrorMessage(opcode);
        }
        if(database.getLogin().containsKey(username) && database.getLogin().get(username).equals(true))
            return new ErrorMessage(opcode);
        else {
            // if we here that mean we could login the user (username) ,
            // so , the main user for the client would be (this username)

            database.getLogin().replace(username, true);
            msgprotocol.setUser(username);
        }
        return new AckMessage(opcode);
    }

    public String getUsername() {
        return username;
    }

}
