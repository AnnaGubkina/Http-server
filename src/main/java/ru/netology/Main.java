package ru.netology;

public class Main {

    public static final int THREADS_COUNT = 64;
    public static final int PORT = 9999;

    public static void main(String[] args) {

        Server server = new Server(THREADS_COUNT);
        server.serverOn(PORT);

    }
}


