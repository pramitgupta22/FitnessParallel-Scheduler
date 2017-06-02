FitnessParallel-Scheduler

To tap the potential of multi-cored machines used in Hadoop Cluster for efficient scheduling  and develop an efficient and parallel scheduling algorithm in order to improve the performance and scalability of the MapReduce model.
 
In order to accomplish our objective, we have used MPJ API, which is an open-source Message Passing library for java. MPJ works as an MPI like model, it allows developers to write and compile parallel and distributed applications. We have chosen this library because of its java nature that is compatible with YARN
 
Tree Based Merge Implementation: 
If there are n elements to sort and p processes, then each slave sorts (n/p) values and master gathers the sorted subsequences. Master now has p sorted subsequences and it will have to call merge operation (p-1) times. Hence a time complexity of Θ ((p-1) (n1+n2)). But with the tree based merge, each process sends its sorted subsequence to its neighbor and a merge operation is performed at each step. This reduces the time complexity to Θ((log p )(n1+n2)).
