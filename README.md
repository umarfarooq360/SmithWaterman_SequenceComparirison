
# Sequence Comparison (Smith Waterman) Java Project


####Overview
This Java Program computes the optimum alignment of two gene sequences (ACTG) using the Smith-Waterman algorithm.  

Author: Omar Farooq    
Version: 25 Jan 2014

####Compiling 
```
javac -cp lib/Jama-1.0.3.jar src/*.java -d bin/ 
```
####Running
```
java -cp . bin/SequenceComparison < name of configuration file >
```
 
####Configuration file Parameters
	- sequenceInputFile : Name of input sequence file with many ACTG gene sequences.

	- sequenceA : The number of the first sequence in the file

	- sequenceB : The number of the first sequence in the file

	- weightMatrixFile : A 4x4 matrix with weights of different alignments

	- gapPenaltyFile : A file with the gap penalty.

	- maxColumns : The maximum number of columns to display

	- writeToFile : Should the output be written to file

	- outputFile : The name of the output file

	- debugMode : Turns on debug mode

Sample configurationa and input files are given in the examples directory.