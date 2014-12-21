import java.io.*;
import java.util.*;
import Jama.*;
/**
 * This class implements the the global Smith-Waterman/Needleman-Wunsch algorithm
 * to find the optimal alignment of two sequences. The main method takes in a parameter 
 * 
 * @author (Omar Farooq) 
 * @version (27th Feb 2014)
 */
public class SequenceComparison
{   
    //constants that define matrix sizes
    private final int WEIGHT_MATRIX_SIZE =4;  
    private final int PENALTY_MATRIX_SIZE= 4;

    Matrix weightMatrix;  // Matrix with the weights
    Matrix penaltyMatrix;  // Matrix with the gap penalties
    String sequenceA= "";   // The first sequence
    String sequenceB="";   // The second sequence

    //THESE ARE PROPERTIES SET BY THE CONFIG FILE  
    String sequenceInputFile;  //filename
    String sequenceANum; //index number of seq A
    String sequenceBNum;  //index number of seq B
    String weightMatrixFile;  //filename
    String gapPenaltyFile;   //filename
    int maxColumns= 80;   //Maximum Columns to print 
    boolean writeToFile;  //should you write to file?
    String outputFile;  //filename

    //Instance of the smithwaterman private subclass that implements the actual algorithm
    SmithWaterman MrSmith; //the waterman guy doesn't get credit!
    String alignmentA;  //the correct alignment of the first sequence
    String alignmentB;  //the correct alignment of the second sequence

    /**
     * Creates a instance of Sequence Comparison.
     */
    public SequenceComparison(String confFileName){
        readConfigFile(confFileName);   //read the config file
        this.initialize();  //read all the sub files
        MrSmith = new SmithWaterman();  // run the smithWaterman algorithm

        if(sequenceA.length()!= 0 && sequenceB.length()!=0)  
            MrSmith.fillScoringMatrix(sequenceA,sequenceB);  //error check, fill scoreing matrix
        else{
            System.err.println("Error: The sequences were null.");
            System.exit(1);
        }
        produceOutput();    //show output.
    }

    /**
     *Run the program with one argument which is the config file name.
     */
    public static void main(String[] args) {
        if(args.length!= 1) {
            System.out.println("ERROR:Please give a configuration filename");    
        }
        new SequenceComparison(args[0]);
    }

    /**
     *This method reads in the config file.
     *
     *@param filename This is the name of the configuration file
     */
    public void readConfigFile(String filename){
        try{
            Config newConf = new Config(filename); // reads the config file

            //Setting all the Properties
            sequenceInputFile = newConf.getProperty( "sequenceInputFile" );
            sequenceANum = newConf.getProperty( "sequenceA" );
            sequenceBNum = newConf.getProperty( "sequenceB" );
            weightMatrixFile =   newConf.getProperty( "weightMatrixFile");
            gapPenaltyFile = newConf.getProperty( "gapPenaltyFile");
            maxColumns = Integer.parseInt(newConf.getProperty( "maxColumns"));
            writeToFile = Boolean.valueOf(newConf.getProperty("writeToFile"));
            outputFile = newConf.getProperty( "outputFile");

        }
        catch(Exception e){  //error msg
            System.err.println("Error: File not found!" + e.getMessage());
        }

    }

