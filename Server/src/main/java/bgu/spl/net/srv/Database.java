package bgu.spl.net.srv;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

	private ConcurrentHashMap<String,String> adminuser_pass ; // this map match to every admin user his password
	private ConcurrentHashMap<String,String> studentuser_pass ; // this map match to every admin user his password
	private ConcurrentHashMap<Short, LinkedList<Short>> coursenum_kdamlist; // this map match to every course (according the course number) his kdamlist
	private ConcurrentHashMap<Short,String> coursenum_name; // this map match to every course number its name
	private ConcurrentHashMap<Short, ConcurrentLinkedQueue<String>> coursenum_studentreg; // this map match for every course (according the course number) the students that register to it
	private ConcurrentHashMap<Short,Integer> coursenum_maxNumOfSeat; // this map match to every course (according his number) the max number of seats that could have
	private ConcurrentHashMap<Short, AtomicInteger> coursenum_numofregesters; // this map match to every curse (according the course number) the number of the students that registerd to it
	private ConcurrentHashMap<String,Boolean> login; // this map match to every user if he online or offline ( online = logged in , offline = did not looged in)
	private ConcurrentHashMap<String,LinkedList<Short>> regestedcourses; // this map match to every student the courses that he has registered to
	private LinkedList<Short> courses; // this list represents the list of the courses (according the course number)


	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
		adminuser_pass          = new ConcurrentHashMap<>();
		studentuser_pass        = new ConcurrentHashMap<>();
		coursenum_kdamlist      = new ConcurrentHashMap<>();
		coursenum_name          = new ConcurrentHashMap<>();
		coursenum_studentreg    = new ConcurrentHashMap<>();
		coursenum_maxNumOfSeat  = new ConcurrentHashMap<>();
		coursenum_numofregesters= new ConcurrentHashMap<>();
		login                   = new ConcurrentHashMap<>();
		regestedcourses         = new ConcurrentHashMap<>();
		courses                 = new LinkedList<>();

	}
	private static class DatabaseHolder{
		private static Database instance = new Database();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return DatabaseHolder.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) {
		// TODO: implement
		if (coursesFilePath.length()>0){
			try {
				courses(coursesFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * private function that initialize the database
	 * @param FilePath : the path of the file
	 * @throws FileNotFoundException : there is no file in the specified path
	 */
	private void courses(String FilePath) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(FilePath);
		Scanner sc = new Scanner(inputStream);
		while (sc.hasNextLine()){
			String line = sc.nextLine();
			String [] information = line.split("\\|");
			short coursenumber=Short.parseShort(information[0]);
			String coursename = information[1];
			int numberofmaxstudents = Integer.parseInt(information[3]);
			String kdam = information[2].substring(1,information[2].length()-1);
			LinkedList<Short> listkdam = new LinkedList<>();
			if (kdam.length()>0) {
				String[] kdamlist = kdam.split(",");
				for (int i = 0; i < kdamlist.length; i++) {
					listkdam.addLast(Short.parseShort(kdamlist[i]));
				}
			}
			coursenum_kdamlist.putIfAbsent(coursenumber,listkdam);
			coursenum_name.putIfAbsent(coursenumber,coursename);
			coursenum_maxNumOfSeat.putIfAbsent(coursenumber,numberofmaxstudents);
			coursenum_numofregesters.putIfAbsent(coursenumber,new AtomicInteger(0));
			coursenum_studentreg.putIfAbsent(coursenumber,new ConcurrentLinkedQueue<>());
			courses.addLast(coursenumber);
		}
	}

	public ConcurrentHashMap<String, String> getAdminuser_pass() {
		return adminuser_pass;
	}

	public ConcurrentHashMap<Short, String> getCoursenum_name() {
		return coursenum_name;
	}

	public ConcurrentHashMap<String, String> getStudentuser_pass() {
		return studentuser_pass;
	}

	public ConcurrentHashMap<Short, LinkedList<Short>> getCoursenum_kdamlist() {
		return coursenum_kdamlist;
	}

	public ConcurrentHashMap<Short, Integer> getCoursenum_maxNumOfSeat() {
		return coursenum_maxNumOfSeat;
	}

	public ConcurrentHashMap<Short, AtomicInteger> getCoursenum_numofregesters() {
		return coursenum_numofregesters;
	}

	public ConcurrentHashMap<Short, ConcurrentLinkedQueue<String>> getCoursenum_studentreg() {
		return coursenum_studentreg;
	}

	public ConcurrentHashMap<String, Boolean> getLogin() {
		return login;
	}

	public ConcurrentHashMap<String, LinkedList<Short>> getRegestedcourses() {
		return regestedcourses;
	}

	public LinkedList<Short> getCourses() {
		return courses;
	}

}
