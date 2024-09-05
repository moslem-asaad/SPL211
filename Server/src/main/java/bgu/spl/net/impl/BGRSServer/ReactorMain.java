package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Messages.Message_EncoderDecoder;
import bgu.spl.net.Messages.Messaging_Protocol;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String args[]) {

        Database database = Database.getInstance();
        database.initialize("./Courses.txt");

        int portNum = Integer.parseInt(/*args[0]*/"7777");

        int threadNum = Integer.parseInt(/*args[1]*/"3");

        Server.reactor(threadNum,portNum, () -> new Messaging_Protocol<>(), () -> new Message_EncoderDecoder<>())
                .serve();

    }
}
