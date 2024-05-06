package com.morpheusdata.util.retry

class ExponentialRetryDelayPolicy extends AbstractRetryDelayPolicy {

  Long multiplier
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

  Long getMultiplier() {
    return multiplier
  }

  void setMultiplier(Long multiplier) {
    this.multiplier = multiplier
  }

  @Override
  Long calculateRetryDelay(Long attempt) {
    Long sleepTime = 1000l //1 seconds
    if(this.initialSleepTime) {
      sleepTime = this.initialSleepTime
    }
    if(attempt > 1) {
      sleepTime = (sleepTime * Math.pow(multiplier, attempt)).toLong()
    }
    if(this.maxSleepTime && (sleepTime > this.maxSleepTime)) {
      sleepTime = this.maxSleepTime
    }
    return sleepTime
  }

}
