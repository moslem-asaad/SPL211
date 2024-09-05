package bgu.spl.net.impl.rci;

public interface ConnectionHandler<T> extends bgu.spl.net.srv.ConnectionHandler<T> {
    void send(T msg) ;
}
