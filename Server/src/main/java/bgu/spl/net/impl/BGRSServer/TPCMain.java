package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Messages.Message_EncoderDecoder;
import bgu.spl.net.Messages.Messaging_Protocol;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String args[]) {
        Database database = Database.getInstance();
        database.initialize("./Courses.txt");

        int portNum = Integer.parseInt(/*args[0]*/"7777");

        Server.threadPerClient(portNum, () -> new Messaging_Protocol<>(), Message_EncoderDecoder::new).serve();
    }
}
