package org.icij.kaxxa.events;

public interface Notifiable {

	void notifyListeners();

	void notifyListeners(final Object arg);

	void hintRemaining(final int remaining);
}
