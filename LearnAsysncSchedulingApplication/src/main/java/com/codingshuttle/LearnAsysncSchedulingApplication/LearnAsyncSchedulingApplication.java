package com.codingshuttle.LearnAsysncSchedulingApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

@SpringBootApplication
@Slf4j
public class LearnAsyncSchedulingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnAsyncSchedulingApplication.class, args);


		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 6, 2, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(10),
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//Runnable = this is the thread that is going to run.Now we have the thread that got failed or rejected and also the executor which was supposed to run this failed thread
//ThreadExecutor = this is the executor on which the thread is supposed to run
			log.info("Thread Rejected ......Retrying:");
//BEfore retrying we can make the thread to sleep
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
				});

//Once the EXECUTOR IS CREATED WE HAVE TO EXECUTE SOMETHING : TASKS,by calling the submit method which can return something


//We have created a ThreadPooolExecutor , now it has to exceute something for that we have create tasks.To creater Taska we can create it in a separate class as well and we can create it here directly also
//Since it has only one method we cna use lambda over here
//This submit method can take something which is runnable
//		threadPoolExecutor.submit(
//				new Runnable() {
//					@Override
//					public void run() {
////	This run method here this is the task.This is a long running task
//log.info("Long running tasks...Starting tasks");
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//log.info("Ending task");
//                    }
//				}
//		);


//We only have on emethod which is run() inside the Runnable interface so we can replace it with lambda
//Inside run there is a task which we will get executed by ThreadPoolExecutor.
//The good thing about this is :
// When this will run the main thread,Once we are running this the main thread would be free ,the main thread will not keep doing this task or wait for the response from this task
//All these things happening here are going to happen inside a thread only

		log.info("Starting main thread : {}", Thread.currentThread().getName());

//	Future<String> result =  threadPoolExecutor.submit(
//				() -> {
////	This run method here this is the task.This is a long running task
//					log.info("Long running tasks...Starting tasks : {}",Thread.currentThread().getName());
//					try {
//						Thread.sleep(4000);
//					} catch (InterruptedException e) {
//						throw new RuntimeException(e);
//					}
//					log.info("Ending task : {}",Thread.currentThread().getName());
//					return "hello";
////submit() method can return something EX : you were calling some API and once the API has return its response ypu can return this response from here . HOW THIS RESPONSE WILL COME OUT TO THE main Thread() is with the help of Future
//				});

//		threadPoolExecutor.submit(new LongRunningTask("Pawani"));


		for(int i = 0;i<=20;i++) {
			threadPoolExecutor.submit(new LongRunningTask(i+""));
            try {
                Thread.sleep(1000); //Tasks will only be assigned to the threads with a delay of 1sec.main thread sleep for 1 sec before assining a task when the function will be called that is insiee the LongRunningTasks mneans main thread is telling JVM to ask OS to create a new thread for this task,or take a new thread from thread pool for this task
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
		log.info("ENDING MAIN THREAD :{}", Thread.currentThread().getName());
	}
}

//OUTPUT :
//.L.LearnAsyncSchedulingApplication    : Starting main thread : main
//2026-09-24T18:49:03.427+05:30  INFO 27836 --- [LearnAsysncSchedulingApplication] [           main] c.c.L.LearnAsyncSchedulingApplication    : ENDING MAIN THREAD :main
//2026-09-24T18:49:03.428+05:30  INFO 27836 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LearnAsyncSchedulingApplication    : Long running tasks...Starting tasks
//2026-09-24T18:49:07.441+05:30  INFO 27836 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LearnAsyncSchedulingApplication    : Ending task : pool-2-thread-1

//THis part was skipped threadPoolexecutor.submit(Runnable) because everything happening  here is going to execute inside another thread and then the main thread will execute the ending line
//This task is started in the thread-1 of the pool - 2 pool-2-thread-1
//In one pool we can have atmost maxThreads = 6 so starting the thread with thread-1 inside that pool
//We are running the threadPoolExecutor() one time , running one tasks only but we can also run it inside the for loop multiple tasks



