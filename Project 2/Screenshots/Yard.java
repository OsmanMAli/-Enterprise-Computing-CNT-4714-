/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 10, 2023
Class: <Yard.java>
Description: To practice programming an application
			 with multiple threads of execution and synchronizing
	 	     their access to shared objects.
*/
package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Yard {
    public static final int MAX_TRAINS = 30;//setting max trains
    private static final int MAX_SWITCH_ALIGNMENTS = 60;//setting max alignment
    // List to store switch locks, yard configurations, and switch numbers

    private final List<ReentrantLock> switchLocks = new ArrayList<>();
    private final List<YardConfiguration> yardConfigurations = new ArrayList<>();
    private final List<Integer> switchNumbers = new ArrayList<>();

    
     //Creates a new yard with switch configurations based on a provided yard file.
          
    public Yard(String yardFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(yardFile))) {
            String line;
            int switchNumber = 1;
            int switchAlignmentCount = 0;
            // Read yard configurations from the file and create locks and configurations

            while ((line = reader.readLine()) != null) {
                if (switchAlignmentCount >= MAX_SWITCH_ALIGNMENTS) {
                    break;
                }

                String[] parts = line.split(",");
                ReentrantLock switchLock = new ReentrantLock();
                switchLocks.add(switchLock);

                int inboundTrack = Integer.parseInt(parts[0]);
                int firstSwitch = Integer.parseInt(parts[1]);
                int secondSwitch = Integer.parseInt(parts[2]);
                int thirdSwitch = Integer.parseInt(parts[3]);
                int outboundTrack = Integer.parseInt(parts[4]);
                YardConfiguration configuration = new YardConfiguration(inboundTrack, outboundTrack, firstSwitch, secondSwitch, thirdSwitch);
                yardConfigurations.add(configuration);

                switchNumbers.add(switchNumber);
                switchNumber++;
                switchAlignmentCount++;
            }
            // Check if the number of switches exceeds the allowed limit
            if (switchLocks.size() > MAX_SWITCH_ALIGNMENTS) {
                System.out.println("The number of switches in the yard exceeds the allowed limit.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    
     //Get the required locks for a train moving from an inbound track to an outbound track.
     
    public List<ReentrantLock> getRequiredLocks(int inboundTrack, int outboundTrack) {
        List<ReentrantLock> requiredLocks = new ArrayList<>();

        for (YardConfiguration configuration : yardConfigurations) {
            if (configuration.matches(inboundTrack, outboundTrack)) {
                requiredLocks.add(switchLocks.get(configuration.getFirstSwitch() - 1));
                requiredLocks.add(switchLocks.get(configuration.getSecondSwitch() - 1));
                requiredLocks.add(switchLocks.get(configuration.getThirdSwitch() - 1));
                break;
            }
        }

        return requiredLocks;
    }
   //Get the switch number associated with a specific lock
    public int getSwitchNumber(ReentrantLock lock) {
        return switchNumbers.get(switchLocks.indexOf(lock));
    }

}