import com.mongodb.client.*; 
import com.mongodb.MongoClient; 
import org.bson.Document; 
//import com.mongodb.client.MongoCollection; 

import java.util.concurrent.locks.Lock;

import java.util.concurrent.locks.ReentrantLock;


public class Dining
{


public static void main(String[] args) 
{

    Lock forks[] = new ReentrantLock[5];  
    for(int i = 0; i<5; i++)
    {
    	forks[i] = new ReentrantLock();
    }

    Thread p1 = new Thread(new Philosopher(forks[4], forks[0], "1st Philosopher"));

    Thread p2 = new Thread(new Philosopher(forks[0], forks[1], "2nd Philosopher"));

    Thread p3 = new Thread(new Philosopher(forks[1], forks[2], "3rd Philosopher"));

    Thread p4 = new Thread(new Philosopher(forks[2], forks[3], "4th Philosopher"));

    Thread p5 = new Thread(new Philosopher(forks[3], forks[4], "5th Philosopher"));

    p1.start();

    p2.start();

    p3.start();

    p4.start();

    p5.start();

}

}



class Philosopher implements Runnable
{

	Lock leftFork = new ReentrantLock();

	Lock rightFork = new ReentrantLock();

	String name;
	
	 MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	 MongoDatabase database = mongo.getDatabase("Raut1"); //create database in mongodb using "use Raut"
	 MongoCollection<Document> collection = database.getCollection("philo"); //db.createCollection("phil")


public Philosopher(Lock leftFork, Lock rightFork, String name) 
{

    this.leftFork = leftFork;

    this.rightFork = rightFork;

    this.name = name;

}



@Override

public void run() 
{

	try 
	{
		eat(leftFork, rightFork, name);
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
	}
}



private void eat(Lock leftFork, Lock rightFork, String name) throws Exception
{

    leftFork.lock();
    rightFork.lock();
  
    Document document = new Document(name, "Eating") ;
    collection.insertOne(document); 
    System.out.println("Document inserted successfully");   

        Thread.sleep(1000);

    
    Document document1 = new Document(name, "Done Eating and now thinking") ;
    collection.insertOne(document1);

    leftFork.unlock();

    rightFork.unlock();
}
}
