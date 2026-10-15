package giamberini.it;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int porta = 12345;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Server avviato. In ascolto sulla porta " + porta);

            // Il server continua ad aspettare nuovi client
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client collegato.");

                // Si occupa del client appena collegato
                gestisciClient(client);
            }

        } catch (IOException e) {
            System.out.println("Errore del server: " + e.getMessage());
        }
    }

    private static void gestisciClient(Socket client) {
        try (
            Socket socket = client;
            BufferedReader ingresso = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter uscita = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Il server riceve una riga, ad esempio: 10 + 5
            String richiesta = ingresso.readLine();
            String risposta = calcola(richiesta);

            // Invia il risultato o un messaggio di errore
            uscita.println(risposta);

        } catch (IOException e) {
            System.out.println("Errore durante la comunicazione: " + e.getMessage());
        }

        System.out.println("Connessione chiusa.");
    }

    private static String calcola(String richiesta) {
        if (richiesta == null) {
            return "Errore: richiesta vuota.";
        }

        // Divide la richiesta usando gli spazi
        String[] parti = richiesta.trim().split("\\s+");

        if (parti.length != 3) {
            return "Errore: inserisci l'operazione nel formato numero operatore numero.";
        }

        try {
            double primoNumero = Double.parseDouble(parti[0]);
            String operatore = parti[1];
            double secondoNumero = Double.parseDouble(parti[2]);
            double risultato;

            switch (operatore) {
                case "+":
                    risultato = primoNumero + secondoNumero;
                    break;
                case "-":
                    risultato = primoNumero - secondoNumero;
                    break;
                case "*":
                    risultato = primoNumero * secondoNumero;
                    break;
                case "/":
                    if (secondoNumero == 0) {
                        return "Errore: non è possibile dividere per zero.";
                    }
                    risultato = primoNumero / secondoNumero;
                    break;
                default:
                    return "Errore: operatore non riconosciuto.";
            }

            return "Risultato: " + risultato;

        } catch (NumberFormatException e) {
            return "Errore: inserisci due numeri validi.";
        }
    }
}
