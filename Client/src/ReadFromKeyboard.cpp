#include "ReadFromKeyboard.h"

ReadFromKeyboard::ReadFromKeyboard(ConnectionHandler &_connectionHandler) :
connectionHandler(_connectionHandler), terminate(false){}

void ReadFromKeyboard::shouldterminate() {terminate=true;}


void ReadFromKeyboard::operator()() {

    int space;
    while (!(connectionHandler.isterminate())){
        const short size=1024;
        char nextSteps[size];
        std::cin.getline(nextSteps,size);
        string command(nextSteps);
        space=command.find_first_of(' ');

        if (command.substr(0,space)=="ADMINREG"){
            ADMINREGMSG(command);
        }
        else if(command.substr(0,space)=="STUDENTREG") {
            STUDENTREGMSG(command);
        }
        else if (command.substr(0,space)=="LOGIN") {
            LOGINMSG(command);
        }
        else if (command.substr(0,space)=="STUDENTSTAT") {
            STUDENTSTATMSG(command);
        }
        else if(command.substr(0,space)=="LOGOUT"){
            char tosend[2];
            SHORTOBYTES(4,tosend);
            connectionHandler.sendBytes(tosend,2);
            connectionHandler.terminate();
        }
        else if(command.substr(0,space)=="COURSEREG"){
            COURSEREGMSG(command);
        }
        else if(command.substr(0,space)=="KDAMCHECK"){
            KDAMCHECKMSG(command);
        }
        else if (command.substr(0,space)=="COURSESTAT"){
            COURSESTATMSG(command);
        }
        else if (command.substr(0,space)=="ISREGISTERED"){
            ISREGISTEREDMSG(command);
        }
        else if (command.substr(0,space)=="UNREGISTER"){
            UNREGISTERMSG(command);
        }
        else if (command.substr(0,space)=="MYCOURSES"){
            char tosend[2];
            SHORTOBYTES(11,tosend);
            connectionHandler.sendBytes(tosend,2);
        }
    }
}

void ReadFromKeyboard::SHORTOBYTES(short op, char *bytearr) {
    bytearr[0] = ((op >> 8) & 0xFF);
    bytearr[1] = (op & 0xFF);
}

void ReadFromKeyboard::ADMINREGMSG(string command) {
    connectionHandler.send(command,9,1);
}

void ReadFromKeyboard::STUDENTREGMSG(string command) {

    connectionHandler.send(command,11,2);
}

void ReadFromKeyboard::LOGINMSG(string command) {
    connectionHandler.send(command,6,3);
}
void ReadFromKeyboard::STUDENTSTATMSG(string command) {
    connectionHandler.sendstat(command,12,8);
}

void ReadFromKeyboard::COURSEREGMSG(string command) {
    int space = command.find_first_of(' ');
    string num = command.substr(space+1);
    stringstream makeit_short (num);
    short x = 0 ;
    makeit_short>>x;
    char msg[4];
    char msgop[2];
    char msgnum[2];
    SHORTOBYTES(5,msgop);
    SHORTOBYTES(x,msgnum);
    msg[0]=msgop[0];
    msg[1]=msgop[1];
    msg[2]=msgnum[0];
    msg[3]=msgnum[1];
    connectionHandler.sendBytes(msg,4);
}

void ReadFromKeyboard::KDAMCHECKMSG(string command) {
    int space = command.find_first_of(' ');
    string num = command.substr(space+1);
    stringstream makeit_short (num);
    short x = 0 ;
    makeit_short>>x;
    char msg[4];
    char msgop[2];
    char msgnum[2];
    SHORTOBYTES(6,msgop);
    SHORTOBYTES((short)x,msgnum);
    msg[0]=msgop[0];
    msg[1]=msgop[1];
    msg[2]=msgnum[0];
    msg[3]=msgnum[1];
    connectionHandler.sendBytes(msg,4);
}

void ReadFromKeyboard::COURSESTATMSG(string command) {
    int space = command.find_first_of(' ');
    string num = command.substr(space+1);
    stringstream makeit_short (num);
    short x = 0 ;
    makeit_short>>x;
    char msg[4];
    char msgop[2];
    char msgnum[2];
    SHORTOBYTES(7,msgop);
    SHORTOBYTES(x,msgnum);
    msg[0]=msgop[0];
    msg[1]=msgop[1];
    msg[2]=msgnum[0];
    msg[3]=msgnum[1];
    connectionHandler.sendBytes(msg,4);
}

void ReadFromKeyboard::ISREGISTEREDMSG(string command) {
    int space = command.find_first_of(' ');
    string num = command.substr(space+1);
    stringstream makeit_short (num);
    short x = 0 ;
    makeit_short>>x;
    char msg[4];
    char msgop[2];
    char msgnum[2];
    SHORTOBYTES(9,msgop);
    SHORTOBYTES(x,msgnum);
    msg[0]=msgop[0];
    msg[1]=msgop[1];
    msg[2]=msgnum[0];
    msg[3]=msgnum[1];
    connectionHandler.sendBytes(msg,4);
}

void ReadFromKeyboard::UNREGISTERMSG(string command) {
    int space = command.find_first_of(' ');
    string num = command.substr(space+1);
    char msg[4];
    stringstream makeit_short (num);
    short x = 0 ;
    makeit_short>>x;
    char msgop[2];
    char msgnum[2];
    SHORTOBYTES(10,msgop);
    SHORTOBYTES(x,msgnum);
    msg[0]=msgop[0];
    msg[1]=msgop[1];
    msg[2]=msgnum[0];
    msg[3]=msgnum[1];
    connectionHandler.sendBytes(msg,4);
}




