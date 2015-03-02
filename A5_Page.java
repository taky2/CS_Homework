import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

/**
 *	This class manages downloading a webpage from a Website and extracting embedded links.
 *
 * @version	1.1  creation date: 11/4/2014
 *
 * @author	Aaron Gordon
 **/

public class A5_Page {
	private ArrayList<String> 	urls;	//holds all links from this page
	private String 				server;	//holds this page's complete url
	private String				dir;	//holds http:// through directory name and final /
	private String				site;	//the machine's name
	private URL 				url;	//holds connection information
	private Socket 				serv;	//Socket to server
	private BufferedReader 		in;		//socket's input stream
	private PrintWriter    		out;	//socket's output stream
	private Scanner 			scan;	//to read input stream
	private boolean				isConnected;	//indicates if steams set up to website
	private boolean 			debug = false;	//turns on error messages

	/**
	 @param completeURL The complete url of this webpage
	 **/
	public Page(String completeURL) throws Exception {
		if (completeURL.length() < 5 || !completeURL.substring(0,7).equals("http://")) {
			throw new Exception("\nThe url should start with: 'http://'  /n");
		}
		urls 		= new ArrayList<String>();
		this.server	= completeURL;
		int spot	= completeURL.lastIndexOf("/");
		if (spot == -1) {	//not found
			this.dir = null;
		} else {
			this.dir	= completeURL.substring(0,spot+1);
		}
		spot	= completeURL.indexOf('/',7);  //where does the machine name end?
		if (spot == -1) {	//not found
			this.site = null;
		} else {
			this.site	= completeURL.substring(0,spot);
		}
		isConnected	= this.connect();
	}

	/**
	 checkLine takes a String and finds any urls in the line (those following 'href="')
	 and retains them for later retrieval
	 @param line The String to parse
	 **/
	public void checkLine(String line) {
		String urlString = null;							//holds the url string
		Scanner linescan 	 = new Scanner(line);			//Scanner to parse line
		while (linescan.findInLine("href=\"") != null) {	//look for another href in this line
			urlString	= linescan.findInLine("[^\"]*");	//pull out url as a String
			if (urlString != null) {
				if (urlString.length() > 0 && urlString.charAt(0) == '#') {
					continue;
				} else if (urlString.length() > 0 &&
						urlString.substring(0,1).equals("/")) {		//go to root of site
					urlString = this.site + urlString;
				} else if (urlString.length() > 7 && !urlString.substring(0,7).equals("http://")) {
					String orig = urlString;
					urlString	= this.dir + urlString;
					if (debug) msg("Page:checkLine:  usr [" + orig + "] changed to ["+urlString+"]/n");
				} else if (urlString.length() > 11 && urlString.substring(0,11).equals("javascript:")) {
					continue;
				}
				urls.add(urlString);
			}
		}
	}

	/**
	 getLine() reads and returns one line from the webpage
	 <p>
	 The line is first checked for <b>embedded links.</b>
	 If any are found they are added to the list of links.
	 <p>
	 @return The line read
	 **/
	public String getLine() {
		String line = null;			//for return value
		try {
			if (scan != null && scan.hasNext()) {
				line = scan.nextLine();
				checkLine(line);
			}
		} catch (Exception err) {
			System.err.println("\n\nPage:getLine:OOPS - I/O Exception - trying to read from: "
					+ server + "\n" + err);
		}
		return line;
	}

	/**
	 @return The arrayList<String> of links from this page
	 **/
	public ArrayList<String> getLinks() {
		return urls;
	}

	/**
	 pageDone reports the status of reading this page.

	 @return true if the webpage has been read completely, false otherwise
	 **/
	public boolean pageDone() {
		return (scan == null) || !scan.hasNext();
	}

	/**
	 getName() provides the url for this Page
	 @return the url for this Page
	 **/
	public String getName() {
		return server;
	}

	/**
	 connect sets up the socket and stream to the webpage
	 @return boolean value to indicate success or failure
	 **/
	protected boolean connect() {
		if (debug) msg("Page:connect:  Establishing connection\n");
		if (server == null) {
			if (debug) msg("Page: connect:  - server is null\n");
			return false;
		}
		try {
			String [] addr =  new String[2];
			splitAddr(server,addr);
			if (addr[0] == null)	return false;				//don't have complete URL
			serv = new Socket(addr[0], 80);						//connect to port 80 (Web server)
			scan = new Scanner(serv.getInputStream());
			out = new PrintWriter(serv.getOutputStream(), true);	//true for autoflush
			String file = addr[1];
			file = "/" + file;				//need a '/' to separate machine from file name
			out.println("GET " + file + " HTTP/1.0\n\n");	//make request (\n\n is required!!)
			String line;
			do {		//skip header info
				line = this.getLine();
				if (debug) msg("Header >> " + line + "\n");
			} while (!this.pageDone() && line.length() >= 1 );
		} catch (IOException err) {
			System.err.println("\n\nPage:connect:OOPS - I/O Exception - trying to connect to: "
					+ server + "\n" + err);
			return false;
		}
		if (debug) msg("Page: connect -- returning true\n");
		return true;
	}


	/**
	 splitAddr splits the website url whole and puts the server and path componets in
	 the instance variables 'server' and 'path'
	 @param whole String containing the complete url
	 @param part  This String array, of size 2, is an out parameter containing the machine name
	 and path to the requested file.
	 **/
	public void splitAddr(String whole, String[] part){
		if (debug)	msg("SplitAddr:  whole is: [" + whole + "]");
		if (whole.substring(0,4).equals("http"))  {  //full url
			Scanner scan = new Scanner(whole);
			scan.useDelimiter(Pattern.compile("//"));
			String dummy = scan.next();
			String [] split = scan.next().split("/",2);
			part[0]	= split[0];
			if (split.length > 1) {
				part[1]	= split[1];
			} else {
				part[1] = " ";
			}
			if (debug)	msg("-->>-->>-->>==> " + part[0]);
			if (debug)	msg("-->>-->>-->>==> " + part[1]);
		} else {	//local url
			part[0]	= null;
			part[1]	= whole;
		}
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void msg(String words) {
		System.out.println(words);
	}

}
