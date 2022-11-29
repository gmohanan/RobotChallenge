package com.test.robochallenge;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/* **********************************************************************
Toy robot moving on a square tabletop, of dimensions 5 units x 5 units.
The robot can receive one of three possible types of commands:
PLACE 1,2,EAST
MOVE
LEFT
RIGHT
REPORT

Input from file is read
Example

PLACE 1,2,EAST
MOVE
MOVE
LEFT
MOVE
REPORT
PLACE 0,0,NORTH
MOVE
REPORT

*Tech notes*
HashMap - robots {string , [set of commands]}
{"ROBOT 1"=[sets of commands]}

Commands intepreted as follows
Starting X postion for Robot= cmds[0]
Starting Y postion for Robot= cmds[1]
Start Direction for Robot=cmds[2]

Command interpretation
-2: turn left 90 degrees
-1: turn right 90 degrees
 1: move forward 1 units
 0: report - print the x, y and direction
 
Depending upon the matrix unit Grid squares outside the matrix are set as obstacles. whatever comes outside 5*5 will be obstacles 
If the robot try to move to that grid, move will not happen will remain in previous position and continues other instructions.
 
Example 1:
Input: commands = 
PLACE 0,0,NORTH
MOVE
REPORT
[0, 0, 0, 1, 0]
Output: Output:  x = 0, y= 1, direction=Optional[NORTH]
Explanation: robot will go to (3, 4)
Example 2:
Input: commands =
PLACE 1,2,EAST
MOVE
MOVE
LEFT
MOVE
REPORT  
[1, 2, 1, 1, 1, -2, 1, 0]
Output:  x = 3, y= 3, direction=Optional[NORTH]

********************************************************************

Testing
	1) Create a file input.txt in the directory C:\data\test\input.txt
	2) Give the commands in the input file 
	
Example content of input.txt below:-
PLACE 1,2,EAST
MOVE
MOVE
LEFT
MOVE
REPORT
PLACE 0,0,NORTH
MOVE
REPORT
PLACE 0,0,NORTH
LEFT
REPORT

*/

enum DIRECTION {
	 // direction 0:NORTH, 1:EAST, 2:SOUTH, 3:WEST
	NORTH(0), EAST(1), SOUTH(2), WEST(3) ;
    private final int value;

    DIRECTION(int value) {
        this.value = value;
    }

    public static Optional<DIRECTION> valueOf(int value) {
        return Arrays.stream(values())
            .filter(direction -> direction.value == value)
            .findFirst();
    }
    public int getValue() { return value; }
}

class RoboSolution {
	
	/*
	 * Start each robot with commands[], tablematrix 5*5 
	 *
	 * */
    public static void starRobot(int[] cmds, int tableMatrixUnit) {

	 Set<String> obstracleSet = new HashSet<>();
	 obstracleSet =createSquareTableObstacle(tableMatrixUnit);
	         
 	int x=cmds[0];
 	int y=cmds[1];
 	int direction=cmds[2];
   
    int[][] moves = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
        
        for(int i = 3; i < (cmds.length); i++){
        	//turn right 
            if(cmds[i] == -1){
                direction++;
                if(direction == 4)  direction = 0;
                //System.out.println("Changing direction -turn right :"+direction);
            }
           //turn left
            if(cmds[i] == -2){
                direction--;
                if(direction == -1) direction = 3;
                //System.out.println("Changing direction -turn left :"+direction);
            }
            if(cmds[i] == 1){
                int c = cmds[i];
                while(c > 0 && !obstracleSet.contains((x+moves[direction][0]) + " " + (y + moves[direction][1]))){
                	//System.out.println(" x move= "+moves[direction][0]+", y move= "+moves[direction][1]);
                    x = x + moves[direction][0];
                    y = y + moves[direction][1];
                    c--;
                }
            }
            if(cmds[i] == 0){
            	System.out.println("Output: "+x+", "+y+", "+DIRECTION.valueOf(direction).toString());
            }
        }
    }
    
    private static Set<String> createSquareTableObstacle(int unit) {
    	Set<String> set = new HashSet<>();
    	 
    	int index=0;
    	for(int i = 0; i < unit; i++){
            set.add(unit + " " + i);
            set.add(i + " " + unit);
            set.add(i + " " + (index-1));
            set.add((index-1) + " " + i);
        }
		return set;
	}

	public static void main(String[] args) throws IOException {
		try {

			String line = null;
			int startXPos =0;
			int startYPos =0;
			List<Integer> cmds = new ArrayList<Integer>();
			Map<String, List<Integer>> robots = new HashMap<>();
			// Creates a FileReader
			FileReader file = new FileReader("C:\\data\\test\\input.txt");
			// Creates a BufferedReader
			BufferedReader input = new BufferedReader(file);
			//String existingRobo =null;
			// Reads lines
			int i=0;
			
			while ((line = input.readLine()) != null) {
				System.out.println(line);
				/*if(line.contains("ROBOT")) {
					existingRobo = line.replace("ROBOT", "").trim();
				}*/
				 if(line.contains("PLACE")) {
					 String[] splitted = null;
					
					 if(i!=0) {
						 cmds = new ArrayList<Integer>();
					 }
					i++;
					String newComm = line.replace("PLACE", "");
					splitted = newComm.trim().split("\\s*,\\s*");
					if(splitted.length > 3) {
						System.out.println("Invalid PLACE Command. Fix the command");
					}
					else {

						startXPos = Integer.parseInt(splitted[0]); 
						startYPos = Integer.parseInt(splitted[1]);
						cmds.add(startXPos);
						cmds.add(startYPos);
						cmds.add(DIRECTION.valueOf(splitted[2]).getValue());
					}
				}
				if(line.contains("MOVE")) {
					cmds.add(1);
				}
				else if(line.contains("LEFT")) {
					cmds.add(-2);
				}
				else if(line.contains("RIGHT")) {
					cmds.add(-1);
				}else if(line.contains("REPORT")) {
					cmds.add(0);
				}
				robots.put("ROBOT "+i, cmds);
			}
			// Closes the reader
			input.close();
			robots.forEach((robot, roboComands) -> {
	            System.out.println("********Staring Robot : " + robot + "\t*** roboComands : " + roboComands);
	            starRobot(roboComands.stream().mapToInt(p->p).toArray(),5);
	        });
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception " + e);
		}
	}

}