#include <iostream>
#include <thread>
#include "connectionHandler.h"
#include <mutex>

class ReadFromSocket{
public:
    ReadFromSocket(ConnectionHandler &_connectionHandler) ;
    void operator()();
    void shouldterminate();
    short bytesToShort(char* bytesArr);
private:
    ConnectionHandler &connectionHandler;
    bool terminate;
    void AckKdam();
    void AckIsRegested();
    void AckMyCourses();
    void AckStudentStat();
    void AckCourseStat();
};