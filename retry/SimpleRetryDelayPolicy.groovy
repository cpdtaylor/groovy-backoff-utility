package com.morpheusdata.util.retry

class SimpleRetryDelayPolicy extends AbstractRetryDelayPolicy {

  Long maxSleepTime
  Long initialSleepTime

  @Override
  Long getMaxSleepTime() {
    return this.maxSleepTime
  }

  @Override
  void setMaxSleepTime(Long maxSleepTime) {
    this.maxSleepTime = maxSleepTime
  }

  @Override
  Long getInitialSleepTime() {
    return this.initialSleepTime
  }

  @Override
  void setInitialSleepTime(Long initialSleepTime) {
    this.initialSleepTime = initialSleepTime
  }

  @Override
  Long calculateRetryDelay(Long attempt) {
    Long sleepTime = 1000l //1 seconds
    if(this.initialSleepTime) {
      sleepTime = this.initialSleepTime
    }
    if(this.maxSleepTime && (sleepTime > this.maxSleepTime)) {
      sleepTime = this.maxSleepTime
    }
    return sleepTime
  }
}
