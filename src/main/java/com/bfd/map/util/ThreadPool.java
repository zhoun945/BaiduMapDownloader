package com.bfd.map.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhoun945@gmail
 * @Date 2018-03-27
 */
public class ThreadPool {

  private int threadNum;
  private AtomicInteger threadAlive = new AtomicInteger();
  private ReentrantLock reentrantLock = new ReentrantLock();
  private Condition condition = reentrantLock.newCondition();

  public ThreadPool(int threadNum) {
    this.threadNum = threadNum;
    this.executorService = Executors.newFixedThreadPool(threadNum);
  }

  public ThreadPool(int threadNum, ExecutorService executorService) {
    this.threadNum = threadNum;
    this.executorService = executorService;
  }

  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  public int getThreadAlive() {
    return threadAlive.get();
  }

  public int getThreadNum() {
    return threadNum;
  }

  private ExecutorService executorService;

  public void execute(final Runnable runnable) {

    if (threadAlive.get() >= threadNum) {
      try {
        reentrantLock.lock();
        while (threadAlive.get() >= threadNum) {
          try {
            condition.await();
          } catch (InterruptedException e) {
          }
        }
      } finally {
        reentrantLock.unlock();
      }
    }
    threadAlive.incrementAndGet();
    executorService.execute(new Runnable() {
      @Override
      public void run() {
        try {
          runnable.run();
        } finally {
          try {
            reentrantLock.lock();
            threadAlive.decrementAndGet();
            condition.signal();
          } finally {
            reentrantLock.unlock();
          }
        }
      }
    });
  }

  public boolean isShutdown() {
    return executorService.isShutdown();
  }

  public void shutdown() {
    executorService.shutdown();
  }

}
