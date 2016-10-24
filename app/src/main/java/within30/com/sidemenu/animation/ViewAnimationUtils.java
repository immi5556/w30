package within30.com.sidemenu.animation;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class ViewAnimationUtils {
    private static final boolean LOLLIPOP_PLUS;
    public static final int SCALE_UP_DURATION = 500;

    public ViewAnimationUtils() {
    }

    @TargetApi(21)
    public static SupportAnimator createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius) {
        if(LOLLIPOP_PLUS) {
            return new SupportAnimatorLollipop(android.view.ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius));
        } else if(!(view.getParent() instanceof RevealAnimator)) {
            throw new IllegalArgumentException("View must be inside RevealFrameLayout or RevealLinearLayout.");
        } else {
            RevealAnimator revealLayout = (RevealAnimator)view.getParent();
            revealLayout.setTarget(view);
            revealLayout.setCenter((float)centerX, (float)centerY);
            Rect bounds = new Rect();
            view.getHitRect(bounds);
            ObjectAnimator reveal = ObjectAnimator.ofFloat(revealLayout, "revealRadius", new float[]{startRadius, endRadius});
            reveal.addListener(getRevealFinishListener(revealLayout, bounds));
            return new SupportAnimatorPreL(reveal);
        }
    }

    static AnimatorListener getRevealFinishListener(RevealAnimator target, Rect bounds) {
        return (AnimatorListener)(VERSION.SDK_INT >= 17?new RevealAnimator.RevealFinishedJellyBeanMr1(target, bounds):(VERSION.SDK_INT >= 14?new RevealAnimator.RevealFinishedIceCreamSandwich(target, bounds):new RevealAnimator.RevealFinishedGingerbread(target, bounds)));
    }

    public static void liftingFromBottom(View view, float baseRotation, float fromY, int duration, int startDelay) {
        ViewHelper.setRotationX(view, baseRotation);
        ViewHelper.setTranslationY(view, fromY);
        ViewPropertyAnimator.animate(view).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration((long)duration).setStartDelay((long)startDelay).rotationX(0.0F).translationY(0.0F).start();
    }

    public static void liftingFromBottom(View view, float baseRotation, int duration, int startDelay) {
        ViewHelper.setRotationX(view, baseRotation);
        ViewHelper.setTranslationY(view, (float) (view.getHeight() / 3));
        ViewPropertyAnimator.animate(view).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration((long)duration).setStartDelay((long)startDelay).rotationX(0.0F).translationY(0.0F).start();
    }

    public static void liftingFromBottom(View view, float baseRotation, int duration) {
        ViewHelper.setRotationX(view, baseRotation);
        ViewHelper.setTranslationY(view, (float) (view.getHeight() / 3));
        ViewPropertyAnimator.animate(view).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration((long)duration).rotationX(0.0F).translationY(0.0F).start();
    }

    static {
        LOLLIPOP_PLUS = VERSION.SDK_INT >= 21;
    }

    public static class SimpleAnimationListener implements AnimatorListener {
        public SimpleAnimationListener() {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }
}

