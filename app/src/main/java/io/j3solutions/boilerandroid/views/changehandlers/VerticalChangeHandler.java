package io.j3solutions.boilerandroid.views.changehandlers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link AnimatorChangeHandler} that will slide either slide a new View up or slide an old View down,
 * depending on whether a push or pop change is happening.
 */
public class VerticalChangeHandler extends AnimatorChangeHandler {

	public VerticalChangeHandler() {
	}

	public VerticalChangeHandler(boolean removesFromViewOnPush) {
		super(removesFromViewOnPush);
	}

	public VerticalChangeHandler(long duration) {
		super(duration);
	}

	public VerticalChangeHandler(long duration, boolean removesFromViewOnPush) {
		super(duration, removesFromViewOnPush);
	}

	@Override
	@NonNull
	protected Animator getAnimator(@NonNull ViewGroup container, @Nullable View from, @Nullable View to, boolean isPush, boolean toAddedToContainer) {
		AnimatorSet animatorSet = new AnimatorSet();

		if (!isPush) {
			if (from != null) {
				animatorSet.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_Y, -from.getHeight()));
			}
			if (to != null) {
				animatorSet.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, to.getHeight(), 0));
			}
		} else {
			if (from != null) {
				animatorSet.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_Y, from.getHeight()));
			}
			if (to != null) {
				// Allow this to have a nice transition when coming off an aborted push animation
				float fromLeft = from != null ? from.getTranslationY() : 0;
				animatorSet.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, fromLeft - to.getHeight(), 0));
			}
		}

		return animatorSet;
	}

	@Override
	protected void resetFromView(@NonNull View from) {
	}

	@Override
	@NonNull
	public ControllerChangeHandler copy() {
		return new VerticalChangeHandler(getAnimationDuration(), removesFromViewOnPush());
	}

}
