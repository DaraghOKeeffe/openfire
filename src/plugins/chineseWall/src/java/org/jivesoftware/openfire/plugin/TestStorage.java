package org.jivesoftware.openfire.plugin;

import java.util.Random;
public class TestStorage {
	//private persistentStorage db = new persistentStorage();
	
	public void start(Storage db){
		//Start timer
		double startTime = System.currentTimeMillis();
		
		//initialize values
		int users = 10000;
		int checks = 10000;
		int count = 1;
		
		//Run queries
		while(count <= checks){
			//generate random number
			Random rn = new Random();
			int randomNum = rn.nextInt((users) + 1);
			//System.out.println("Random number : "+randomNum);
			//Get Org Userx & user x+1
			String userx = db.getOrg("User"+randomNum);
			String usery = db.getOrg("User"+(randomNum+1));
			//check Conflict
			boolean conflict = db.checkConflict(userx,usery);
			count++;
			
			if (count == 100 || count == 200 || count == 500 || count == 1000 || count == 2000 || count == 5000 || count == 10000 ){
				//Stop Timer & log time
				double stopTime = System.currentTimeMillis();
				double finalTime = (stopTime - startTime) / 1000;
				System.out.println("Time for "+count+" checks : "+finalTime);
			}
		}
	}
}
