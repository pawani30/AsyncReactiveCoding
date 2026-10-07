package com.codingshuttle.LearnAsysncSchedulingApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongRunningTask implements Runnable{

//To pass input to this LongRunningTask inside the LongRunningTask object
    private  String cmd;

    public LongRunningTask(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void run() {
        log.info("{} Long running tasks...Starting tasks : {}",cmd,Thread.currentThread().getName());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("{} Ending task : {}",cmd,Thread.currentThread().getName());
//submit() method can return something EX : you were calling some API and once the API has return its response ypu can return this response from here . HOW THIS RESPONSE WILL COME OUT TO THE main Thread() is with the help of Future
    }
    }


// 391+05:30  INFO 23832 --- [LearnAsysncSchedulingApplication] [           main] c.c.L.LearnAsyncSchedulingApplication    : Starting main thread : main
//2026-09-24T19:06:06.392+05:30  INFO 23832 --- [LearnAsysncSchedulingApplication] [           main] c.c.L.LearnAsyncSchedulingApplication    : ENDING MAIN THREAD :main
//2026-09-24T19:06:06.392+05:30  INFO 23832 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : Pawani Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T19:06:10.407+05:30  INFO 23832 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : Pawani Ending task : pool-2-thread-1

//These two lines can interfare  : ENDING MAIN THREAD :main
//  Pawani Long running tasks...Starting tasks : pool-2-thread-1
//They can interfare in the order as well , because we are starting the main thread and then we are calling the method submit in which long running tasks is present ,it might happen that this run method starts calling inside another thread directly or gets the cpu and starts executing inside another thread instantly and the main thread and this thread is executing in parallel inside different CPU cores
//And then after that this line iss executed ENDING main thread is excuted by the main thread and after that after 4sec the ending task:pool-2-thread-1 will be executed




//	for(int i = 0;i<=100;i++) {
//			threadPoolExecutor.submit(new LongRunningTask(i+""));
//		}

//After all the 6 threads are assigned from the thread pool our application is crashing

//-24T19:14:15.818+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [           main] c.c.L.LearnAsyncSchedulingApplication    : Starting main thread : main
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T19:14:15.820+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 14 Long running tasks...Starting tasks : pool-2-thread-5
//2026-09-24T19:14:15.820+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 15 Long running tasks...Starting tasks : pool-2-thread-6
//Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@5e63cad[Not completed, task = java.util.concurrent.Executors$RunnableAdapter@55b62629[Wrapped task = com.codingshuttle.LearnAsysncSchedulingApplication.LongRunningTask@a53bb6f]] rejected from java.util.concurrent.ThreadPoolExecutor@7f4d9395[Running, pool size = 6, active threads = 6, queued tasks = 10, completed tasks = 0]
//	at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2081)
//	at java.base/java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:841)
//	at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1376)
//	at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:123)
//	at com.codingshuttle.LearnAsysncSchedulingApplication.LearnAsyncSchedulingApplication.main(LearnAsyncSchedulingApplication.java:72)
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Ending task : pool-2-thread-2
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Ending task : pool-2-thread-1
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 15 Ending task : pool-2-thread-6
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Ending task : pool-2-thread-3
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Ending task : pool-2-thread-4
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 4 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 5 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 7 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 14 Ending task : pool-2-thread-5
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 6 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T19:14:19.825+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 8 Long running tasks...Starting tasks : pool-2-thread-6
//2026-09-24T19:14:19.825+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 9 Long running tasks...Starting tasks : pool-2-thread-5
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 8 Ending task : pool-2-thread-6
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 7 Ending task : pool-2-thread-2
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 4 Ending task : pool-2-thread-1
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 11 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 10 Long running tasks...Starting tasks : pool-2-thread-6
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 9 Ending task : pool-2-thread-5
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 6 Ending task : pool-2-thread-4
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 5 Ending task : pool-2-thread-3
//2026-09-24T19:14:23.834+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 13 Long running tasks...Starting tasks : pool-2-thread-5
//2026-09-24T19:14:23.834+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 12 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T19:14:27.836+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 10 Ending task : pool-2-thread-6
//2026-09-24T19:14:27.836+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 13 Ending task : pool-2-thread-5
//2026-09-24T19:14:27.836+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 11 Ending task : pool-2-thread-2
//2026-09-24T19:14:27.837+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 12 Ending task : pool-2-thread-1


//ERROR : Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@5e63cad[Not completed, task = java.util.concurrent.Executors$RunnableAdapter@55b62629[Wrapped task = com.codingshuttle.LearnAsysncSchedulingApplication.LongRunningTask@a53bb6f]] rejected from java.util.concurrent.ThreadPoolExecutor@7f4d9395[Running, pool size = 6, active threads = 6, queued tasks = 10, completed tasks = 0]
//Running, pool size = 6, active threads = 6, queued tasks = 10, completed tasks = 0
//We have 10 tasks inside our queue and we have running pool of size 6,we have 6 threads that are running inside our pool and we have activeThreads = 6,taskqueued = 10,after 4 sec some tasks will start to get end and also some more tasks will start
// 1 Ending task : pool-2-thread-2
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Ending task : pool-2-thread-1
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 15 Ending task : pool-2-thread-6
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Ending task : pool-2-thread-3
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Ending task : pool-2-thread-4
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 4 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 5 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 7 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T19:14:19.822+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 14 Ending task : pool-2-thread-5
//2026-09-24T19:14:19.823+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 6 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T19:14:19.825+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 8 Long running tasks...Starting tasks : pool-2-thread-6
//2026-09-24T19:14:19.825+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 9 Long running tasks...Starting tasks : pool-2-thread-5
//2026-09-24T19:14:23.833+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 8 Ending task : pool-2-thread-6

//GUESSSS HOW MANY STARTING OPERATIONS SHOULD BE HAVE 100 right,if all 100 tasks are to be run but we only have 16 starting operations because thread queue = 10 , thread pool maximum tasks that can be executer = 6 rest all tasks will be rejected and only 16 tasks will be executed
//In the first go we had 6 threads all those 6 threads would get acquired / all the 6 tasks were assigned to the thread pool,its assigning the tasks to the random threads that are available in the thread pool
// 3 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T19:14:15.819+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T19:14:15.820+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 14 Long running tasks...Starting tasks : pool-2-thread-5
//2026-09-24T19:14:15.820+05:30  INFO 24452 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 15 Long running tasks...Starting tasks : pool-2-thread-6
//Its taking the operations tasks from the for loop submit method and assigning them to the threads in the thread pool tasks 3,0,1,2,14,15 not in order are assigned to the 6 threads in the thread pool,whichever tasks comes first will be assigned the thread in the thread pool
//Its taking our operations from here the for loop and it iss assigning some operations to some threads thats how multi threading works
//tasks are also not assigned in order to the threads tasks such as 3,0,l,1,2,14,and 15 got the threads first available inside the thread pool not in order thats how tasks are assigned in the taskQUEUE
//6 TASKS WERE ASSIGNED TO THE 6 THREADS,BECAUSE WE HAVE ONLY MAX THREADS AS 6 INSIDE THE THREAD POOL
//WE HAVE ARRAY SIZE 10 ALSO : WE CAN HAVE 10 TASKS IN THE QUEUE AS WELL,AND ONCE THE THREADS ARE FREE EXECUTING PREVIOUS TASKS ASSIGNED TO THEM THEY START TAKING UP THE TASKS FROM THE TASK QUEUE
//TASKS PUT INSIDE THE TASK QUEUE WILL BE STARTED ONCE THE THREAD FINISHES EXECUTING PREVIOUS ASSIGNED TASKS
//10 TASKS PUT INSIDE THE TASK QUEUE,6 TASKS WERE DIRECTLY ASSIGNED -> TOTAL 16 TASKS
//EVEN IF WE STARTED WITH 100 TASKS USING TEH FOR LOOP,84 TASKS WERE REJECTED LIKE THIS BECAUSE THERE WAS NO RESOURCE TO COMPLETE THEM
//Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@5e63cad[Not completed, task = java.util.concurrent.Executors$RunnableAdapter@55b62629[Wrapped task = com.codingshuttle.LearnAsysncSchedulingApplication.LongRunningTask@a53bb6f]] rejected from java.util.concurrent.ThreadPoolExecutor@7f4d9395[Running, pool size = 6, active threads = 6, queued tasks = 10, completed tasks = 0]
//	at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2081)
//ONLY 16 TASKS WERE FULFILLED
//WE CAN CONFIGURE THE REJECTION POLICY AS WELL < SO THAT WE CAN DO THE RETRY OR WE CAN CONFIGURE THE CAPABILITIES HERE DEPENDING UPION THE HARDWARE CAPABILITIES OF THE SYSTEM
//SYSTEM IS GOOD AND CAN SUPPORT MULTIPLE TASKS PARALLELY ,we can increase the limit and increase the maxPoolSize.IF TEH COMPUTATIONS TASKS ARE VERY HEAY AND WE ARE ALSO MAKING THE maxThread size also high then the system may suffer.APPLICATION MIGHT GET OUT OF COMPUTE BECAUEE THE SYSTEM DOES NOT HAVE MUCH RESOURCES TO DO COMPUTED PARALLELY BECAUSE RESOURCES ATRE ALSO USED BY EACH THREAD
//IF YOU HAVE SOMETHING TASKS LIKE : INPUT/OUTPUT RELATYED OPERATIONS THEN IN THAT CASE WE CAN MAKE maxThread much larger like 10000
//DEPENDING ON THE TYPE OF TASKS,KIND OF OPERATIONS TO BE PERFORMED INSIDE THE TASKS YOu CAN CHANGE THE maxThread no
//SINCE,The computational task we are doing here is sleeping for 4 sec so it is not that much compute heavy as well,if we make it 600 maxThread so all the 100 tasks would get assigned to the threads inside the thread pool




//---------INSIDE THE THREADPOOLEXECUTOR----------------------
//ThreadFactory = way to create threads.
//Chef hiring Process = how you want to create new threads,you can assign certain properties to the thread.You cannot customize the threads that much but you can assign the name like the prefix you want to add to the name of the thread.
//rejectionHandler=If the threads are getting rejected then how do you want to handle that rejected threads
//When we were not doing Thread.sleep(1sec) and bombarding all the tasks at the same time then we are getting an error because at the same time all tasks want threads so in that way some tasks are getting to threads in the thread pool + task queue