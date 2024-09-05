#include "ReadFromSocket.h"
#include <iostream>
using namespace std;

ReadFromSocket::ReadFromSocket(ConnectionHandler &_connectionHandler ):
connectionHandler(_connectionHandler), terminate(false) {}
/**
 * we read the first 2 bytes to know the type of the message if it is ack message or error message 
 * then read the next 2 bytes to know the message type (the client to server messages)
 */
void ReadFromSocket::operator()() {
    while(!terminate){
        char opcode[2];
        if(!connectionHandler.getBytes(opcode,2)){
            cout<<"Disconnected. Exiting...\n"<<endl;
            break;
        }
        short op = bytesToShort(&opcode[0]);
        if(op==12){
            connectionHandler.getBytes(opcode,2);
            short msgop = bytesToShort(&opcode[0]);
            if (msgop==1){
                cout<<"ACK 1"<<endl;
            }
            if (msgop==2){
                cout<<"ACK 2"<<endl;
            }
            if(msgop==3){
                cout<<"ACK 3"<<endl;
            }
            if(msgop==4){
                cout<<"ACK 4"<<endl;
                connectionHandler.terminate();
                terminate=true;
            }
            if(msgop==5){
                cout<<"ACK 5"<<endl;
            }
            if (msgop==6){
                cout<<"ACK 6"<<endl;
                AckKdam();
            }
            if (msgop==7){
                cout<<"ACK 7"<<endl;
                AckCourseStat();
            }
            if(msgop==8){
                cout<<"ACK 8"<<endl;
                AckStudentStat();
            }
            if(msgop==9){
                cout<<"ACK 9"<<endl;
                AckIsRegested();
            }
            if(msgop==10){
                cout<<"ACK 10"<<endl;
            }
            if(msgop==11){
                cout<<"ACK 11"<< endl;
                AckMyCourses();
            }
        }
        else if(op==13){
            connectionHandler.getBytes(opcode, 2);
            short opcodeMessage = bytesToShort(&opcode[0]);
            if(opcodeMessage==4){
                connectionHandler.terminate();
                terminate=true;
            }
            cout <<"ERROR "<<opcodeMessage <<endl;
        }
    }

}

short ReadFromSocket::bytesToShort(char *bytesArr) {
       unsigned short result = (short)((bytesArr[0] & 0xff) << 8);
        result += (short)(bytesArr[1] & 0xff);
        return result;
}
void shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}
void ReadFromSocket::AckKdam() {
    char byte[1];
    connectionHandler.getBytes(&byte[0], 1);
    string list = "";
    while (byte[0]!=0){
        list=list+byte[0];
        connectionHandler.getBytes(&byte[0],1);
    }
    cout<< list << endl;
}
void ReadFromSocket::AckIsRegested() {
    char byte[1];
    connectionHandler.getBytes(&byte[0],1);
    string reg = "";
    while (byte[0]!=0){
        reg=reg+byte[0];
        connectionHandler.getBytes(&byte[0],1);
    }
    connectionHandler.getBytes(&byte[0],1);
    cout<< reg << endl;
}

void ReadFromSocket::AckMyCourses() {
    char byte[1];
    connectionHandler.getBytes(&byte[0], 1);
    string list = "";
    while (byte[0]!=0){
        list=list+byte[0];
        connectionHandler.getBytes(&byte[0],1);
    }
    cout<< list << endl;
}

void ReadFromSocket::AckStudentStat() {
    char byte[1];
    connectionHandler.getBytes(&byte[0], 1);
    string list = "";
    string studentusername = "";
    while(byte[0]!='\n'){
        studentusername=studentusername+byte[0];
        connectionHandler.getBytes(&byte[0], 1);
    }
    connectionHandler.getBytes(&byte[0], 1);
    while (byte[0]!='\0'){
        list=list+byte[0];
        connectionHandler.getBytes(&byte[0],1);
    }
    cout<<studentusername<<endl;
    cout<< list << endl;
}

void ReadFromSocket::AckCourseStat() {
    char byte[1];
    connectionHandler.getBytes(&byte[0], 1);
    string coursenum_name = "";
    string list = "";
    string seatsavailable = "";
    while(byte[0]!='\n'){
        coursenum_name = coursenum_name+byte[0];
        connectionHandler.getBytes(&byte[0], 1);
    }
    coursenum_name=coursenum_name+'\n';
    connectionHandler.getBytes(&byte[0], 1);
    while (byte[0]!='\n'){
        seatsavailable=seatsavailable+byte[0];
        connectionHandler.getBytes(&byte[0],1);
    }
    seatsavailable=seatsavailable+'\n';
    connectionHandler.getBytes(&byte[0], 1);
    while (byte[0]!=']'){
        list=list+byte[0];
        connectionHandler.getBytes(&byte[0],1);
    }
    list=list+byte[0];
    connectionHandler.getBytes(&byte[0],1);
    cout<<coursenum_name;
    cout<<seatsavailable;
    cout<< list<<endl;
}


