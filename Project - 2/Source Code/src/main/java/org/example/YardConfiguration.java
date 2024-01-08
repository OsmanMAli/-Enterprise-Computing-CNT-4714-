/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 10, 2023
Class: <YardConfiguration.java>
Description: To practice programming an application
			 with multiple threads of execution and synchronizing
	 	     their access to shared objects.
*/
package org.example;


 //YardConfiguration represents the configuration of switches in a train yard.
 
public class YardConfiguration {
    private final int inboundTrack;
    private final int outboundTrack;
    private final int firstSwitch;
    private final int secondSwitch;
    private final int thirdSwitch;

    
     //Creates a new YardConfiguration.
     
    public YardConfiguration(int inboundTrack, int outboundTrack, int firstSwitch, int secondSwitch, int thirdSwitch) {
        this.inboundTrack = inboundTrack;
        this.outboundTrack = outboundTrack;
        this.firstSwitch = firstSwitch;
        this.secondSwitch = secondSwitch;
        this.thirdSwitch = thirdSwitch;
    }

    
      //Checks if this configuration matches the specified inbound and outbound tracks.
    
    public boolean matches(int inboundTrack, int outboundTrack) {
        return this.inboundTrack == inboundTrack && this.outboundTrack == outboundTrack;
    }

    
     //Gets the number of the first switch in this configuration.
        public int getFirstSwitch() {
        return firstSwitch;
    }

    
     //Gets the number of the second switch in this configuration.
    
    public int getSecondSwitch() {
        return secondSwitch;
    }

   
    //Gets the number of the third switch in this configuration.
     
    public int getThirdSwitch() {
        return thirdSwitch;
    }
}
