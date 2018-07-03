package io.j3solutions.boilerandroid.views.changehandlers;

public class RightChangeHandler extends HorizontalDirectionChangeHandler {
	public RightChangeHandler() {
		super(1);
	}

	public RightChangeHandler(long duration) {
		super(duration);
	}

	public RightChangeHandler(long duration, boolean removesFromViewOnPush) {
		super(duration, removesFromViewOnPush);
	}
}
