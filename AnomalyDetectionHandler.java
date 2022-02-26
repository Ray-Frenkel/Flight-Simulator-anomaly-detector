package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	public void Handler(InputStream inFromClient, OutputStream outToClient){
		SocketIO sockIO = new SocketIO(inFromClient,outToClient);
		CLI cli = new CLI(sockIO);
		cli.start();
		cli.dio.write("bye\n");
		sockIO.close();
	}


	public class SocketIO implements DefaultIO {

		BufferedReader in;
		PrintWriter out;
		public SocketIO(InputStream inputFileName,OutputStream outputFileName)  {
				in= new BufferedReader(new InputStreamReader(inputFileName));
				out= new PrintWriter(outputFileName,true);
		}

		@Override
		public String readText() {
			try {
				 return in.readLine();
			} catch (IOException e){
				e.printStackTrace();
				return null;
			}

		}

		@Override
		public void write(String text) {
			out.print(text);
			out.flush();
		}

		@Override
		public float readVal() {
			return 0;
		}

		@Override
		public void write(float val) {
			out.print(val);
			out.flush();
		}

		public void close() {
			try {
				in.close();
				out.close();
			} catch (IOException e){
				e.printStackTrace();
			}

		}

	}


}
