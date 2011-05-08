package com.ttProject.red5.server.adapter.library.php;

import org.red5.server.api.scheduling.IScheduledJob;
import org.red5.server.api.scheduling.ISchedulingService;

import com.ttProject.red5.server.adapter.ApplicationAdapterPhp;

public class ScheduledJobWrapper implements IScheduledJob{
	private ApplicationAdapterPhp adapter;
	private String phpfile;

	/**
	 * @param adapter
	 * @param phpfile
	 */
	public ScheduledJobWrapper(ApplicationAdapterPhp adapter, String phpfile) {
		this.adapter = adapter;
		this.phpfile = phpfile;
	}

	@Override
	public void execute(ISchedulingService service)
			throws CloneNotSupportedException {
		adapter.execute(phpfile, adapter, service);
	}
}
