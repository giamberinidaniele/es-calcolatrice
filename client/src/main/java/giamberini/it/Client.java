package giamberini.it;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String indirizzoServer = "localhost";
        int porta = 12345;

        try (
            Scanner tastiera = new Scanner(System.in);
            Socket socket = new Socket(indirizzoServer, porta);
            PrintWriter uscita = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader ingresso = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.print("Inserisci il primo numero: ");
            String primoNumero = tastiera.nextLine();

            System.out.print("Inserisci l'operatore (+, -, *, /): ");
            String operatore = tastiera.nextLine();

            System.out.print("Inserisci il secondo numero: ");
            String secondoNumero = tastiera.nextLine();

            // Invia una sola riga al server, per esempio: 10 + 5
            uscita.println(primoNumero + " " + operatore + " " + secondoNumero);

            // Attende e stampa la risposta del server
            String risposta = ingresso.readLine();
            System.out.println("Risposta del server: " + risposta);

        } catch (Exception e) {
            System.out.println("Impossibile collegarsi al server: " + e.getMessage());
        }
    }
}