//WHEN maxThread size is 600 and we have 100 tasks
// : Starting main thread : main
//2026-09-24T20:15:13.776+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T20:15:13.776+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T20:15:13.776+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T20:15:13.776+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T20:15:13.776+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 14 Long running tasks...Starting tasks : pool-2-thread-5
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-7] c.c.L.LongRunningTask                    : 16 Long running tasks...Starting tasks : pool-2-thread-7
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 15 Long running tasks...Starting tasks : pool-2-thread-6
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-8] c.c.L.LongRunningTask                    : 17 Long running tasks...Starting tasks : pool-2-thread-8
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-10] c.c.L.LongRunningTask                    : 19 Long running tasks...Starting tasks : pool-2-thread-10
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-9] c.c.L.LongRunningTask                    : 18 Long running tasks...Starting tasks : pool-2-thread-9
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-11] c.c.L.LongRunningTask                    : 20 Long running tasks...Starting tasks : pool-2-thread-11
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-12] c.c.L.LongRunningTask                    : 21 Long running tasks...Starting tasks : pool-2-thread-12
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-14] c.c.L.LongRunningTask                    : 23 Long running tasks...Starting tasks : pool-2-thread-14
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-13] c.c.L.LongRunningTask                    : 22 Long running tasks...Starting tasks : pool-2-thread-13
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-15] c.c.L.LongRunningTask                    : 24 Long running tasks...Starting tasks : pool-2-thread-15
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-16] c.c.L.LongRunningTask                    : 25 Long running tasks...Starting tasks : pool-2-thread-16
//2026-09-24T20:15:13.777+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-17] c.c.L.LongRunningTask                    : 26 Long running tasks...Starting tasks : pool-2-thread-17
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-18] c.c.L.LongRunningTask                    : 27 Long running tasks...Starting tasks : pool-2-thread-18
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-26] c.c.L.LongRunningTask                    : 35 Long running tasks...Starting tasks : pool-2-thread-26
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-20] c.c.L.LongRunningTask                    : 29 Long running tasks...Starting tasks : pool-2-thread-20
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-21] c.c.L.LongRunningTask                    : 30 Long running tasks...Starting tasks : pool-2-thread-21
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-22] c.c.L.LongRunningTask                    : 31 Long running tasks...Starting tasks : pool-2-thread-22
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-23] c.c.L.LongRunningTask                    : 32 Long running tasks...Starting tasks : pool-2-thread-23
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-24] c.c.L.LongRunningTask                    : 33 Long running tasks...Starting tasks : pool-2-thread-24
//2026-09-24T20:15:13.778+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-25] c.c.L.LongRunningTask                    : 34 Long running tasks...Starting tasks : pool-2-thread-25
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-27] c.c.L.LongRunningTask                    : 36 Long running tasks...Starting tasks : pool-2-thread-27
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-19] c.c.L.LongRunningTask                    : 28 Long running tasks...Starting tasks : pool-2-thread-19
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-32] c.c.L.LongRunningTask                    : 41 Long running tasks...Starting tasks : pool-2-thread-32
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-28] c.c.L.LongRunningTask                    : 37 Long running tasks...Starting tasks : pool-2-thread-28
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-46] c.c.L.LongRunningTask                    : 55 Long running tasks...Starting tasks : pool-2-thread-46
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-33] c.c.L.LongRunningTask                    : 42 Long running tasks...Starting tasks : pool-2-thread-33
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-47] c.c.L.LongRunningTask                    : 56 Long running tasks...Starting tasks : pool-2-thread-47
//2026-09-24T20:15:13.779+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-36] c.c.L.LongRunningTask                    : 45 Long running tasks...Starting tasks : pool-2-thread-36
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-30] c.c.L.LongRunningTask                    : 39 Long running tasks...Starting tasks : pool-2-thread-30
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-37] c.c.L.LongRunningTask                    : 46 Long running tasks...Starting tasks : pool-2-thread-37
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-31] c.c.L.LongRunningTask                    : 40 Long running tasks...Starting tasks : pool-2-thread-31
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-35] c.c.L.LongRunningTask                    : 44 Long running tasks...Starting tasks : pool-2-thread-35
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-57] c.c.L.LongRunningTask                    : 66 Long running tasks...Starting tasks : pool-2-thread-57
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-58] c.c.L.LongRunningTask                    : 67 Long running tasks...Starting tasks : pool-2-thread-58
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-55] c.c.L.LongRunningTask                    : 64 Long running tasks...Starting tasks : pool-2-thread-55
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-41] c.c.L.LongRunningTask                    : 50 Long running tasks...Starting tasks : pool-2-thread-41
//2026-09-24T20:15:13.780+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-60] c.c.L.LongRunningTask                    : 69 Long running tasks...Starting tasks : pool-2-thread-60
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-42] c.c.L.LongRunningTask                    : 51 Long running tasks...Starting tasks : pool-2-thread-42
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-45] c.c.L.LongRunningTask                    : 54 Long running tasks...Starting tasks : pool-2-thread-45
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-43] c.c.L.LongRunningTask                    : 52 Long running tasks...Starting tasks : pool-2-thread-43
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-29] c.c.L.LongRunningTask                    : 38 Long running tasks...Starting tasks : pool-2-thread-29
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-34] c.c.L.LongRunningTask                    : 43 Long running tasks...Starting tasks : pool-2-thread-34
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-48] c.c.L.LongRunningTask                    : 57 Long running tasks...Starting tasks : pool-2-thread-48
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-49] c.c.L.LongRunningTask                    : 58 Long running tasks...Starting tasks : pool-2-thread-49
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-50] c.c.L.LongRunningTask                    : 59 Long running tasks...Starting tasks : pool-2-thread-50
//2026-09-24T20:15:13.781+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-51] c.c.L.LongRunningTask                    : 60 Long running tasks...Starting tasks : pool-2-thread-51
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-52] c.c.L.LongRunningTask                    : 61 Long running tasks...Starting tasks : pool-2-thread-52
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-54] c.c.L.LongRunningTask                    : 63 Long running tasks...Starting tasks : pool-2-thread-54
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-53] c.c.L.LongRunningTask                    : 62 Long running tasks...Starting tasks : pool-2-thread-53
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-56] c.c.L.LongRunningTask                    : 65 Long running tasks...Starting tasks : pool-2-thread-56
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-72] c.c.L.LongRunningTask                    : 81 Long running tasks...Starting tasks : pool-2-thread-72
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-39] c.c.L.LongRunningTask                    : 48 Long running tasks...Starting tasks : pool-2-thread-39
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-40] c.c.L.LongRunningTask                    : 49 Long running tasks...Starting tasks : pool-2-thread-40
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-59] c.c.L.LongRunningTask                    : 68 Long running tasks...Starting tasks : pool-2-thread-59
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-44] c.c.L.LongRunningTask                    : 53 Long running tasks...Starting tasks : pool-2-thread-44
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-61] c.c.L.LongRunningTask                    : 70 Long running tasks...Starting tasks : pool-2-thread-61
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-62] c.c.L.LongRunningTask                    : 71 Long running tasks...Starting tasks : pool-2-thread-62
//2026-09-24T20:15:13.782+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-63] c.c.L.LongRunningTask                    : 72 Long running tasks...Starting tasks : pool-2-thread-63
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-64] c.c.L.LongRunningTask                    : 73 Long running tasks...Starting tasks : pool-2-thread-64
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-65] c.c.L.LongRunningTask                    : 74 Long running tasks...Starting tasks : pool-2-thread-65
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-66] c.c.L.LongRunningTask                    : 75 Long running tasks...Starting tasks : pool-2-thread-66
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-67] c.c.L.LongRunningTask                    : 76 Long running tasks...Starting tasks : pool-2-thread-67
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-69] c.c.L.LongRunningTask                    : 78 Long running tasks...Starting tasks : pool-2-thread-69
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-68] c.c.L.LongRunningTask                    : 77 Long running tasks...Starting tasks : pool-2-thread-68 After 4sec tasks will start to end
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [           main] c.c.L.LearnAsyncSchedulingApplication    : ENDING MAIN THREAD :main
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-70] c.c.L.LongRunningTask                    : 79 Long running tasks...Starting tasks : pool-2-thread-70
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-71] c.c.L.LongRunningTask                    : 80 Long running tasks...Starting tasks : pool-2-thread-71
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-73] c.c.L.LongRunningTask                    : 82 Long running tasks...Starting tasks : pool-2-thread-73
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-74] c.c.L.LongRunningTask                    : 83 Long running tasks...Starting tasks : pool-2-thread-74
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-91] c.c.L.LongRunningTask                    : 100 Long running tasks...Starting tasks : pool-2-thread-91
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-75] c.c.L.LongRunningTask                    : 84 Long running tasks...Starting tasks : pool-2-thread-75
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-76] c.c.L.LongRunningTask                    : 85 Long running tasks...Starting tasks : pool-2-thread-76
//2026-09-24T20:15:13.783+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-77] c.c.L.LongRunningTask                    : 86 Long running tasks...Starting tasks : pool-2-thread-77
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-79] c.c.L.LongRunningTask                    : 88 Long running tasks...Starting tasks : pool-2-thread-79
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-78] c.c.L.LongRunningTask                    : 87 Long running tasks...Starting tasks : pool-2-thread-78
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-80] c.c.L.LongRunningTask                    : 89 Long running tasks...Starting tasks : pool-2-thread-80
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-81] c.c.L.LongRunningTask                    : 90 Long running tasks...Starting tasks : pool-2-thread-81
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-82] c.c.L.LongRunningTask                    : 91 Long running tasks...Starting tasks : pool-2-thread-82
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-83] c.c.L.LongRunningTask                    : 92 Long running tasks...Starting tasks : pool-2-thread-83
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-84] c.c.L.LongRunningTask                    : 93 Long running tasks...Starting tasks : pool-2-thread-84
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-85] c.c.L.LongRunningTask                    : 94 Long running tasks...Starting tasks : pool-2-thread-85
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-86] c.c.L.LongRunningTask                    : 95 Long running tasks...Starting tasks : pool-2-thread-86
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-87] c.c.L.LongRunningTask                    : 96 Long running tasks...Starting tasks : pool-2-thread-87
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-88] c.c.L.LongRunningTask                    : 97 Long running tasks...Starting tasks : pool-2-thread-88
//2026-09-24T20:15:13.784+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-89] c.c.L.LongRunningTask                    : 98 Long running tasks...Starting tasks : pool-2-thread-89
//2026-09-24T20:15:13.785+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-90] c.c.L.LongRunningTask                    : 99 Long running tasks...Starting tasks : pool-2-thread-90
//2026-09-24T20:15:13.785+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-38] c.c.L.LongRunningTask                    : 47 Long running tasks...Starting tasks : pool-2-thread-38
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-40] c.c.L.LongRunningTask                    : 49 Ending task : pool-2-thread-40
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-53] c.c.L.LongRunningTask                    : 62 Ending task : pool-2-thread-53
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-54] c.c.L.LongRunningTask                    : 63 Ending task : pool-2-thread-54
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-72] c.c.L.LongRunningTask                    : 81 Ending task : pool-2-thread-72
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-44] c.c.L.LongRunningTask                    : 53 Ending task : pool-2-thread-44
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-62] c.c.L.LongRunningTask                    : 71 Ending task : pool-2-thread-62
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-48] c.c.L.LongRunningTask                    : 57 Ending task : pool-2-thread-48
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-39] c.c.L.LongRunningTask                    : 48 Ending task : pool-2-thread-39
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-49] c.c.L.LongRunningTask                    : 58 Ending task : pool-2-thread-49
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-45] c.c.L.LongRunningTask                    : 54 Ending task : pool-2-thread-45
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-51] c.c.L.LongRunningTask                    : 60 Ending task : pool-2-thread-51
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-43] c.c.L.LongRunningTask                    : 52 Ending task : pool-2-thread-43
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-49] c.c.L.LongRunningTask                    : 9 Long running tasks...Starting tasks : pool-2-thread-49
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-36] c.c.L.LongRunningTask                    : 45 Ending task : pool-2-thread-36
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-30] c.c.L.LongRunningTask                    : 39 Ending task : pool-2-thread-30
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-57] c.c.L.LongRunningTask                    : 66 Ending task : pool-2-thread-57
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-37] c.c.L.LongRunningTask                    : 46 Ending task : pool-2-thread-37
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-55] c.c.L.LongRunningTask                    : 64 Ending task : pool-2-thread-55
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-19] c.c.L.LongRunningTask                    : 28 Ending task : pool-2-thread-19
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-27] c.c.L.LongRunningTask                    : 36 Ending task : pool-2-thread-27
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-53] c.c.L.LongRunningTask                    : 10 Long running tasks...Starting tasks : pool-2-thread-53
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-18] c.c.L.LongRunningTask                    : 27 Ending task : pool-2-thread-18
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-52] c.c.L.LongRunningTask                    : 61 Ending task : pool-2-thread-52
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-26] c.c.L.LongRunningTask                    : 35 Ending task : pool-2-thread-26
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-22] c.c.L.LongRunningTask                    : 31 Ending task : pool-2-thread-22
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-20] c.c.L.LongRunningTask                    : 29 Ending task : pool-2-thread-20
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-24] c.c.L.LongRunningTask                    : 33 Ending task : pool-2-thread-24
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-50] c.c.L.LongRunningTask                    : 59 Ending task : pool-2-thread-50
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Ending task : pool-2-thread-2
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Ending task : pool-2-thread-1
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-61] c.c.L.LongRunningTask                    : 70 Ending task : pool-2-thread-61
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-56] c.c.L.LongRunningTask                    : 65 Ending task : pool-2-thread-56
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-34] c.c.L.LongRunningTask                    : 43 Ending task : pool-2-thread-34
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-44] c.c.L.LongRunningTask                    : 7 Long running tasks...Starting tasks : pool-2-thread-44
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-17] c.c.L.LongRunningTask                    : 26 Ending task : pool-2-thread-17
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-62] c.c.L.LongRunningTask                    : 5 Long running tasks...Starting tasks : pool-2-thread-62
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-39] c.c.L.LongRunningTask                    : 6 Long running tasks...Starting tasks : pool-2-thread-39
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-28] c.c.L.LongRunningTask                    : 37 Ending task : pool-2-thread-28
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-72] c.c.L.LongRunningTask                    : 8 Long running tasks...Starting tasks : pool-2-thread-72
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-48] c.c.L.LongRunningTask                    : 4 Long running tasks...Starting tasks : pool-2-thread-48
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-42] c.c.L.LongRunningTask                    : 51 Ending task : pool-2-thread-42
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-60] c.c.L.LongRunningTask                    : 69 Ending task : pool-2-thread-60
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-51] c.c.L.LongRunningTask                    : 12 Long running tasks...Starting tasks : pool-2-thread-51
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-71] c.c.L.LongRunningTask                    : 80 Ending task : pool-2-thread-71
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-43] c.c.L.LongRunningTask                    : 13 Long running tasks...Starting tasks : pool-2-thread-43
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-45] c.c.L.LongRunningTask                    : 11 Long running tasks...Starting tasks : pool-2-thread-45
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-25] c.c.L.LongRunningTask                    : 34 Ending task : pool-2-thread-25
//2026-09-24T20:15:17.797+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-89] c.c.L.LongRunningTask                    : 98 Ending task : pool-2-thread-89
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-58] c.c.L.LongRunningTask                    : 67 Ending task : pool-2-thread-58
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-46] c.c.L.LongRunningTask                    : 55 Ending task : pool-2-thread-46
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-33] c.c.L.LongRunningTask                    : 42 Ending task : pool-2-thread-33
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-32] c.c.L.LongRunningTask                    : 41 Ending task : pool-2-thread-32
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-21] c.c.L.LongRunningTask                    : 30 Ending task : pool-2-thread-21
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Ending task : pool-2-thread-3
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-7] c.c.L.LongRunningTask                    : 16 Ending task : pool-2-thread-7
//2026-09-24T20:15:17.793+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-31] c.c.L.LongRunningTask                    : 40 Ending task : pool-2-thread-31
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-6] c.c.L.LongRunningTask                    : 15 Ending task : pool-2-thread-6
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-9] c.c.L.LongRunningTask                    : 18 Ending task : pool-2-thread-9
//2026-09-24T20:15:17.792+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-29] c.c.L.LongRunningTask                    : 38 Ending task : pool-2-thread-29
//2026-09-24T20:15:17.791+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-59] c.c.L.LongRunningTask                    : 68 Ending task : pool-2-thread-59
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-10] c.c.L.LongRunningTask                    : 19 Ending task : pool-2-thread-10
//2026-09-24T20:15:17.794+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-8] c.c.L.LongRunningTask                    : 17 Ending task : pool-2-thread-8
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-14] c.c.L.LongRunningTask                    : 23 Ending task : pool-2-thread-14
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-12] c.c.L.LongRunningTask                    : 21 Ending task : pool-2-thread-12
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-13] c.c.L.LongRunningTask                    : 22 Ending task : pool-2-thread-13
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-38] c.c.L.LongRunningTask                    : 47 Ending task : pool-2-thread-38
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-15] c.c.L.LongRunningTask                    : 24 Ending task : pool-2-thread-15
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-90] c.c.L.LongRunningTask                    : 99 Ending task : pool-2-thread-90
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-87] c.c.L.LongRunningTask                    : 96 Ending task : pool-2-thread-87
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-47] c.c.L.LongRunningTask                    : 56 Ending task : pool-2-thread-47
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-85] c.c.L.LongRunningTask                    : 94 Ending task : pool-2-thread-85
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-84] c.c.L.LongRunningTask                    : 93 Ending task : pool-2-thread-84
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-82] c.c.L.LongRunningTask                    : 91 Ending task : pool-2-thread-82
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-81] c.c.L.LongRunningTask                    : 90 Ending task : pool-2-thread-81
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-86] c.c.L.LongRunningTask                    : 95 Ending task : pool-2-thread-86
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-80] c.c.L.LongRunningTask                    : 89 Ending task : pool-2-thread-80
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-78] c.c.L.LongRunningTask                    : 87 Ending task : pool-2-thread-78
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-35] c.c.L.LongRunningTask                    : 44 Ending task : pool-2-thread-35
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-41] c.c.L.LongRunningTask                    : 50 Ending task : pool-2-thread-41
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-75] c.c.L.LongRunningTask                    : 84 Ending task : pool-2-thread-75
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-74] c.c.L.LongRunningTask                    : 83 Ending task : pool-2-thread-74
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-73] c.c.L.LongRunningTask                    : 82 Ending task : pool-2-thread-73
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-70] c.c.L.LongRunningTask                    : 79 Ending task : pool-2-thread-70
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-91] c.c.L.LongRunningTask                    : 100 Ending task : pool-2-thread-91
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-79] c.c.L.LongRunningTask                    : 88 Ending task : pool-2-thread-79
//2026-09-24T20:15:17.795+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-16] c.c.L.LongRunningTask                    : 25 Ending task : pool-2-thread-16
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-68] c.c.L.LongRunningTask                    : 77 Ending task : pool-2-thread-68
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-63] c.c.L.LongRunningTask                    : 72 Ending task : pool-2-thread-63
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-66] c.c.L.LongRunningTask                    : 75 Ending task : pool-2-thread-66
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-67] c.c.L.LongRunningTask                    : 76 Ending task : pool-2-thread-67
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-23] c.c.L.LongRunningTask                    : 32 Ending task : pool-2-thread-23
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-69] c.c.L.LongRunningTask                    : 78 Ending task : pool-2-thread-69
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-5] c.c.L.LongRunningTask                    : 14 Ending task : pool-2-thread-5
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Ending task : pool-2-thread-4
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-65] c.c.L.LongRunningTask                    : 74 Ending task : pool-2-thread-65
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-64] c.c.L.LongRunningTask                    : 73 Ending task : pool-2-thread-64
//2026-09-24T20:15:17.796+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-11] c.c.L.LongRunningTask                    : 20 Ending task : pool-2-thread-11
//2026-09-24T20:15:17.797+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-77] c.c.L.LongRunningTask                    : 86 Ending task : pool-2-thread-77
//2026-09-24T20:15:17.797+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-88] c.c.L.LongRunningTask                    : 97 Ending task : pool-2-thread-88
//2026-09-24T20:15:17.797+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-83] c.c.L.LongRunningTask                    : 92 Ending task : pool-2-thread-83
//2026-09-24T20:15:17.797+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-76] c.c.L.LongRunningTask                    : 85 Ending task : pool-2-thread-76
//2026-09-24T20:15:21.806+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-44] c.c.L.LongRunningTask                    : 7 Ending task : pool-2-thread-44
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-62] c.c.L.LongRunningTask                    : 5 Ending task : pool-2-thread-62
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-51] c.c.L.LongRunningTask                    : 12 Ending task : pool-2-thread-51
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-39] c.c.L.LongRunningTask                    : 6 Ending task : pool-2-thread-39
//2026-09-24T20:15:21.806+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-43] c.c.L.LongRunningTask                    : 13 Ending task : pool-2-thread-43
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-53] c.c.L.LongRunningTask                    : 10 Ending task : pool-2-thread-53
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-72] c.c.L.LongRunningTask                    : 8 Ending task : pool-2-thread-72
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-48] c.c.L.LongRunningTask                    : 4 Ending task : pool-2-thread-48
//2026-09-24T20:15:21.807+05:30  INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-49] c.c.L.LongRunningTask                    : 9 Ending task : pool-2-thread-49
//2026-09-24T20:15:21.806+05:30  `INFO 5688 --- [LearnAsysncSchedulingApplication] [ool-2-thread-45] c.c.L.LongRunningTask                    : 11 Ending task : pool-2-thread-45

