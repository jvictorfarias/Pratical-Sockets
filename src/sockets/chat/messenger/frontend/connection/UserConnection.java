package sockets.chat.messenger.frontend.connection;

import sockets.chat.messenger.frontend.controller.UserController;

import java.io.IOException;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class UserConnection extends Thread {
    private UserController user;

    public UserConnection() throws IOException {
        user = new UserController("Joao", "127.0.0.1", 3333);
        Scanner scan = new Scanner(System.in);
        this.start();
        while (scan.hasNextLine()) {
            user.getOut().writeUTF(scan.nextLine());
        }
    }

    public void run() {
        while (true) {
            try {
                String data = user.getIn().readUTF();
                System.out.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}