    /**
     * Uses the information from configuration file to read the weight, gap penalty and
     * sequence files and initializes the required instance variables.
     */
    public void initialize(){
        try{

            //CREATING THE WEIGHT MATRIX FIRST
            //Maybe add code to exclude lines with # sign
            Scanner s1 = new Scanner(new File(weightMatrixFile)); //Scan the weight matrix file 
            double [][] arrayOfWeights = new double[WEIGHT_MATRIX_SIZE][WEIGHT_MATRIX_SIZE];
            while(s1.hasNextDouble() ){
                for (int i=0; i<WEIGHT_MATRIX_SIZE;i++ ) {
                    for (int j=0;j<WEIGHT_MATRIX_SIZE ;j++ ) {
                        arrayOfWeights[i][j]= s1.nextDouble();
                    }
                }    
            }
            weightMatrix = new Matrix(arrayOfWeights); //create matrix
            s1.close();

            //GET THE GAP PENALTY MATRIX INITIALIZED
            Scanner s2 = new Scanner(new File(gapPenaltyFile));
            double [][] arrayOfPenalties = new double[1][PENALTY_MATRIX_SIZE];
            while(s2.hasNextDouble()){  //scan through
                for (int i=0; i<PENALTY_MATRIX_SIZE; i++ ) {
                    arrayOfPenalties[0][i] = s2.nextDouble(); 
                }
            }
            penaltyMatrix = new Matrix(arrayOfPenalties); //create matrix
            s2.close();

            //READ THE SEQUENCE INPUT FILE
            Scanner s3 = new Scanner(new File( sequenceInputFile ));
            String aLine ;
            while(s3.hasNextLine()){  //scan the lines
                aLine = s3.nextLine();
                if(!aLine.startsWith("#") && aLine.contains("sequence") ){  //only read required lines
                    if(aLine.contains(sequenceANum)){
                        while(!aLine.contains("X") && s3.hasNextLine() ){ //incase sequence has multiple lines
                            aLine+= s3.nextLine();
                        }
                        sequenceA = aLine.substring(aLine.lastIndexOf(":")+1, aLine.indexOf("X")) ;
                    }
                    if(aLine.contains(sequenceBNum))   {  //for sequence b
                        while(!aLine.contains("X") ){  //incase sequence is on multiples lines
                            aLine+= s3.nextLine();
                        }
                        sequenceB = aLine.substring(aLine.lastIndexOf(":")+1, aLine.indexOf("X")) ; 
                    }
                }
                //System.out.println(aLine);

            }

        }
        catch(Exception e) {
            System.err.println("Error: Weight Matrix or Gap Penalty or Sequence file doesn't exist: "+ e.getMessage() );
        }

    }

    /*
     *This method produces output either by writing to file or by printing to std output.
     */
    public void produceOutput(){
        try{
            FileWriter f = new FileWriter(outputFile); //filewriter to write to file
            String printString = ""; //the string with the stuff to be printed
            int numLines = (alignmentA.length()/maxColumns) +1;  //how many lines of each sequence will be there

            for (int i = 0; i< numLines ;i++ ) {  //DONT TOUCH, IT WORKS
                if( i== numLines-1 ){  //for the last line
                    printString += alignmentA.substring(i*maxColumns, alignmentA.length()) + "\n" ;
                    printString += alignmentB.substring(i*maxColumns, alignmentB.length());
                }
                else{  // for all the other lines
                    printString += alignmentA.substring(i*maxColumns, (i+1)*maxColumns ) + "\n" ;
                    printString += alignmentB.substring(i*maxColumns,  (i+1)*maxColumns);
                    printString += "\n\n";  //Need some space

                }

            }
            if(writeToFile) {  //Just Print Em Or Write Em
                f.write(printString);  
                f.close();
            } 
            else{
                System.out.print((printString));
            }
        }
        catch( IOException e ){  //Error
            System.out.println("Error could not print results or write to file.");
        }

    }

    /*
     * This is a private inner class, that implements the Smith Waterman Algorithm.
     * No particular reason for making an inner class.I just wanted to lol! 
     */
    private class SmithWaterman
    {
        // 2D array containing alignment scores
        private double[][] alignmentScores;

        //Array containing directions to back track and find alignment,
        //In the array u is up, l is left and d is diagonal
        private char[][] backtrackDirections;        

        /**
         * Constructor for objects of class SmithWaterman
         */
        public SmithWaterman()
        {
            // initialise instance variables or not?
        }

