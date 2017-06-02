package org.apache.hadoop.yarn.server.resourcemanager.scheduler.fifo;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import mpi.*;
import java.nio.*;
import java.util.*;

public class Mpj 
{
	public static void main(String args[]) throws Exception
	{
			MPI.Init(args);
			int me = MPI.COMM_WORLD.Rank();
			int size = MPI.COMM_WORLD.Size();
			int s=0;
			int[] s_buffer = new int[1];
			int r;
			float[] other = null;
			int m =0 ;
			int[] m_buffer = new int[1];
			float[] chunk_array = null;
			int step=1;
			Status status;
			float inp_data[] = null;
			//float inp[] = null;
		if (me ==0 )
		 {
		 	//generate inp arr from args
		 	int no_of_args = args.length-3;
		 	float inp_arr[] = new float[no_of_args];

		 	for(int k=0;k<no_of_args;k++)
		 	{
		 		inp_arr[k] = Float.parseFloat(args[3+k]);
		 	}
			//mpi send portions of pdata to slaves
			int no_of_items = inp_arr.length;
			s =no_of_items/size;
			r = no_of_items%size;
			
			// do padding and divide extra added among thr slaves
			if(r!=0)
			{
			  inp_data = Arrays.copyOf(inp_arr,inp_arr.length+size-r);
			  s++;
			}
			
			else
			{
			  inp_data =inp_arr;
			}

			chunk_array = new float[s];
			//scatter data
			s_buffer[0] = s;
			MPI.COMM_WORLD.Bcast(s_buffer,0,1,MPI.INT,0);
			//MPI.COMM_WORLD.Bcast(inp_data,0,inp_data.length,MPI.INT,0);
			MPI.COMM_WORLD.Scatter(inp_data,0,s,MPI.FLOAT,chunk_array,0,s,MPI.INT,0);
			qsort(chunk_array,0,s-1);
	    }

		else 
		{	//get data
			MPI.COMM_WORLD.Bcast(s_buffer,0,1,MPI.INT,0);
		//	MPI.COMM_WORLD.Bcast(inp_data,0,inp_data.length,MPI.INT,0);
			s=s_buffer[0];
			inp_data = new float[s*size];
			chunk_array = new float[s];
			MPI.COMM_WORLD.Scatter(inp_data,0,s,MPI.FLOAT,chunk_array,0,s,MPI.INT,0);
			qsort(chunk_array,0,s-1);
		}

		step=1;
		while(step<size)
		{
			if(me%(2*step)==0)
			{
				if(me+step<size)
				{
					status = MPI.COMM_WORLD.Recv(m_buffer,0,1,MPI.INT,me+step,0);
					//m = m_buffer.get();
					m=m_buffer[0];
					other= new float[m];
					//IntBuffer other_buffer = IntBuffer.allocate(m);
					status = MPI.COMM_WORLD.Recv(other,0,m,MPI.FLOAT,me+step,0);
					//showElapsed(id,"got merge data");
					//other = other_buffer.array();
					chunk_array = merge(chunk_array,s,other,m);
					//showElapsed(id,"merged data");
					s = s+m;
					s_buffer[0]=s;
				} 
			}
			else
			{
				int near = me-step;
				MPI.COMM_WORLD.Send(s_buffer,0,1,MPI.INT,near,0);
				//s=s_buffer.get();
				s=s_buffer[0];
				MPI.COMM_WORLD.Send(chunk_array,0,s,MPI.FLOAT,near,0);
				//showElapsed(id,"sent merge data");
				break;
			}
			step = step*2;
		}

		if(me==0)
		{
		  System.out.println(Arrays.toString(chunk_array));		
		}
			MPI.Finalize();

		}

private static float[] merge(float[] v1, int n1, float[] v2, int n2) {
	int i,j,k;
	float[] result = new float[n1+n2];
	i=0; j=0; k=0;
	while(i<n1 && j<n2)
		if(v1[i]<v2[j])
		{
			result[k] = v2[j];
			j++; k++;
		}
		else
		{
			result[k] = v1[i];
			i++; k++;
		}
	if(i==n1)
		while(j<n2)
		{
			result[k] = v2[j];
			j++; k++;
		}
	else
		while(i<n1)
		{
			result[k] = v1[i];
			i++; k++;
		}
	return result;
	}

	private static void qsort(float[] chunk_array, int left, int right) 
	{
	int i,last;
	if(left>=right)
		return;
	swap(chunk_array,left,(left+right)/2);
	last = left;
	for(i=left+1;i<=right;i++)
		if(chunk_array[i]>chunk_array[left])
			swap(chunk_array,++last,i);
	swap(chunk_array,left,last);
	qsort(chunk_array,left,last-1);
	qsort(chunk_array,last+1,right);
	}

	private static void swap(float[] chunk_array, int i, int j) 
	{
			float t;
			t = chunk_array[i];
			chunk_array[i] = chunk_array[j];
			chunk_array[j] = t;
		}
}
