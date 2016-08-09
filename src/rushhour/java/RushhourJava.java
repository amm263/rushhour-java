package rushhour.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RushhourJava {
    
    public static void main(String[] args) {
        // Options
        Boolean verbose = false;
        Boolean interactive = false;
        String file_path = "";
        // Variables
        Controller controller;
        Model model;
        Boolean solved;
        ArrayList<String> board  = new ArrayList<String>();
        int rows = 0;
        int cols = 0;
        
        
        for (String arg : args) {
            switch (arg) {
                case "-v":
                    verbose = true;
                    break;
                case "-i": 
                    interactive = true;
                    break;
                default:
                    file_path = arg;
                    break;
            }                     
        }
        
        if (interactive)
            file_path = "None - Interactive mode selected";
	
        if (verbose) {	
            System.out.format("Options:\n");
            System.out.format("\tVebose: "+verbose+"\n");
            System.out.format("\tInteractive: "+interactive+"\n");
            if (file_path.length() == 0) {
		System.out.format("\tFile path: No file selected\n");
            } else { 	
		System.out.format("\tFile path: "+file_path+"\n");
            }
        }
		
        if (interactive) {
            Scanner reader = new Scanner(System.in);
            String line = "";
            System.out.format("\nPlease draw your board:\n");
            System.out.format("-Use '.' for empty cells\n");
            System.out.format("-Alphabetic characters are reserved for cars\n");
            System.out.format("-'r' is reserved for the special red car\n");
            System.out.format("-Insert a blank line to finish\n\n");
            while(!(line = reader.nextLine()).isEmpty()) {
                if ((cols == 0) || (cols == line.length())) {
                    cols = line.length();
                    board.add(line);
                    rows++;
                } else {
                    System.out.format("Error: Wrong input in row "+rows+". Expecting "+cols+" columns, but received "+line.length()+"\n");
                }
            }    
        } else if (!file_path.isEmpty()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file_path));
                String line = reader.readLine();
                while (line!=null) {
                    if ((cols == 0) || (cols == line.length())) {
                        cols = line.length();
                        board.add(line);
                        rows++;
                        line = reader.readLine();
                    } else {
                        System.out.format("Error: Wrong input in row "+rows+". Expecting "+cols+" columns, but received "+line.length()+"\n");
                        System.exit(-1);
                    }
                }
                reader.close();
            } catch(IOException e) {
                System.out.format(e.getMessage());
                System.exit(-1);
            } 
        } else {	
            System.out.format("Usage: $rushour.py [-v] [-i] [file_path] \n\t-v: Verbose mode \n\t-i: Interactive mode (overrides file usage)	\n\tfile_path: Parse a text file containing a game board");
            System.exit(0);
        }
        
        if (verbose) {
            System.out.format("\nBoard acquired:\n");			
            for (int x = 0; x < rows; x++) {
                System.out.format(board.get(x)+"\n");
            }	
        }
        
        String[] originboard = new String[board.size()];
        originboard = (String[]) board.toArray(originboard);
        model = new Model(rows, cols, originboard);
        if (verbose)
            System.out.format("\nModel created\n");	
        
        controller = new Controller(model, verbose);
        if (verbose)
            System.out.format("\nPlaying...\n");	
        solved = controller.play();
        if (solved) {
            if (verbose || interactive) {
                for (int i=0; i<model.getSolutionSize(); i++) {
                    System.out.format("Step "+i+" - "+model.getSolution(i)+"\n");
                    for (String row : model.getSolutionFrames(i)) {
                        System.out.format(row+"\n");
                    }
                    
                }
                System.out.format("Won in "+model.getSolutionSize()+" steps!\n");
            }
        } else {
            System.out.format("No solution found");
        } 
        System.exit(0);                 
    }   
}