        /*
         * Fills Scoring matrix for alignment scores for two sequences A and B
         * @param A This is the first sequence
         * @param B This is the second sequence 
         */
        public void fillScoringMatrix(String A, String B){
            int m = A.length()+1 ;  //length of the row
            int n = B.length() +1 ;  //length of the col
            //Added 1 to the length to account for gap column and row

            alignmentScores = new double[m][n];  
            alignmentScores[0][0] = 0;  //Can't have gap and gap

            backtrackDirections = new char[m][n];  //initialize arrays

            //the resulting alignments
            String resultA = "";
            String resultB = "";

            //SCORING FOR THE FIRST ROW AND THE FIRST COLUMN WITH GAP PENALTIES
            for (int i = 1; i< m ; i++ ) { 
                alignmentScores[i][0] = alignmentScores[i-1][0]+ penaltyMatrix.get(0,returnIndex(A.charAt(i-1))) ;   
                backtrackDirections[i][0] = 'u';  //first column will have up direction
            }
            //just like I said above!
            for (int j = 1; j< n ; j++ ) {
                alignmentScores[0][j] = alignmentScores[0][j-1] + penaltyMatrix.get(0,returnIndex(B.charAt(j-1))) ;            
                backtrackDirections[0][j] = 'l';  //first row will have left backtrack direction
            }

            //NOW FILLING THE REST OF THE SCORING MATRIX
            for (int j=1; j<n ; j++) {
                for (int i = 1; i <m ;i++ ) {
                    //Behold, the magic
                    //There are three possibilities
                    // They either align, or there is a gap in A or gap in B.
                    double dScore = (alignmentScores[i-1][j-1])+ weightMatrix.get(returnIndex(A.charAt(i-1)), returnIndex(B.charAt(j-1)));
                    double lScore = alignmentScores[i][j-1] + penaltyMatrix.get(0,returnIndex(B.charAt(j-1)));
                    double uScore = alignmentScores[i-1][j] + penaltyMatrix.get(0,returnIndex(A.charAt(i-1)));

                    //take the max of the 3
                    alignmentScores[i][j]=(double) Math.max((Math.max(dScore, lScore)), uScore);

                    //setting the back directs depending on which we picked
                    //we set it in direction of the maximum
                    if (uScore >= dScore  && uScore >= lScore) {
                        backtrackDirections[i][j] = 'u'; //u for up
                    }
                    else  if (dScore >= lScore  && dScore >= uScore) {
                        backtrackDirections[i][j] = 'd'; //d for diagonal
                    }
                    else if (lScore >= dScore  && lScore >=uScore) {
                        backtrackDirections[i][j] = 'l'; //l for left
                    }
                }
            }

            //DEBUG STUFF

            //             for (int i = 0; i <m ;i++ ) {
            //                 for (int j= 0; j<n ; j++) {
            //                     System.out.print( backtrackDirections[i][j] + " ");
            //                 }
            //                 System.out.println("");
            //             }

            //NOW WE FIGURE OUT THE ALIGNMENT
            int i = m-1;// Size of sequences
            int j = n-1;
            char direction;
            while( (i!=0 || j!=0)) { // Going backwards
                direction = backtrackDirections[i][j]; //get the direction at i,j

                if(direction == 'u'){  //if up 
                    resultA = A.charAt(i-1) + resultA;  //traverse A
                    resultB = "-"+ resultB;  //gap in B
                    i--; //decrement index
                }
                else if (direction == 'l'){  
                    resultA = "-" + resultA;  //gap in A
                    resultB = B.charAt(j-1)+ resultB; //traverse B
                    j--; //decrement index
                }
                else if(direction== 'd'){  
                    resultA = A.charAt(i-1) + resultA; //traverse both
                    resultB = B.charAt(j-1)+ resultB;
                    i--;  //decrement indexes
                    j--;

                }

            }

            //Sets the instance variables to the result obtained.
            alignmentA = resultA;
            alignmentB = resultB;

        }
        /*
         * This method return the index in order of nucleotides A,C,G,T from 0 to 3.
         * THE CORRECT ORDER HERE IS VERY IMPORTANT.
         * @param a the character representing nucleotides
         * @return The index of nucleotide.
         */
        public int returnIndex(char a) {
            //Random comment- Obvious stuff          
            if(a == 'A')  //0 to 3 for ACTG  
                return 0;
            else if(a == 'C')
                return 1;
            else if(a == 'G')
                return 2;
            else if(a == 'T')
                return 3;
            else
                return 0;
        }

    }
}
