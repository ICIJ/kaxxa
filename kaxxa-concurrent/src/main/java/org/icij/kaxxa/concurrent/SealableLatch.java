package org.icij.kaxxa.concurrent;

/**
 */
public interface SealableLatch {

	void signal();

	void await() throws InterruptedException;

	void seal();

	boolean isSealed();
}
