package com.morpheus.util.backoff

/**
 * Provide a function that can update params of a BackOffFunction
 * @param <T>
 * @author Chris Taylor
 */
class UpdateParamsFunction {
  // The closure that will update params for BackOffFunction
  private Closure updateParamsClosure
  // Additional opts to the BackOffFunction
  private opts
  //BackOffFunction to update params with
  private BackOffFunction backOffFunction

  public UpdateParamsFunction(BackOffFunction backOffFunction, Closure updateParamsFunction, opts = null){
    this.updateParamsClosure = updateParamsFunction
    this.opts = opts
    this.backOffFunction = backOffFunction
  }

  public UpdateParamsFunction(Closure updateParamsFunction, opts = null){
    this.updateParamsClosure = updateParamsFunction
    this.opts = opts
  }

  def updateParams() {
    if (this.backOffFunction) {
      def params = this.backOffFunction.getArgs()
      if (this.opts) {
        params = params + [this.opts]
      }
      params = this.updateParamsClosure(*params)
      this.backOffFunction.setArgs(*params)
    }
  }

  Closure getUpdateParamsClosure() {
    return updateParamsClosure
  }

  void setUpdateParamsClosure(Closure updateParamsClosure) {
    this.updateParamsClosure = updateParamsClosure
  }

  Map getOpts() {
    return opts
  }

  void setOpts(opts) {
    this.opts = opts
  }

  BackOffFunction getBackOffFunction() {
    return backOffFunction
  }

  void setBackOffFunction(BackOffFunction backOffFunction) {
    this.backOffFunction = backOffFunction
  }
}