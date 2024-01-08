/*
Name: <Osman Ali>
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 10, 2023
Class: <Train.java>
Description: To practice programming an application
			 with multiple threads of execution and synchronizing
	 	     their access to shared objects.
*/
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

class Train implements Runnable {
    private final int trainNumber;
    private final Yard yard;
    private final List<ReentrantLock> requiredLocks;
    private final CountDownLatch allTrainsDispatched;

    
     // Constructing a Train object with specified parameters
     
    public Train(int trainNumber, int inboundTrack, int outboundTrack, Yard yard, CountDownLatch allTrainsDispatched) {
        this.trainNumber = trainNumber;
        this.yard = yard;
        this.requiredLocks = yard.getRequiredLocks(inboundTrack, outboundTrack);
        this.allTrainsDispatched = allTrainsDispatched;
    }
//Implements the logic for the train, acquiring 
  //and releasing locks on switches based on specific conditions.
    @Override
    public void run() {
        List<ReentrantLock> acquiredLocks = new ArrayList<>();
        List<Train> dispatchedTrains = new ArrayList<>();

        for (ReentrantLock lock : requiredLocks) {

            boolean lockAcquired;
            try {
            	if(dispatchedTrains.contains(this)){
                    break;
                    // Attempt to acquire the lock within a specific time frame

                }
                System.out.println();
                System.out.println("Train " + trainNumber + " attempting to acquire Lock on Switch " + yard.getSwitchNumber(lock));
                lockAcquired = lock.tryLock(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Handles acquiring and releasing locks
          // If all required locks are acquired, simulate the train movement, release the locks, and mark the train as dispatched

                if (acquiredLocks.isEmpty()) {
                    System.out.println("Train " + trainNumber + " Unable To lock FIRST required Switch is switch: " + yard.getSwitchNumber(lock) + ", Train will wait." + "\n");
                    acquiredLocks.add(lock);
                } else if (acquiredLocks.size() == 1) {
                    System.out.println("Train " + trainNumber + " UNABLE TO LOCK second required switch: Switch " + yard.getSwitchNumber(lock) + ".");
                    System.out.println("Train " + trainNumber + " Releasing lock on first required switch: Switch " + yard.getSwitchNumber(acquiredLocks.get(0)) + ". Train will wait..." + "\n");
                    releaseLocks(acquiredLocks);
                } else if (acquiredLocks.size() == 2) {
                    System.out.println("Train " + trainNumber + " UNABLE TO LOCK third required switch: Switch " + yard.getSwitchNumber(lock) + ".");
                    System.out.println("Train " + trainNumber + " Releasing locks on first and second required switches: Switch " + yard.getSwitchNumber(acquiredLocks.get(0)) + " and Switch " + yard.getSwitchNumber(acquiredLocks.get(1)) + ". Train will wait..." + "\n");
                    releaseLocks(acquiredLocks);
                }
                if (lockAcquired) {
                        acquiredLocks.add(lock);
                        System.out.println("\nTrain " + trainNumber + " HOLDS LOCK on Switch " + yard.getSwitchNumber(lock) + "\n");

                        if (acquiredLocks.size() == 3) {
                            simulateTrainMovement();
                            releaseLocks(acquiredLocks);
                            System.out.println("\nTrain " + trainNumber + " HOLDS ALL NEEDED SWITCH LOCKS - Train movement begins.");
                            System.out.println("\nTrain " + trainNumber + " Clear of yard control.");
                            System.out.println("Train " + trainNumber + " Unlocks/releases lock on Switch " + yard.getSwitchNumber(requiredLocks.get(0)));
                            System.out.println("Train " + trainNumber + " Unlocks/releases lock on Switch " + yard.getSwitchNumber(requiredLocks.get(1)));
                            System.out.println("Train " + trainNumber + " Unlocks/releases lock on Switch " + yard.getSwitchNumber(requiredLocks.get(2)));
                            System.out.println("Train " + trainNumber + " releases all switch locks");
                            System.out.println("Train " + trainNumber + " Has been dispatched and moves on down the line out of yard control into CTC.");
                            System.out.println("                                    @ @ @ TRAIN " + trainNumber + ": DISPATCHED @ @ @" + "\n");
                            dispatchedTrains.add(this);
                            allTrainsDispatched.countDown();
                        }
                }
        }

    }
		//releases acquired locks
    private void releaseLocks(List<ReentrantLock> locks) {
        System.out.println();
        for (ReentrantLock lock : locks) {
            if (lock.isHeldByCurrentThread()) {
                System.out.println("Train " + trainNumber + " Releases lock on Switch " + yard.getSwitchNumber(lock));
                lock.unlock();
            }
        }
    }
    	// Simulating train movement by sleeping for a random duration between 2000 and 4000 milliseconds.

    private void simulateTrainMovement() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4000));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
