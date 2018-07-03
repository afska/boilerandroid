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

public class HorizontalDirectionChangeHandler extends AnimatorChangeHandler {
	private int direction = 1;

	public HorizontalDirectionChangeHandler(int direction) {
		this.direction = direction;
	}

	public HorizontalDirectionChangeHandler(boolean removesFromViewOnPush) {
		super(removesFromViewOnPush);
	}

	public HorizontalDirectionChangeHandler(long duration) {
		super(duration);
	}

	public HorizontalDirectionChangeHandler(long duration, boolean removesFromViewOnPush) {
		super(duration, removesFromViewOnPush);
	}

	@NonNull
	protected Animator getAnimator(@NonNull ViewGroup container, @Nullable View from, @Nullable View to, boolean isPush, boolean toAddedToContainer) {
		AnimatorSet animatorSet = new AnimatorSet();
		if(isPush) {
			if(from != null) {
				animatorSet.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, new float[]{(float)(-from.getWidth() * direction)}));
			}

			if(to != null) {
				animatorSet.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, new float[]{(float)to.getWidth() * direction, 0.0F}));
			}
		} else {
			if(from != null) {
				animatorSet.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, new float[]{(float)from.getWidth() * direction}));
			}

			if(to != null) {
				float fromLeft = from != null?from.getTranslationX():0.0F;
				animatorSet.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, new float[]{fromLeft - (float)to.getWidth() * direction, 0.0F}));
			}
		}

		return animatorSet;
	}

	protected void resetFromView(@NonNull View from) {
		from.setTranslationX(0.0F);
	}

	@NonNull
	public ControllerChangeHandler copy() {
		return new com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler(this.getAnimationDuration(), this.removesFromViewOnPush());
	}
}

