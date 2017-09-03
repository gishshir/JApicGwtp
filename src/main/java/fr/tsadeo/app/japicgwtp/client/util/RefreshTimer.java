package fr.tsadeo.app.japicgwtp.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Command;

public class RefreshTimer {
	private final RefreshRepeatingCommand repeatingCommand;
	private final int delai;
	private final int period;

	private int currentLoop = 0;
	private int maxLoop = 1;
	private boolean running = false;

	public RefreshTimer(int delai, int period,  final Command action) {

		this.delai = delai;
		this.period = period;
        this.repeatingCommand = new RefreshRepeatingCommand(action);
        
	}
	
	private void incrementsLoop() {
		this.currentLoop++;
	}
	private boolean canContinue() {
		this.running = this.currentLoop < maxLoop;
		return this.running;
	}

	public void doCancel() {
		this.maxLoop = 0;
	}

	public void doScheduleRepeting(int maxLoop) {
		this.doCancel();
		this.maxLoop = maxLoop;
		this.currentLoop = 0;
		if (!this.running) {
		    // TODO gerer delai
		   this.running = true;
		   Scheduler.get().scheduleFixedDelay(this.repeatingCommand, this.period);
		} else {
			this.currentLoop = 0;
		}
	}

	public void doSchedule() {
		this.doScheduleRepeting(1);
	}
	
	private  class RefreshRepeatingCommand implements RepeatingCommand {
		
		private final Command action;
		
		 private RefreshRepeatingCommand(final Command action) {
			this.action = action;
		}
		
		@Override
		public boolean execute() {
			System.out.println("action execute...");
			this.action.execute();
			incrementsLoop();
			return canContinue();
		}

		
	}
}
