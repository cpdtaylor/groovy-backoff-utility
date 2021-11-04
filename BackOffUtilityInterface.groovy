package com.morpheus.util.backoff

interface BackOffUtilityInterface {
    
  /**
   * Execute a function with Backoff
   * @param backOffFunction BackOffFunction that will be executed
   * @param updateParamsFunction Function to update params of BackOffFunction
   * @return
   */
  def execute(BackOffFunction backOffFunction, UpdateParamsFunction updateParamsFunction) 
}
