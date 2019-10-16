import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

enum FileType {
    SONG, ARTIST, ALBUM
}

/*This program will allow a user to input data that describes songs, one at a time, 
 * and at the end will create a .txt file that contains the information.
 */
public class MusicDataBaseReal {

    public static boolean writeOnExistingFile(ArrayList<String[]> list, String fileName, FileType type) {
        int i;

        try(FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer  = new PrintWriter(bw))
        {
            if (type == FileType.ALBUM) {
                for (i = 0; i < list.size(); i++) {
                    writer.println(list.get(i)[0] + " // Rated: " 
                        + list.get(i)[1] + "/10");

                    if (list.get(i)[2] == null) {
                        writer.println("");
                        continue;
                    }
                    else {
                        writer.println("Comments: " + list.get(i)[2]);
                        writer.println("");
                    }

                }
            }
            else {
                for (i = 0; i < list.size(); i++) {
                    writer.println(list.get(i)[0] + " - " + list.get(i)[1] + " // Rated: " 
                        + list.get(i)[2] + "/10");

                    if (list.get(i)[3] == null) {
                        writer.println("");
                        continue;
                    }
                    else {
                        writer.println("Comments: " + list.get(i)[3]);
                        writer.println("");
                    }

                }
            }

        } catch (IOException e) {
            System.out.println("Looks like there was an error printing to the new file");
            return false;
        }
        return true;
    }

    public static void songInfo(ArrayList<String[]> list, Scanner scnr) {
        String promptNameSong = "PLease enter a song.";
        String promptArtist = "Whose it by?";
        String promptRating = "Please rate from 1 to 10";
        String promptComments = "Whats comments do you have? (If none, type \"n\")";
        String promptAgain = "Would you like to add another?";


        while (true) {
            //Initializes a new array to hold song info
            String[] songInfo = new String[4];

            //Populates the array with user inputs.
            songInfo[0] = promptString(promptNameSong, scnr);
            songInfo[1] = promptString(promptArtist, scnr);
            songInfo[2] = promptRating(promptRating, scnr, 1, 10) + "";
            songInfo[3] = promptString(promptComments, scnr);

            if (promptChar(promptAgain, scnr) == 'n') {
                list.add(songInfo);
                break;
            }
            else {
                list.add(songInfo);
                continue;
            }
        }
    }

    public static void albumInfo(ArrayList<String[]> list, Scanner scnr) {
        String promptNameAlbum = "PLease enter an album.";
        String promptArtist = "Whose it by?";
        String promptRating = "Please rate from 1 to 10";
        String promptComments = "Whats comments do you have? (If none, type \"n\")";
        String promptAgain = "Would you like to add another?";

        while (true) {
            //Initializes a new array to hold song info
            String[] albumInfo = new String[4];

            //Populates the array with user inputs.
            albumInfo[0] = promptString(promptNameAlbum, scnr);
            albumInfo[1] = promptString(promptArtist, scnr);
            albumInfo[2] = promptRating(promptRating, scnr, 1, 10) + "";
            albumInfo[3] = promptString(promptComments, scnr);

            if (promptChar(promptAgain, scnr) == 'n') {
                list.add(albumInfo);
                break;
            }
            else {
                list.add(albumInfo);
                continue;
            }
        }
    }

    public static void artistInfo(ArrayList<String[]> list, Scanner scnr) {
        String promptNameArtist = "Please enter an artist.";
        String promptRating = "Please rate from 1 to 10";
        String promptComments = "Whats comments do you have? (If none, type \"n\")";
        String promptAgain = "Would you like to add another?";

        while (true) {
            //Initializes a new array to hold song info
            String[] artistInfo = new String[3];

            //Populates the array with user inputs.
            artistInfo[0] = promptString(promptNameArtist, scnr);
            artistInfo[1] = promptRating(promptRating, scnr, 1, 10) + "";
            artistInfo[2] = promptString(promptComments, scnr);

            if (promptChar(promptAgain, scnr) == 'n') {
                list.add(artistInfo);
                break;
            }
            else {
                list.add(artistInfo);
                continue;
            }
        }
    }

    public static boolean openDatabaseFile(ArrayList<String[]> list, Scanner scnr, FileType type) {
        Scanner fileScanner;
        FileInputStream fbs;
        String firstLine;
        String promptExistingFileName = "What file would you like to edit?";


        while (true) {
            String fileName = promptString(promptExistingFileName, scnr);
            File file = new File(fileName);

            try {
                if (!file.exists()) {
                    System.out.println("You're file does not exist.");
                    continue;
                }

                fbs = new FileInputStream(file);
                fileScanner = new Scanner(fbs);

                if (!fileScanner.hasNext()) {
                    System.out.println("The file you selected was empty. Please select a non-empty file.");
                    fileScanner.close();
                    continue;
                }
                else {
                    firstLine = fileScanner.nextLine();

                    if (firstLine.equals("#SONG")) {
                        type = FileType.SONG;
                        songInfo(list, scnr);
                    }
                    if (firstLine.equals("#ALBUM")) {
                        type = FileType.ALBUM;
                        albumInfo(list, scnr);
                    }
                    if (firstLine.equals("#ARTIST")) {       
                        type = FileType.ARTIST;
                        albumInfo(list, scnr);
                    }
                }
            } catch (IOException e) {
                System.out.println("There was an error accessing your chosen file.");
                return false;
            }
            if (writeOnExistingFile(list, fileName, type)) {
                fileScanner.close();
                return true;
            }
            else {
                fileScanner.close();
                return false;
            }
        }
    }

