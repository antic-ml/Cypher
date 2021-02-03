package cypher;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Run {
    private DataInputStream din;

    private static final String TITLE =
            "  ______ ____    ____ .______    __    __   _______ .______       __  \n" +
                    " /      |\\   \\  /   / |   _  \\  |  |  |  | |   ____||   _  \\     |  | \n" +
                    "|  ,----' \\   \\/   /  |  |_)  | |  |__|  | |  |__   |  |_)  |    |  | \n" +
                    "|  |       \\_    _/   |   ___/  |   __   | |   __|  |      /     |  | \n" +
                    "|  `----.    |  |     |  |      |  |  |  | |  |____ |  |\\  \\----.|__| \n" +
                    " \\______|    |__|     | _|      |__|  |__| |_______|| _| `._____|(__) \n" +
                    "                                                                      ";

    private static final String PLAIN_TEXT = "" +
            "NON NOBIS SOLUM SED TOTI MUNDO NATI";
    private static final int KEY = 7;

    public Run() {
        din = new DataInputStream(System.in);
    }

    public void printInstructions() {
        out(TITLE);
        out("\n  Written by Mario Gianota (gianotamario@gmail.com) October 2020\n\n");
        out("Can you decrypt the secret message to reveal what it says?\n\n");
        out("The message has been encrypted with a Caesar Shift.  A type of substitution\n");
        out("cipher in which each letter in the message is replaced by a letter some\n");
        out("fixed number of positions down the alphabet.  For example, with a left shift\n");
        out("of 3, D would be replaced by A, E would become B, and so on.\n");
        out("To decipher the message, substitute the letters. Be warned, the message is in Latin!\n\n");
        out("\nThis is an engrossing game, but if you want to ");
        out("quit,  type \"quit\".\n\n");

    }

    public void loop() {
        out("\nPress ENTER to continue.");

        while( play() ) {
            ;
        }
        out("\n\nOk.  Until next time.\n\n");
    }

    private boolean play() {
        boolean play = false;
        boolean win = false;
        int numMoves = 0;
        char c = 0;
        char rc  = 0;
        HashMap<Character,Character> replacementChars = new HashMap<Character, Character>();

        getString();

        out("\nLet's start, the encrypted message is:\n");
        String encryptedMessage = getEncryptedPlainText();
        out("\n\t" + encryptedMessage + "\n\n");

        while( ! win) {
            numMoves++;

            // Ask which letter to substitute in encrypted message
            out("Which letter in the encrypted message would you like to substitute [A-Z]? ");
            String s = getString().toUpperCase();

            if( s.length() == 0 )
                continue;
            if( "quit".equalsIgnoreCase(s)) {
                break;
            }

            c = s.charAt(0);
            if( ! contains(encryptedMessage, c)) {
                out("The encrypted message does not contain the letter '"+c+"'.\n");
                continue;
            }

            // Ask replace it with which character?
            boolean accepted = false;
            while( ! accepted ) {
                out("Replace '" + c + "' with which letter [A-Z]? ");
                s = getString().toUpperCase();

                if (s.length() == 0)
                    continue;

                rc = s.charAt(0);
                if (Character.isAlphabetic(c)) {
                    accepted = true;
                }
            }

            // Is the  replacement char in the plain text?
            if( ! contains(PLAIN_TEXT, rc)) {
                out("\n**** Clue: The letter '"+rc+"' is not in the plain text message. ****\n");
                continue;
            }

            replacementChars.put(c,rc);

            // Replace chars in message
            StringBuffer replacementText = new StringBuffer();
            for(int i=0; i<encryptedMessage.length(); i++) {
                char c2 = encryptedMessage.charAt(i);
                if( c2 == ' ' || c2 == ',' || c2 == '.' || c2 == '!' || c2 == '\n' || c2 == '\t') {
                    replacementText.append(c2);
                    continue;
                }
                Character rc2 = replacementChars.get(c2);
                if( rc2 != null ) {
                    replacementText.append(rc2);
                } else
                    replacementText.append('*');
            }

            // Do messages match?
            win = PLAIN_TEXT.equalsIgnoreCase(replacementText.toString());

            if( win ) {
                out("\n***** Success! *****\n");
                out("The message reads:\n");
                out("\t" + replacementText + "\n");
                out("Translation: Not for ourselves alone are we born, but for the whole world.\n");
                out("\n***** You did it in " + numMoves + " moves!!! *****");
                break;
            }

            // show decrypted message
            // show encrypted message
            out("\n\nDecrypted message text: " + replacementText + "\n");
            out("Encrypted message text: " + encryptedMessage + "\n\n");
        }

        /*
        out("\n\nAnother game [Y:N] ? ");
        String ans = getString();
        if( "y".equalsIgnoreCase(ans))
            play = true;
        */

        return play;
    }

    private String getEncryptedPlainText() {
        String cypher = getCypher();
        StringBuffer buf = new StringBuffer();

        for(int i=0; i<PLAIN_TEXT.length(); i++) {
            // Get char from plain text
            char c = PLAIN_TEXT.charAt(i);
            if( c == ' ' || c == ',' || c == '.' || c == '!' || c == '\n' || c == '\t') {
                buf.append(c);
                continue;
            }
            // index = char - 65
            int index = c - 65;
            // new char = buf.charAt(index)
            buf.append(cypher.charAt(index));
        }
        //buf.append("\nEncrypted: " + buf.toString());
        return buf.toString();
    }

    private String getCypher() {
        StringBuffer buf = new StringBuffer();
        for(char c='A'+ KEY; c <='Z'; c++) {
            // A = 65
            buf.append(c);
        }
        for(char c = 'A'; c < 'A' + KEY; c++) {
            buf.append(c);
        }
        //out("Cypher: " + buf.toString() + "\n");
        return buf.toString();
    }

    private boolean contains(String s, char c) {
        boolean contains = false;
        for(int i=0; i<s.length(); i++) {
            if( s.charAt(i) == c)
                contains = true;
        }
        return contains;
    }

    private void out(String s) {
        System.out.print(s);
    }

    private String getString() {
        try {
            return din.readLine();
        } catch(IOException ioe) {
            return "";
        }
    }
    public static void main(String[] args) {
        Run r = new Run();
        r.printInstructions();
        r.loop();
    }
}
