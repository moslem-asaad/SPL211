CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: BGRSclient

BGRSclient: bin/echoClient.o bin/connectionHandler.o bin/ReadFromSocket.o bin/ReadFromKeyboard.o
	@echo "Building target: BGRSclient"
	g++ -o bin/BGRSclient bin/echoClient.o bin/connectionHandler.o bin/ReadFromKeyboard.o bin/ReadFromSocket.o $(LDFLAGS)
	@echo "finished Linking"

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/ReadFromKeyboard.o: src/ReadFromKeyboard.cpp
	g++ $(CFLAGS) -o bin/ReadFromKeyboard.o src/ReadFromKeyboard.cpp

bin/ReadFromSocket.o: src/ReadFromSocket.cpp
	g++ $(CFLAGS) -o bin/ReadFromSocket.o src/ReadFromSocket.cpp

.PHONY: clean
clean:
	rm -f bin/*
