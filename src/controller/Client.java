package controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {

		try {
			Socket s = new Socket("localhost", 3333);

			DataInputStream dis = new DataInputStream(s.getInputStream());

			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String serverMsg, clientMsg;

			do {
				clientMsg = br.readLine();
				dos.writeUTF(clientMsg);
				dos.flush();
				serverMsg = dis.readUTF();
				System.out.println("sdasda");
				System.out.println("server says: " + serverMsg);
			} while (!clientMsg.equals("end"));

			dis.close();
			dos.close();
			s.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
