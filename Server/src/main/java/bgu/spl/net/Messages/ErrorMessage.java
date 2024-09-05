package bgu.spl.net.Messages;

public class ErrorMessage implements ServerMessage {

    private short otherop;
    private byte[] opmsg;
    private byte[] msgresult;

    public ErrorMessage(short opcode){
        otherop=opcode;
        opmsg= new byte[2];
        msgresult = new byte[4];
    }

    @Override
    public byte[] encode() {
        opmsg=shortToBytes((short)13);
        msgresult[0]=opmsg[0];
        msgresult[1]= opmsg[1];
        opmsg=shortToBytes(otherop);
        msgresult[2]=opmsg[0];
        msgresult[3]=opmsg[1];
        return msgresult;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
