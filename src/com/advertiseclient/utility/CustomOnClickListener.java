package com.advertiseclient.utility;

import android.widget.Button;
/**
 * Private OnClickListener that is able to run its task in a separate
 * thread.
 */
public abstract class CustomOnClickListener implements Button.OnClickListener, Runnable{
	
	public abstract void run();
}
