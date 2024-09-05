package bgu.spl.net.Messages;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.Database;

public class Messaging_Protocol<T> implements MessagingProtocol<T> {

    private String user;
    private boolean terminate;
    private Database database = Database.getInstance();

    public Messaging_Protocol(){
        terminate=false;
        user=null;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public Object process(Object msg) {
        Message message = (Message) msg;
        short op =message.getOpcode();
        if (op==1){
             return adminreg((ADMINREGMessages) msg);
        }
        if (op==2){
            return studentreg((STUDENTREGMessage) msg);
        }
        if (op==3){
            return login((LOGINMessage) msg);
        }
        if (op==4){
            ServerMessage m = logout((LOGOUTMessage) msg);
            if (m instanceof AckMessage)
                terminate=true;
            return m;
        }
        if (op==5){
            return coureseg((COURSEREGMessage)msg);
        }
        if (op==6){
            return checkkdamlist((KdamchekMessage)msg);
        }
        if (op==7){
            return coursestat((COURSESTATMessage)msg);
        }
        if (op==8){
            return studentstat((StudentStatMessage) msg);
        }
        if (op==9){
            return isregester((ISREGISTEREDMessage) msg);
        }
        if (op==10){
            return unregester((UNREGISTERMessage)msg);
        }
        if (op==11){
            return mycourses((MYCOURSESNessage)msg);
        }
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }

    private ServerMessage adminreg(ADMINREGMessages msg){
       return msg.act(this);
    }

    private ServerMessage studentreg(STUDENTREGMessage msg){
       return msg.act(this);
    }

    private ServerMessage login (LOGINMessage msg){
       return msg.act(this);
    }

    private ServerMessage logout (LOGOUTMessage msg){
        return msg.act(this);
    }

    private ServerMessage coureseg (COURSEREGMessage msg){
        return msg.act(this);
    }

    private ServerMessage checkkdamlist(KdamchekMessage msg){
        return msg.act(this);
    }

    private ServerMessage coursestat(COURSESTATMessage msg){
        return msg.act(this);
    }

    private ServerMessage studentstat(StudentStatMessage msg){
        return msg.act(this);
    }

    private ServerMessage isregester(ISREGISTEREDMessage msg){
        return msg.act(this);
    }

    private ServerMessage unregester(UNREGISTERMessage msg){
        return msg.act(this);
    }

    private ServerMessage mycourses(MYCOURSESNessage msg){
        return msg.act(this);
    }
}