//WE cannot gurantee the order of tasks because we dont know in multithreading that which task would get assigned to which thread
//Because you want to run them parallely all these tasks
//If you want to maintain the ordering between the tasks then you want to actually get the response of one thread and then you want to another thread.That is completely different scenerio
//here we want to run them parallely as we have 8 core CPU inside the system so we can run 8 threads parallely but we have only 6 threads inside the thread pool so at the same time only 6 threads can run parallely
//But we dont have only these operations running inside the system so system might run this applciation threads concurrently also inside one CPU core , as different CPU could be assigned to different applications also running in the background.Like we are running intellij it is also a process running inside the APU only,we are running docker also in the background for that also it needs CPU










//---------TASKS WILL BE ASSIGNED AFTER A DELAY OF 1 sec,all tasks will not be assigned all at the same time------
//And because tasks are assigned to tasks at a delay of 1sec so ,some threads are getting free also which are already assigned the tasks that is why we are not getting the error also,because we have threads in the thread pool available as threads are getting free after 4sec and tasks are being assigned to the thread at a delay of 4sec.If tasks were assigned at the same time all together to all the threads then threadpool and threadqueue was getting full in 16 tasks only and other tasks were rejected that is why we were getting the error
//Here tasks are getting assigned after a delay of 1sec so threads are not getting full and till all the threads are getting assigned the tasks the before tasks which were assigned the tasks are getting free.
//Even the thread 5 and 6 ere not used only from the thread pool and they were able to complete the tasks in pnly 4 threads
//for(int i = 0;i<=20;i++) {
//			threadPoolExecutor.submit(new LongRunningTask(i+""));
//            try {
//                Thread.sleep(1000); //Tasks will only be assigned to the threads with a delay of 1sec.main thread sleep for 1 sec before assining a task when the function will be called that is insiee the LongRunningTasks mneans main thread is telling JVM to ask OS to create a new thread for this task,or take a new thread from thread pool for this task
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }






//We have made a sleep of 1 sec , so before assigning the next task to the new thread the main thread will wait for 1 sec, all teh tasks instantly will not be assigend to the threads from the thread pool immediatelty the tasks will be assigned to the thread one after the another
//The tasks will be assigned to thread after a delay of 1 sec
// Starting main thread : main
//0 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T21:05:33.035+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T21:05:34.049+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T21:05:35.060+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T21:05:36.044+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 0 Ending task : pool-2-thread-1
//2026-09-24T21:05:36.066+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 4 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T21:05:37.046+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 1 Ending task : pool-2-thread-2
//2026-09-24T21:05:37.077+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 5 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T21:05:38.057+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 2 Ending task : pool-2-thread-3
//2026-09-24T21:05:38.088+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 6 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T21:05:39.072+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 3 Ending task : pool-2-thread-4
//2026-09-24T21:05:39.103+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 7 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T21:05:40.069+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 4 Ending task : pool-2-thread-1
//2026-09-24T21:05:40.116+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 8 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T21:05:41.079+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 5 Ending task : pool-2-thread-2
//9 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T21:05:42.092+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 6 Ending task : pool-2-thread-3
//2026-09-24T21:05:42.139+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 10 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T21:05:43.104+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 7 Ending task : pool-2-thread-4
//2026-09-24T21:05:43.151+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 11 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T21:05:44.131+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 8 Ending task : pool-2-thread-1
//2026-09-24T21:05:44.162+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 12 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T21:05:45.141+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 9 Ending task : pool-2-thread-2
//2026-09-24T21:05:45.172+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 13 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T21:05:46.150+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 10 Ending task : pool-2-thread-3
//2026-09-24T21:05:46.181+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 14 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T21:05:47.164+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 11 Ending task : pool-2-thread-4
//2026-09-24T21:05:47.196+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 15 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T21:05:48.175+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 12 Ending task : pool-2-thread-1
//2026-09-24T21:05:48.207+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 16 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T21:05:49.187+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 13 Ending task : pool-2-thread-2
//2026-09-24T21:05:49.219+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 17 Long running tasks...Starting tasks : pool-2-thread-2
//2026-09-24T21:05:50.186+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 14 Ending task : pool-2-thread-3
//2026-09-24T21:05:50.233+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 18 Long running tasks...Starting tasks : pool-2-thread-3
//2026-09-24T21:05:51.202+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 15 Ending task : pool-2-thread-4
//2026-09-24T21:05:51.233+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 19 Long running tasks...Starting tasks : pool-2-thread-4
//2026-09-24T21:05:52.215+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 16 Ending task : pool-2-thread-1
//2026-09-24T21:05:52.247+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 20 Long running tasks...Starting tasks : pool-2-thread-1
//2026-09-24T21:05:53.227+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-2] c.c.L.LongRunningTask                    : 17 Ending task : pool-2-thread-2
//2026-09-24T21:05:53.258+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [           main] c.c.L.LearnAsyncSchedulingApplication    : ENDING MAIN THREAD :main
//2026-09-24T21:05:54.244+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-3] c.c.L.LongRunningTask                    : 18 Ending task : pool-2-thread-3
//2026-09-24T21:05:55.246+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-4] c.c.L.LongRunningTask                    : 19 Ending task : pool-2-thread-4
//2026-09-24T21:05:56.257+05:30  INFO 28288 --- [LearnAsysncSchedulingApplication] [pool-2-thread-1] c.c.L.LongRunningTask                    : 20 Ending task : pool-2-thread-1