    public static void main (String[] args) {
        FileType type = null;
        Scanner scnr = new Scanner(System.in);
        ArrayList<String[]> list = new ArrayList<String[]>();
        String [] options = new String[] {"New Song Database", "New Album Database",
            "New Artist Database", "Alter An Existing File"};
        String choice;
        String promptFileName = "What would you like to name your database file?";
        String fileName;

        System.out.println("Thank you for using the Music Database!\n\n"
            + "This database will collect and store you're inputs into an\n"
            + "easy to read textfile.\nEnter \"done\" at any point to quit.");

        System.out.println("Please select an option:");

        for (int i = 0; i < 4; i++) {
            System.out.println(i + ") " + options[i]);
        }

        choice = scnr.nextLine().trim();

        switch (choice) {
            case "0" :

                type = FileType.SONG;

                songInfo(list, scnr);

                fileName = promptString(promptFileName, scnr);

                if (createDatabaseFile(list, fileName, type)) {
                    System.out.print("The file was created succesfully!");
                }
                else {
                    System.out.print("Sorry, looks like an error occured.");
                }
                break;

            case "1" :

                type = FileType.ALBUM;

                albumInfo(list, scnr);

                fileName = promptString(promptFileName, scnr);

                if (createDatabaseFile(list, fileName, type)) {
                    System.out.print("The file was created succesfully!");
                }
                else {
                    System.out.print("Sorry, looks like an error occured.");
                }
                break;
            case "2" :

                type = FileType.ARTIST;

                artistInfo(list, scnr);

                fileName = promptString(promptFileName, scnr);

                if (createDatabaseFile(list, fileName, type)) {
                    System.out.println("The file was created succesfully!");
                }
                else {
                    System.out.print("Sorry, looks like an error occured.");
                }
                break;
            case "3" :

                if (openDatabaseFile(list, scnr, type)) {
                    System.out.println("The file has been succesfully updated!");
                }
                else {
                    System.out.print("Sorry, looks like an error occured.");
                }
                break;
            case "done" :
                break;
        }
        System.out.print("Thank you for using The Music Database!");
    }

    public static String promptString(String prompt, Scanner scnr) {

        System.out.println(prompt);

        String text = scnr.nextLine().trim();

        if (text.equals("n")) {
            return null;
        }
        
        if (text.equals("Done") || text.equals("done")) {
            return "quit";
        }

        return text;

    }

    public static int promptRating(String prompt, Scanner scnr, int min, int max) {
        int rating;

        while (true) {
            System.out.println(prompt);

            if (scnr.hasNextInt()) {
                rating = scnr.nextInt();
                scnr.nextLine();
            }
            else {
                System.out.println("Please enter a number");
                scnr.nextLine();
                continue;
            }

            if (rating < min || rating > max) {
                System.out.println("Sorry, that number isn't in the given range.");
                continue;
            }
            else {
                return rating;
            }
        }
    }

    public static char promptChar (String prompt, Scanner scnr) {

        System.out.println(prompt);

        char choice = scnr.nextLine().charAt(0);

        return choice;    
    }

    public static boolean createDatabaseFile (ArrayList<String[]> list, String fileName, FileType type) {
        PrintWriter writer = null;
        int i;

        if (type == FileType.SONG || type == FileType.ALBUM) {
            try {
                writer = new PrintWriter(fileName + ".txt");

                if (type == FileType.SONG) {
                    writer.println("#SONG");
                }
                else {
                    writer.println("#ALBUM");
                }

                for (i = 0; i < list.size(); i++) {
                    writer.println(list.get(i)[0] + " - " + list.get(i)[1] + " // Rated: " 
                        + list.get(i)[2] + "/10");

                    if (list.get(i)[3] == null) {
                        writer.println("");
                        continue;
                    }
                    else {
                        writer.println("Comments: " + list.get(i)[3]);
                        writer.println("");
                    }

                }

                writer.close();

            } catch (IOException e) {
                System.out.println("Error creating database file.");
                return false;
            }
            return true;
        }
        else {
            try {
                writer = new PrintWriter(fileName + ".txt");

                writer.println("#ARTIST");

                for (i = 0; i < list.size(); i++) {
                    writer.println(list.get(i)[0] + " // Rated: " 
                        + list.get(i)[1] + "/10");

                    if (list.get(i)[2] == null) {
                        writer.println("");
                        continue;
                    }
                    else {
                        writer.println("Comments: " + list.get(i)[2]);
                        writer.println("");
                    }

                }

                writer.close();

            } catch (IOException e) {
                System.out.println("Error creating database file.");
                return false;
            }
            return true;
        }
    }
}
