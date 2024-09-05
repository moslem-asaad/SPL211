package bgu.spl.net.Messages;

import bgu.spl.net.api.MessageEncoderDecoder;

public class Message_EncoderDecoder<T> implements MessageEncoderDecoder<T> {

    private short opcode; // the opcode of the message
    private byte []msgbytes;
    private int index;
    private Message msg;

    public Message_EncoderDecoder(){
        this.opcode=-1;
        msgbytes= new byte[1<<10];
        index=0;
        msg=null;
    }

    @Override
    public T decodeNextByte(byte nextByte) {
        msgbytes[index] = nextByte;
        index++;
        if (index==2) {
            opcode = bytesToShort(msgbytes);
            msg = msgtype();
            if (msg instanceof LOGOUTMessage || msg instanceof MYCOURSESNessage) {
                if (opcode == 4) {
                    index = 0;
                    return (T) new LOGOUTMessage();
                }
                else if (opcode == 11) {
                    index = 0;
                    return (T) new MYCOURSESNessage();
                }
                else
                    return null;
            }
        }
            else if (index>2){
                Object decode = msg.decodeNextByte(nextByte);
                if (decode!=null){
                    msgbytes = new byte[1<<10];
                    index = 0;
                    return (T) decode;
                }
            }
        return null;
    }

    @Override
    /**
     * this function encodes the message
     * according to its type
     * @param message : the message to be encoded
     * @return message if it is not null
     * @return null if the message is null
     */
    public byte[] encode(Object message) {
        if (message!=null){
            if (message instanceof AckMessage) {
                return ((AckMessage) message).encode();
            }

            else if (message instanceof ErrorMessage)
                return ((ErrorMessage) message).encode();

            else if (message instanceof ACKCOURSESTATMessage)
                return ((ACKCOURSESTATMessage) message).encode();

            else if (message instanceof ACKISREGESTERDMessage)
                return ((ACKISREGESTERDMessage) message).encode();

            else if (message instanceof ACKKDAMCHECKMessage)
                return ((ACKKDAMCHECKMessage) message).encode();

            else if (message instanceof ACKMYCOURSESMessage)
                return ((ACKMYCOURSESMessage) message).encode();

            else if (message instanceof ACKSTUDENTSTATMessage)
                return ((ACKSTUDENTSTATMessage) message).encode();
        }
        return null;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    /**
     * this function get the type of the message according to the opcode
     * @return the a message of the specific opcode if 0<opcode<12
     * @return null if there is no message type that has this opcode
     */
    private Message msgtype() {

        if (opcode == 1)
            return new ADMINREGMessages();
        else if (opcode == 2) {
            return new STUDENTREGMessage();
        } else if (opcode == 3)
            return new LOGINMessage();
        else if (opcode == 4)
            return new LOGOUTMessage();
        else if (opcode == 5)
            return new COURSEREGMessage();
        else if (opcode == 6) {
            return new KdamchekMessage();
        } else if (opcode == 7) {
            return new COURSESTATMessage();
        } else if (opcode == 8) {
            return new StudentStatMessage();
        } else if (opcode == 9) {
            return new ISREGISTEREDMessage();
        } else if (opcode == 10) {
            return new UNREGISTERMessage();
        } else if (opcode == 11) {
            return new MYCOURSESNessage();
        } else {
            return null;
        }

    }

}

