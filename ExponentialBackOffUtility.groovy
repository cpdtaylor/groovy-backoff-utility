package com.morpheus.util.backoff

import groovy.util.logging.Slf4j

/**
 * Utility class for executing a BackOffFunction with Exponential Backoff - useful for rate limiting functions that interface with APIs
 * Currently this utility will implement Exponential Back Off using a Fibonacci Sequence but can be extended in the future
 * @author Chris Taylor
 */
class ExponentialBackOffUtility extends AbstractBackOffUtility{

  private ArrayList <Integer> fibonacciSequence = [1, 1, 2, 3, 5, 8, 13] //fibonacci sequence.
  // sleep time multiplier
  private Integer sleepTime = 1000 // 1 second
  // maximum sleep time
  private Integer maxWaitTime = null
  // current elapsed sleep time
  private Long elapsedTime = null
  // Maximum length of fibonacci sequence
  private static final Integer MAX_FIBONACCI_SIZE = 30

  ExponentialBackOffUtility(
    LinkedHashMap<Class<? extends Exception>, ArrayList<String>> communicationErrors = [:],
    Integer sleepTime = null,
    Integer maxWaitTime = null,
    Integer fibonacciSize = null
  ) {
    super(communicationErrors)
    handleSleepTime(sleepTime)
    handleMaxWaitTime(maxWaitTime)
    handleFibonacciSize(fibonacciSize)
  }
  
  /**
   * Execute a function with Exponential Backoff
   * @param backOffFunction BackOffFunction that will be executed
   * @param updateParamsFunction Function to update params of BackOffFunction
   * @return
   */
  @Override
  def execute(BackOffFunction backOffFunction, UpdateParamsFunction updateParamsFunction = null) {
    if (this.maxWaitTime) {this.elapsedTime = 0L}
    else {this.elapsedTime = null}
    if (updateParamsFunction) {updateParamsFunction.setBackOffFunction(backOffFunction)}

    for (int attempt = 0; attempt < this.fibonacciSequence.size(); attempt++) {
      try {
        //log.debug("Executing ExponentialBackOffUtility attempt: {}", attempt)
        //println "Executing ExponentialBackOffUtility attempt: ${attempt}"
        return backOffFunction.execute()
      } catch (Exception e) {
        handleException( attempt, e )
        if (updateParamsFunction) {updateParamsFunction.updateParams()} //update params of BackOffFunction
      }
    }
    throw new BackOffException( "Maximum Fibonacci Sequence Attempts Reached", "1000")
  }

  Integer getSleepTime() {
    return sleepTime
  }

  void setSleepTime(Integer sleepTime) {
    handleSleepTime(sleepTime)
  }

  Integer getMaxWaitTime() {
    return maxWaitTime
  }

  void setMaxWaitTime(Integer maxWaitTime) {
    handleMaxWaitTime(maxWaitTime)
  }

  void setFibonacciSize(Integer fibonacciSize) {
    handleFibonacciSize(fibonacciSize)
  }

  private handleException(int attempt, Exception e) {
    if (!this.isCommunicationError(e)) {
      throw e
    }
    wait( attempt ) //An expected communication error has been thrown so wait
  }

  private wait(int attempt) {
    def fibonacciSleepTime = this.fibonacciSequence[attempt] * this.sleepTime
    try {
      if (this.elapsedTime != null) {
        if ((this.elapsedTime + this.sleepTime) <= this.maxWaitTime) {
          this.elapsedTime += (Long) this.sleepTime
          doSleep(fibonacciSleepTime)
        } else {
          throw new BackOffException("Maximum Wait Time Of ${this.maxWaitTime} Reached.", "2000")
        }
      }
      doSleep(fibonacciSleepTime)
    }
    catch (InterruptedException e) {throw new RuntimeException( e )}
  }

  private handleSleepTime(Integer sleepTime) {
    if (sleepTime && sleepTime > 0) {
      this.sleepTime = sleepTime
    }
  }

  private handleMaxWaitTime(Integer maxWaitTime) {
    if (maxWaitTime && maxWaitTime > 0) {
      if(maxWaitTime < this.sleepTime) {
        //log.warn("maxWaitTime must be greater than sleepTime. Setting maxWaitTime to {}", this.sleepTime)
        maxWaitTime = this.sleepTime
      }
      this.maxWaitTime = maxWaitTime
    }
  }

  private handleFibonacciSize(Integer fibonacciSize) {
    if (fibonacciSize && fibonacciSize > 0) {
      if (fibonacciSize > MAX_FIBONACCI_SIZE) {
        //log.warn("fibonacciSize must not be greater than {}. Setting fibonacciSize to {}", MAX_FIBONACCI_SIZE)
        fibonacciSize = MAX_FIBONACCI_SIZE
      }
      this.fibonacciSequence = this.generateFibonacciSequence(fibonacciSize)
    }
  }

  /**
   * Generate a fibonacci sequence of size fibonacciSize
   * @param fibonacciSize
   * @return
   */
  def generateFibonacciSequence(fibonacciSize){
    return (1..fibonacciSize).collect {fib(it)}
  }

  /**
   * Recursively generate the nth fibonancci number
   * @param n
   * @return
   */
  private fib(Integer n) {
    switch (n) {
      case 1..2: 1; break
      default: n = fib(n-1) + fib(n-2); break
    }
  }
}
