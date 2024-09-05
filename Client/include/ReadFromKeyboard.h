#include <iostream>
#include <boost/asio.hpp>
#include "connectionHandler.h"
#include <thread>

using namespace std;

class ReadFromKeyboard{
private:
    ConnectionHandler &connectionHandler;
    bool terminate ;
public:
    ReadFromKeyboard(ConnectionHandler &_connectionHandler);
    void shouldterminate();
    void operator()();
    void ADMINREGMSG(string command);
    void STUDENTREGMSG(string command);
    void LOGINMSG(string command);
    void STUDENTSTATMSG(string command);
    void COURSEREGMSG(string command);
    void KDAMCHECKMSG(string command);
    void COURSESTATMSG(string command);
    void ISREGISTEREDMSG(string command);
    void UNREGISTERMSG(string command);
    void SHORTOBYTES(short op , char* bytearr);
};
