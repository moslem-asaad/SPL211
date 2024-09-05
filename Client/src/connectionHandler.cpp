#include "connectionHandler.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(),
socket_(io_service_), terminated(false){}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some
			        (boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, '\0');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, '\0');
}


bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
	do{
		if(!getBytes(&ch, 1))
		{
			return false;
		}
		if(ch!='\0')
			frame.append(1, ch);
	}while (delimiter != ch);
    } catch (std::exception& e) {
	std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
	return false;
    }
    return true;
}


bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try {
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

bool ConnectionHandler::isterminate() {
    return terminated;
}
void ConnectionHandler::terminate() {
    terminated=true;
}
void ConnectionHandler::SHORTOBYTES(short op, char *bytearr) {
    bytearr[0] = ((op >> 8) & 0xFF);
    bytearr[1] = (op & 0xFF);
}
void ConnectionHandler:: send (string str , int num , short op){
    string _command= str.substr(num);
    int space = _command.find_first_of(' ');
    string username = _command.substr(0,space);
    string password = _command.substr(space+1);
    int msglength = 4 + username.size()+ password.size();
    char msg[msglength];
    SHORTOBYTES(op,msg);
    int index ;
    for (unsigned int i = 0; i < username.size(); ++i) {
        msg[i+2]= username.at(i);
        index = i+2;
    }
    index=index+1;
    msg[index]='\0';
    index++;
    for (unsigned int i = 0; i < password.size(); ++i) {
        msg[index]= password.at(i);
        index++;
    }
    msg[index]='\0';
    sendBytes(msg,msglength);
}

void ConnectionHandler::sendstat(std::string str, int num, short op) {
    string username= str.substr(num);
    int msglength = 3 + username.size();
    char msg[msglength];
    SHORTOBYTES(op,msg);
    int index ;
    for (unsigned int i = 0; i < username.size(); ++i) {
        msg[i+2]= username.at(i);
        index = i+2;
    }
    index=index+1;
    msg[index]='\0';
    sendBytes(msg,msglength);
}

