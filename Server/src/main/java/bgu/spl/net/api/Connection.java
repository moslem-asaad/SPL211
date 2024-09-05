package bgu.spl.net.api;


public interface Connection<T> {

        boolean send(int connectionId, T msg);

        void broadcast(T msg);

        void disconnect(int connectionId);

}
