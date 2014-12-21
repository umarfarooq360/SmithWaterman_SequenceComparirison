
# Sequence Comparison (Smith Waterman) Java Project


####Overview
This Java Program computes the optimum alignment of two gene sequences (ACTG) using the Smith-Waterman algorithm.  

Author: Omar Farooq    
Version: 11 Mar
 2014

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

####Weight Matrix Input Files

The weight matrix is four by four, and to allow for greater flexibility,  Each line of a weight matrix input file should represent a row in the matrix. Some of these values may be negative. Assume that the order of nucleotides corresponding to both the rows and columns of the matrix is A, C, T, G. The is, the entry in row 0, column 0 is s(A, A), the entry in row 0, column 1 is s(A, C), the entry in row 1, column 0 is s(C, A), etc. Lines consisting entirely of white space are ignored, as are lines beginning with the pound sign(#) which should be considered comments.

#### Gap Penalty Input Files
The format for these files is similar to the format for weight matrix input files, but with only a single line of data rather than four. The single line should represent the gap penalties associated with A,C,T, and G, respectively. 

#### Sequence Input Files
These should consist of multiple lines of characters from the alphabet Σ = {A, C, G, T}. Eachsequence begins with the string sequence:, followed by an integer, followed by another colon, followed by the nucleotide sequence itself. For example


sequence:1:ACCGTTCTGAGTCGATCX


Whitespace should be ignored. The uppercase X character marks the end of a sequence. Lines consisting entirely of white space are be ignored, as are lines beginning with the pound sign(#) which should be considered comments. The integer between the colons specifies the sequence number,
and this is what the user must specify in the configuraton file

Sample configurationa and input files are given in the examples directory.


####Sample Output

AC-G-T------TGCGCTAT----CG-T--A-GC-TA-GCTAACTG-ATC-GA-GCGCGC
ACTGATCGGGATTGCGCGATGGCGCGCTATATGCATATGCGATCTGAATCTTATATGCG-

GATAGCTAGCTA-GCGGC---A-T----CGA-A-TCGA-TAGC
GAGAGCTTA-TATGCGGCTATATTGGCGCGATATTCGAGGACT