package com.codingshuttlte.learn.multiThreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MultiThreadingApplication {

	public static void main(String[] args) throws InterruptedException{
		SpringApplication.run(MultiThreadingApplication.class, args);
//The main method would run in main thread only
//When a new thread is created with the help of Thread class , it is in the start state
//JVM will assume the thread to be in the runnable state whether the cpu is assigned or not assigned to the thread.Becasde when the cpu will be assigned to the thread is only decided by the operating system
//Using this Thread class we are creating a thread and this thread object would be stored in a heap memory

//	Thread workerThread = new Thread();
//	workerThread.start(); //but currently this workerThread is not doing anything,this Thread() is spawning a new thread,making a new thread but it is not going to do anything because we have not define any task to it.What task the thread should perform we have not defined that task


//To define a task to the thread we have to use runnable
//Runnable is an interface which is a functional interface which has only one abstract method which is run(),functional interface can be replace with lambda
//Using runnable, we can pass on tasks  that can  run in this new thread(worker thread)
//Thread workerThread = new Thread(
//		new Runnable() {
//			@Override
//			public void run() {
//
//			}
//		}
//);


//Use with lambda

		log.info("BEFORE THREAD,Name of threads : {},State of thread : {}",Thread.currentThread().getName(),Thread.currentThread().getState()); //This will be the main Thread

//All THESE THREAD STATES ARE MANAGED BY JVM ONLY.JVM will ask os to crrate a new thread as soon as we write this and that thread will take around 1MB of space inside the thread allocation only,so dont create many threads because it will take a lot of resources and if thread are blocked doing something we blocked the thread for 5sec means for that 5 sec thread cannot do anything and will hold the resouces and this is not the JVM based thing this is the actual OS kernel thread.Before virtual thread one thread that you create in JVM would actually mean one OS kernel thread.The virtual thread is just a wrapper aorund the actual OS kernel thread,here we are blocked by the limitations of the hardware.One thread in JVM means one OS kernel thread
//Virtual thread solves this problem ,with virtual thread you have platform thread pool which will be taken care by JVM.Platform thread pool has a few no of threads,you as a user can create as many virtual threads as you want
//Virtual threads are much light weight.Platform threads take areound 1Mb while virtual threads cna be created in some kilo bytes
//Now its the duty of JVM to assign virtual thread to the platform thread only
//If virtual thread goes too blocking state then that completely fine because another virtual thread can come and acquire the OS kernel thread in which that previous virtual thread that went to sleep was there.THis is a concept of switching and JVM will take care of this switching
//WITHOUT VIRTUAL THREADS,AS SOON AS YOU CREATE A NEW THREAD THAT THREAD IS GOING TO CREATE A OS KERNEL THREAD,ITS GOING TO TAKE SOME RESOURCE AND IT IS NOT THAT LIGHTWEIGHT.IF YOU WANT TO CREATE MILLIONS OF THREAD YOU CANNOT DO THAT WITH OS KERNELS  because they tak resourcses

//Here we are passing RUNNABLE INSIDE A THREAD
		Thread workerThread = new Thread(
				() -> {
//  Define whatever task you want to perform with this worker thread
					log.info("Inside the thread,Name of threads : {},State of thread : {}",Thread.currentThread().getName(),Thread.currentThread().getState());
                    try {
                        Thread.sleep(5000); //5sec.The worker thread would stuck here for 5 sec.In the output we will see Before thread,Inside thread and then after 5 sec we will see the After thread being printed
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
		);
		workerThread.start(); //If we dont start the thread it will not do anything,because the thread will be created but not started

		//workerThread.join(); //The calling thread(main Thread is blocked here until the worker Thread finishes its work)The calling thread will wait here until this worker thread finishes its task.blocks the calling thread which is the main thread. The calling thread is main thread here , and throws the checked exception so we have to catch the exception
//See the state of the worker thread after it has finished its task
		log.info("AFTER WORKER THREAD,State of worker thread : {}",workerThread.getState()); //TERMINATED .But if we comment our workerThread.join() so the main thread will not be blocked until the worker thread finishes its job,the main thread will keep on running and now the state of worker Thread will be RUNNABLE
		log.info("AFTER THREAD,Name of threads : {},State of thread : {}",Thread.currentThread().getName(),Thread.currentThread().getState());


//	BEFORE THREAD,Name of threads : main,State of thread : RUNNABLE
//2026-09-24T13:06:56.982+05:30  INFO 26612 --- [multiThreading] [       Thread-1] c.c.l.m.MultiThreadingApplication        : Inside the thread,Name of threads : Thread-1,State of thread : RUNNABLE
//2026-09-24T13:07:01.986+05:30  INFO 26612 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : AFTER WORKER THREAD,State of worker thread : TERMINATED
//2026-09-24T13:07:01.986+05:30  INFO 26612 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : AFTER THREAD,Name of threads : main,State of thread : RUNNABLE

//AFTER COMMENTING workerThread.join()
//   : BEFORE THREAD,Name of threads : main,State of thread : RUNNABLE
//2026-09-24T13:09:26.899+05:30  INFO 19724 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : AFTER WORKER THREAD,State of worker thread : RUNNABLE
//2026-09-24T13:09:26.900+05:30  INFO 19724 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : AFTER THREAD,Name of threads : main,State of thread : RUNNABLE
//2026-09-24T13:09:26.900+05:30  INFO 19724 --- [multiThreading] [       Thread-1] c.c.l.m.MultiThreadingApplication        : Inside the thread,Name of threads : Thread-1,State of thread : RUNNABLE

	}
}


//OUTPUT:
// [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : Starting MultiThreadingApplication using Java 21.0.12 with PID 20040 (C:\Users\lenovo\Downloads\multiThreading\multiThreading\target\classes started by lenovo in C:\Users\lenovo\Downloads\multiThreading)
//2026-09-24T12:27:07.289+05:30  INFO 20040 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : No active profile set, falling back to 1 default profile: "default"
//2026-09-24T12:27:07.883+05:30  INFO 20040 --- [multiThreading] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
//2026-09-24T12:27:07.894+05:30  INFO 20040 --- [multiThreading] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
//2026-09-24T12:27:07.895+05:30  INFO 20040 --- [multiThreading] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.24]
//2026-09-24T12:27:07.930+05:30  INFO 20040 --- [multiThreading] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 607 ms
//2026-09-24T12:27:08.226+05:30  INFO 20040 --- [multiThreading] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
//2026-09-24T12:27:08.229+05:30  INFO 20040 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : Started MultiThreadingApplication in 1.298 seconds (process running for 1.7)
//----ALL THESE LOGS ATRE COMING FROM THE MAIN THREAD---------

//2026-09-24T12:27:08.233+05:30  INFO 20040 --- [multiThreading] [       Thread-1] c.c.l.m.MultiThreadingApplication        : Inside the thread
//--------THIS LOG IS COMING FROM THE THREAD-1,means : "Inside the thread" task was run in a separate thread




//2026-09-24T12:29:44.503+05:30  INFO 19892 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : BEFORE THREAD
//2026-09-24T12:29:44.503+05:30  INFO 19892 --- [multiThreading] [           main] c.c.l.m.MultiThreadingApplication        : AFTER THREAD
//2026-09-24T12:29:44.503+05:30  INFO 19892 --- [multiThreading] [       Thread-1] c.c.l.m.MultiThreadingApplication        : Inside the thread
//BEFORE THREAD AND AFTER THREAD IS PRINTED FIRST AND THEN "INSIDE THE THREAD"
//BECAUSE THEY ARE BEING EXCUTED BY THE MAIN THREAD "BEFORE THRREAD" AND "AFTER THREAD"-----ARE BEING EXCUTED BY THE MAIN THREAD
//AFTER THAT _---"INSIDE THE TYREAD" IS BEING PRINTED BY THE THREAD-1
//WHY ??????
//BECAUSE ALL THE JAVA APPLICATION'S THE MAIN METHOD WOULD BE RUN BY THE MAIN THREAD ONLY
//WHEN MAIN THREAD SAW THIS LINE : Thread workerThread = new Thread() it created a worker thread object in the heap memory
//Main thread saw Thread.start() means it has to start the worker thread so it started the workerThread,so it started the workerThread execution and then the JVM will assign the new thread which is called Thrwad-1 here
//And IT WILL Do this particular task on the thread-1 only
//But main thread wont stop here main thread will keep on continuing ,main thread will start the worker thread which means it tells JVM to create a new thread and assign the task to that thread but now JVM coordinated with OS , OS assigns make the thread and decides when to give this thread the CPU,but the main thread will keep on executing  ans hence main thread goes to the line "AFTER THREAD" and prints it
//WHEN THE THREAD-1 will be get assigned or the task will be assigned a new thread by the OS,JVM will tell OS that assign this task a new thread and OS will assign it a new thread , now when OS assigns this thread the CPU it will excute


//But WHAT IF YOU WANT To STOP THE EXECUTION OF MAIN THREAD AT workerThread.start() , until the worker thread has done excuting
//All though you should not block the main thread unless and until required
//Thw worker thread could also be doing some heavy operation could be making some http call,or could be doing some db operation or some other io operation
//And the main thread is waiting because we have done workerThread.join() so we should not do that



//https://chatgpt.com/share/6ab4e8a9-ae34-83ee-bfcd-38e309e497f9