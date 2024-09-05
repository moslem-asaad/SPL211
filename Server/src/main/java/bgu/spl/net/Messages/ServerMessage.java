package bgu.spl.net.Messages;

/**
 * this interface represents the messages that are server to clieant messages
 */
public interface ServerMessage {

    public abstract byte[] encode();
}
