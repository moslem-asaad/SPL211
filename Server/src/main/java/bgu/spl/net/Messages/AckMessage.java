package bgu.spl.net.Messages;

public class AckMessage implements ServerMessage {
    private short otherop;
    private short opcode;
    private byte[] opmsg;
    private byte[] msgresult;

    public AckMessage(short op) {
        otherop = op;
        opcode=12;
        opmsg= new byte[2];
        msgresult = new byte[4];
    }

    @Override
    public byte[] encode() {
        opmsg=shortToBytes(opcode);
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
