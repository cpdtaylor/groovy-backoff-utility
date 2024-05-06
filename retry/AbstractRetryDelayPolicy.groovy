package com.morpheusdata.util.retry

abstract class AbstractRetryDelayPolicy {

  abstract Long getMaxSleepTime()

  abstract void setMaxSleepTime(Long maxSleepTime)

  abstract Long getInitialSleepTime()

  abstract void setInitialSleepTime(Long initialSleepTime)

  abstract Long calculateRetryDelay(Long retryAttempt)

}
