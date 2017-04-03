package edu.res.bytral;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class LaboSMTP {

    public static final String ADDR_FILE = "addessList.txt";
    public static final String CONTENT_FILE = "fakeMails.txt";
    public static final String MAIL_DELIM = "\r\n==========\r\n";
    public static String SMTP_SERVER = "localhost";
    public static int SMTP_PORT = 25000;

    /**
     * Effectue les parsing des fichiers et arguments
     *
     * @param args
     */
    public static void main(String[] args) {
        int nbOfGroups = Integer.parseInt(args[2]);
        SMTP_SERVER = args[0];
        SMTP_PORT = Integer.parseInt(args[1]);

        if (nbOfGroups < 1) {
            System.out.println("You must specify the number of groups that must be formed as argument.");
            System.exit(1);
        }

        // initialise les scanners
        Scanner addrScanner = null;
        Scanner contentScanner = null;
        try {
            addrScanner = new Scanner(new File(ADDR_FILE));
            contentScanner = new Scanner(new File(CONTENT_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // récupère les addresses mail
        LinkedList<String> addresses = new LinkedList<>();
        while (addrScanner.hasNextLine()) {
            addresses.add(addrScanner.nextLine());
        }
        addrScanner.close();

        // récupère les contenus des mails
        LinkedList<String> mails = new LinkedList<>();
        contentScanner.useDelimiter(MAIL_DELIM);
        try {
            String mail = contentScanner.next();
            while (true) {
                mails.add(mail);
                mail = contentScanner.next();
            }
        } catch (NoSuchElementException e) {
            // end of file
        }
        contentScanner.close();

        // envoie les mails
        try {
            sendMails(mails, addresses, nbOfGroups);
        } catch (IOException e) {
            System.exit(1);
            e.printStackTrace();
        }

        System.out.println("MAILS SENT !");
    }

    private static void sendMails(LinkedList<String> mails, LinkedList<String> addresses, int nbOfGroups) throws IOException {
        int minGroupSize = addresses.size() / nbOfGroups;
        Random rand = new Random();

        // pour chaque groupe...
        for(int group = 0; group < nbOfGroups; group++) {
            // récupère un mails aléatoirement
            String mail = mails.get(rand.nextInt(mails.size()));

            // envoie les mails
            for(int i = 1; group == nbOfGroups - 1 ?addresses.size() > group*minGroupSize + i : i < minGroupSize; i++) {
                sendMail(mail, addresses.get(group*minGroupSize), addresses.get(group*minGroupSize + i));
            }
        }
    }

    private static void sendMail(String mail, String from, String to) throws IOException {
        // connexion
        Socket socket = new Socket(SMTP_SERVER, SMTP_PORT);
        PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // hello
        inFromServer.readLine();
        outToServer.print("EHLO laboHEIG\r\n");
        outToServer.flush();
        // avance jusqu'à la fin des "250-..."
        while (!inFromServer.readLine().startsWith("250 ")) ;

        outToServer.print("MAIL FROM: fakemail@r39h29few34f.ch\r\n");
        outToServer.flush();
        inFromServer.readLine();

        outToServer.print("RCPT TO: " + to + "\r\n");
        outToServer.flush();
        inFromServer.readLine();

        outToServer.print("DATA\r\n");
        outToServer.flush();
        inFromServer.readLine();

        outToServer.print("From: " + from + "\r\n");
        outToServer.print("To: " + to + "\r\n");
        outToServer.print(mail);
        outToServer.print("\r\n.\r\n");
        outToServer.flush();

        inFromServer.close();
        outToServer.close();
        socket.close();
    }
}
