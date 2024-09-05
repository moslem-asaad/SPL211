package bgu.spl.net.Messages;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class COURSEREGMessage extends Message {

    private short opcode;
    private int index;
    private boolean finishreading;
    private byte[]bytes;
    private short coursenumber;

    public COURSEREGMessage(){
        opcode=5;
        index=0;
        finishreading=false;
        bytes= new byte[1<<10];
    }

    public Object decodeNextByte(byte nextByte) {
        if (!finishreading){
            if (index<1){
                bytes[index]=nextByte;
                index++;
            }
            else {
                bytes[index]=nextByte;
                finishreading = true;
                coursenumber = bytesToShort(bytes);
                return this;
            }
        }
        return null;
    }



    @Override
    // this  function registers the student to the specified course ( if it can) and returns ack message,
    // else returns error message
    public synchronized ServerMessage act(Messaging_Protocol msgprotocol) {
        String username = msgprotocol.getUser();
        if (username==null) return new ErrorMessage(opcode);
        else if (!database.getCoursenum_name().containsKey(coursenumber)) return new ErrorMessage(opcode);
        else if(database.getCoursenum_numofregesters().get(coursenumber).get()==database.getCoursenum_maxNumOfSeat().get(coursenumber))
            return new ErrorMessage(opcode);
        else if (database.getLogin().get(username).equals(false)) return new ErrorMessage(opcode);
        else if (database.getAdminuser_pass().containsKey(username)|
                !database.getStudentuser_pass().containsKey(username)) return new ErrorMessage(opcode);
        else if (!database.getRegestedcourses().containsKey(username)) return new ErrorMessage(opcode);
        LinkedList<Short> kdamcourses = database.getCoursenum_kdamlist().get(coursenumber);
        LinkedList<Short> studentcourseslist = database.getRegestedcourses().get(username);
        for (int i = 0 ;i<kdamcourses.size();i++){
            if (!studentcourseslist.contains(kdamcourses.get(i))) return new ErrorMessage(opcode);
        }
        if (studentcourseslist.contains(coursenumber)) return new ErrorMessage(opcode);
        int max =database.getCoursenum_maxNumOfSeat().get(coursenumber);
        AtomicInteger num_of_regesters = database.getCoursenum_numofregesters().get(coursenumber);
        if (num_of_regesters.get()==max) return new ErrorMessage(opcode);
        studentcourseslist.add(coursenumber);
        database.getRegestedcourses().replace(username,studentcourseslist);
        AtomicInteger val=database.getCoursenum_numofregesters().get(coursenumber);
        database.getCoursenum_numofregesters().replace(coursenumber,new AtomicInteger(val.get()+1));
        ConcurrentLinkedQueue<String> studentslilst = database.getCoursenum_studentreg().get(coursenumber);
        studentslilst.add(username);
        database.getCoursenum_studentreg().putIfAbsent(coursenumber,studentslilst);
        return new AckMessage(opcode);
    }

    @Override
    public short getOpcode() {
        return opcode;
    }




}
