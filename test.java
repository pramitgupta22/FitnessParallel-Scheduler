package test;
import mpi.*;
import java.util.*;
import java.io.*;

public class Scheduler
{
	public Scheduler(String args[])
	{
		String s =null;
        try {
            Process p = Runtime.getRuntime().exec("javac Mpj.java");
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            
            // read any errors from the attempted command
            if(stdError.readLine() != null)
            {
            	 System.out.println("Here is the standard error of the command (if any):\n");
                 while ((s = stdError.readLine()) != null) 
                {
                 System.out.println(s);
                }

            }
            
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}


	public void scheduleevent()
	{
		Random rand = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0;i<10;i++)
		{
			list.add(rand.nextInt());
		}

		 String res = null;

        try {
            
          Process p = Runtime.getRuntime().exec("mpjrun.sh -np 2 Mpj 10 20 30 40 50 60 70 80");
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((res = stdInput.readLine()) != null) {
                System.out.println(res);
            }
            
           if(stdError.readLine() != null)
            {
                 System.out.println("Here is the standard error of the command (if any):\n");
                 while ((res = stdError.readLine()) != null) 
                {
                 System.out.println(res);
                }

            }
            
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

	/*DRIVER*/
	public static void main(String args[])
	{
		Scheduler sc = new Scheduler(args);
		sc.scheduleevent();
 	}	
}



