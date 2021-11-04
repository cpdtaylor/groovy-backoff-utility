package com.morpheus.util.backoff

/**
 * Provide a function that can run with Back Off
 * @param <T>
 * @author Chris Taylor
 */
class BackOffFunction<T> {

  // The closure that will run with backoff
  private Closure backOffClosure
  // The list of arguments to the function
  private ArrayList<T> args

  public BackOffFunction(Closure fn, T... args){
    this.backOffClosure = fn
    this.args = args
  }

  def execute() {
    return this.backOffClosure(*this.args)
  }

  Closure getBackOffClosure() {
    return backOffClosure
  }

  void setBackOffClosure(Closure backOffClosure) {
    this.backOffClosure = backOffClosure
  }

  ArrayList<T> getArgs() {
    return args
  }

  void setArgs(T... args) {
    this.args = args
  }
}