package berlin.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class FixedMemoryLogbackAppender extends AppenderBase<ILoggingEvent> {

	private PatternLayoutEncoder encoder;
	
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
	private static int maxSize = 5000;
	
	private static LinkedList<String> lines = new LinkedList<>();
	
	public void setMaxSize(int newMaxSize) {
		maxSize = newMaxSize;
	}
	
	public int getMaxSize() {
		return maxSize;
	}

	public PatternLayoutEncoder getEncoder() {
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoder encoder) {
		this.encoder = encoder;
	}

	@Override
	public void start() {
		if (this.encoder == null) {
			addError("No encoder set for the appender named [" + name + "].");
			return;
		}

		try {
			encoder.init(outputStream);
		} catch (IOException e) {
		}
		super.start();
	}

	@Override
	protected synchronized void append(ILoggingEvent eventObject) {
		outputStream.reset();
		
		try {
			encoder.doEncode(eventObject);
			lines.addLast(outputStream.toString());
		}
		catch (IOException e) {
			// as a fallback, formatted message is better than nothing
			lines.addLast(eventObject.getFormattedMessage());
		}
		
		while (lines.size() > maxSize) {
			lines.removeFirst();
		}
	}

	public static synchronized void writeList(PrintWriter printWriter) {
		for (String s : lines) {
			printWriter.print(s);
		}
	}
}
