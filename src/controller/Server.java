package controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		try {
			ServerSocket ss = new ServerSocket(3333);

			Socket s = ss.accept();

			DataInputStream dis = new DataInputStream(s.getInputStream());

			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String serverMsg, clientMsg;

			do {
				clientMsg = dis.readUTF();
				System.out.println("client says: " + clientMsg);
				serverMsg = br.readLine();
				dos.writeUTF(serverMsg);
				dos.flush();
			} while (!clientMsg.equals("end"));

			dis.close();
			s.close();
			ss.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
