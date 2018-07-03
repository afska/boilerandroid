package io.j3solutions.boilerandroid.views.changehandlers;

public class LeftChangeHandler extends HorizontalDirectionChangeHandler {
	public LeftChangeHandler() {
		super(-1);
	}

	public LeftChangeHandler(long duration) {
		super(duration);
	}

	public LeftChangeHandler(long duration, boolean removesFromViewOnPush) {
		super(duration, removesFromViewOnPush);
	}
}
