package utilities;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.derby.tools.sysinfo;

import main.gui.MainMenuController;

public class Counter extends Thread {

	private Thread thread = null;
	private LocalTime localTimeValue;
	DateTimeFormatter formatter;
	String timeToString;
	MainMenuController controllerinst;

	public Counter() {
		localTimeValue = LocalTime.MIDNIGHT;
		formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		timeToString = localTimeValue.format(formatter);
	}

	public void startTimer(MainMenuController m) {
		controllerinst = m;
		System.out.println("controllerinst object value: " + controllerinst);
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
		System.out.println("Timer started");

	}

	public void stopTimer(MainMenuController m) {
		controllerinst = m;
		if (thread != null) {
			thread.interrupt();
			System.out.println("Timer stopped");
		}
		setTime(localTimeValue);
	}

	public void setTime(LocalTime localTimeValue) {
		this.localTimeValue = localTimeValue;
	}

	public String getTime() {

		return timeToString;

	}

	@Override
	public void run() {
		try {
			while (!thread.isInterrupted()) {
				setTime(localTimeValue);
				sleep(1000);
				localTimeValue = localTimeValue.plusSeconds(1);
				timeToString = localTimeValue.format(formatter);
				controllerinst.timerTextField.setText(timeToString);
				System.out.println(localTimeValue);
				System.out.println(controllerinst);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
