package bgu.spl.net.Messages;

import bgu.spl.net.srv.Database;

/**
 * this class represents the messages
 * with the method decodeNextByte
 * every client to server message extends this class
 */
public abstract class Message {

    protected final Database database = Database.getInstance();

    public abstract short getOpcode();

    public abstract Object decodeNextByte(byte nextByte);

    public abstract ServerMessage act(Messaging_Protocol msgprotocol);

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }



}
