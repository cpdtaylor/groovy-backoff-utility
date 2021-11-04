package com.morpheus.util.backoff

import javax.net.ssl.SSLHandshakeException

/**
 * Abstract class for executing a BackOffFunction with delayed execution, retry logic, rate limiting and backoff logic
 * @author Chris Taylor
 */
class AbstractBackOffUtility implements BackOffUtilityInterface{

  //Expected communication errors that will trigger a sleep and retry action
  private communicationErrors = [(SSLHandshakeException.class) : [], (SocketTimeoutException.class): []]

  AbstractBackOffUtility(
    LinkedHashMap<Class<? extends Exception>, ArrayList<String>> communicationErrors = [:]
  ) {
    if (communicationErrors) {this.communicationErrors = communicationErrors}
  }
  
  def execute(BackOffFunction backOffFunction, UpdateParamsFunction updateParamsFunction = null) {
    return backOffFunction.execute()
  }
  
  def getCommunicationErrors() {
    return communicationErrors
  }

  void setCommunicationErrors(communicationErrors) {
    this.communicationErrors = communicationErrors
  }
  
  protected isCommunicationError(Exception e) {
    def rtn = false
    if (e?.getClass() != null) {
      this.communicationErrors.any{ clazz, messages ->
        if (e?.getClass() == clazz) {
            if (messages) {
                rtn = messages.any {it =~ e?.getMessage()}
            } else {
                rtn = true
            }
        }
      }
    }
    return rtn
  }

  /**
   * Thread sleep for sleepTime
   * @param sleepTime
   * @return
   */
  protected doSleep(int sleepTime) {Thread.sleep(sleepTime)}
}
