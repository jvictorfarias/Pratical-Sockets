package sockets.chat.chatcalc.backend.connection;

import sockets.chat.chatcalc.backend.model.Calculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Calculator c;
    private Socket clientSocket;
    private static ArrayList<DataOutputStream> comms = new ArrayList<>();

    public ServerConnection(Socket newClientSocket) {
        try {
            this.clientSocket = newClientSocket;
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.c.getInstance();
            this.comms.add(out);
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                String data = this.in.readUTF();
                if (data.startsWith("calc:")) {
                    System.out.println("Resultado da operação: " + this.c.getInstance().calc(data));
                }
                System.out.println(data);
                for (DataOutputStream dos : comms) {
                    if (!(dos.hashCode() == this.getOut().hashCode())) {
                        dos.writeUTF(data);
                    }
                }

            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO:" + e.getMessage());
            } catch (NullPointerException np) {
                try {
                    clientSocket.close();
                } catch (IOException ignored) {
                }
            }

        }

    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static ArrayList<DataOutputStream> getComms() {
        return comms;
    }

    public static void setComms(ArrayList<DataOutputStream> comms) {
        ServerConnection.comms = comms;
    }
}
