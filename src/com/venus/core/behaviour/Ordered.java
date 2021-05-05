package com.venus.core.behaviour;

public interface Ordered {
	/**
	 * This method will be overridden to implement the order of the post-processor
	 * beans reflecting the execution order of its methods.
	 */
	public int order();
}
