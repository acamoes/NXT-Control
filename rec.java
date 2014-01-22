/*Copyright (c) 2011 Aravind Rao

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation 
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, 
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT 
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


import lejos.nxt.*; //imports
import lejos.nxt.comm.*;
import lejos.robotics.navigation.*;
import java.io.*;

public class rec  {

  BTConnection btc;//a bluetooth connection
  DataInputStream dis;//data input and output sreams
  DataOutputStream dos;
  int command;
 DifferentialPilot pilot;// a class with methods used to control the motors.

public static void main(String [] args)  throws Exception 
{
rec rec = new rec();
rec.pilot = new DifferentialPilot(2.25f, 5.5f, Motor.A, Motor.C);//create a new pilot with Motors A and C
rec.pilot.setTravelSpeed(5);//set the travel speed to 5
rec.start();
}
        
public void start ()
{
  try
     {
	    LCD.drawString("waiting",0,0);
	    LCD.refresh();
	    btc = Bluetooth.waitForConnection();//wait until a connection is opened with the PC
	    LCD.clear();
	    LCD.drawString("connected",0,0);
	    LCD.refresh();  
	    dis = btc.openDataInputStream(); //create the data input and output streams
	    dos = btc.openDataOutputStream();
        while(true)//infinite loop to read in commands from the PC
        {
         try{
           command = dis.readInt();//read in command from PC
            if(command == -1){stop();}//if command is -1, then shutdown the NXT(stop() method)
            else if(command == 0){ //if command is 0, terminate connection and wait for a new one   
            dis.close();
	    dos.close();
	    LCD.clear();
	    btc.close();
            start();//start the process of reconnecting again
            }
                    
            else
            {
            LCD.clear();
            LCD.drawInt(command, 0,0);
            if(command == 37){pilot.setTravelSpeed(3);pilot.rotateRight();}/*If the command is for turning, set the speed to 3, else set it to 10
             *                                                               and according to the command(keycode) recieved, turn left or right or 
                                                                             move forward or backward 
                                                                           */
            
            else if(command == 38){pilot.setTravelSpeed(10);pilot.forward();}
            else if(command == 39){pilot.setTravelSpeed(3);pilot.rotateLeft();}
            else if(command == 40){pilot.setTravelSpeed(10);pilot.backward();}
            else{ //if command recieved is -2, stop the movement of the motors
                
                if(pilot.getTravelSpeed() == 3){pilot.setTravelSpeed(2);Thread.sleep(100);//fake way of showing deacceleration, incrementely decrease the speed by making the thread sleep in between each change
            pilot.setTravelSpeed(1);Thread.sleep(100);
            pilot.setTravelSpeed(0);}
                else{
                pilot.setTravelSpeed(9);Thread.sleep(100);
            pilot.setTravelSpeed(8);Thread.sleep(100);
            pilot.setTravelSpeed(7);Thread.sleep(100);
            pilot.setTravelSpeed(6);Thread.sleep(100);
            pilot.setTravelSpeed(5);Thread.sleep(100);
            pilot.setTravelSpeed(4);Thread.sleep(100);
            pilot.setTravelSpeed(3);Thread.sleep(100);
            pilot.setTravelSpeed(2);Thread.sleep(100);
            pilot.setTravelSpeed(1);Thread.sleep(100);
            pilot.setTravelSpeed(0);
            } 
            }
            }
        }
        catch(Exception E){}
        }
     }
    catch(Exception E){}
}
	
public void stop()//stop method to shutdown the NXT
{
   try
       {
	   dis.close();//close all connections
	   dos.close();
	   Thread.sleep(100); 
	   LCD.clear();
	   LCD.drawString("closing",0,0);
	   LCD.refresh();
	   btc.close();
	   LCD.clear();
       }
            
    catch(Exception e){}
}

}

