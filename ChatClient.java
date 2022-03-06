import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Runnable
{

	private Socket link;
	private PrintWriter outputStream;
	private Scanner inputStream;
	private int port = 7777;
	private String nick;

	public ChatClient() throws IOException
	{
		initialize();
	}

	private void initialize() throws IOException
	{
		// get server address
		Scanner scan = new Scanner(System.in);
		System.out.println("What is the ip address of the chat server you are trying to connect to?");
		String str = scan.next();

		// get usernames
		System.out.println("What is your username?");
		nick = scan.next();

		// connects to server
		InetAddress host = null;
		try
		{
			host = InetAddress.getByName(str);
		}
		catch (UnknownHostException e1)
		{
			System.out.println("Host not found");
		}
		System.out.println("You are now connected to: " + host.getHostAddress());

		link = null;
		try
		{
			link = new Socket(host, port);
			link.setReuseAddress(true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("not found");
		}
		inputStream = new Scanner(link.getInputStream());
		outputStream = new PrintWriter(link.getOutputStream());

		Thread t = new Thread(this);
		t.start();

		//infinite loop waiting for user input
		while (scan.hasNextLine())
		{
			String msg = scan.nextLine();
			outputStream.println(nick + " says: " + msg);
			outputStream.flush();
		}
	}

	public static void main(String[] args) throws Exception
	{
		new ChatClient();
	}

	@Override
	public void run()
	{
		while (true)
		{
			if (inputStream.hasNextLine())
				System.out.println(inputStream.nextLine());
		}
	}
}