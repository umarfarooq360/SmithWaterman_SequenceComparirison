
# Sequence Comparison (Smith Waterman) Java Project


####Overview
This Java Program computes the optimum alignment of two gene sequences (ACTG) using the Smith-Waterman algorithm.  

Author: Omar Farooq    
Version: 25 Jan 2014

####Compiling 
```
javac -cp . *.java 
```
####Running
```
java -cp . SequenceComparison < name of configuration file >
```
 

#### Configuration file
	- sequenceInputFile = sequence2.input

	- sequenceA = 1

	- sequenceB = 2

	- weightMatrixFile = matrix.input

	- gapPenaltyFile = penalty.input

	- maxColumns = 60

	- writeToFile = false

	- outputFile = sequenceComparison.out

	- debugMode = false
