package within30.com.sidemenu.animation;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */

import android.animation.Animator;
import android.annotation.TargetApi;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;

@TargetApi(11)
final class SupportAnimatorLollipop extends SupportAnimator {
    WeakReference<Animator> mNativeAnimator;

    SupportAnimatorLollipop(Animator animator) {
        this.mNativeAnimator = new WeakReference(animator);
    }

    public boolean isNativeAnimator() {
        return true;
    }

    public Object get() {
        return this.mNativeAnimator;
    }

    public void start() {
        Animator a = (Animator)this.mNativeAnimator.get();
        if(a != null) {
            a.start();
        }

    }

    public void setDuration(int duration) {
        Animator a = (Animator)this.mNativeAnimator.get();
        if(a != null) {
            a.setDuration((long)duration);
        }

    }

    public void setInterpolator(Interpolator value) {
        Animator a = (Animator)this.mNativeAnimator.get();
        if(a != null) {
            a.setInterpolator(value);
        }

    }

    public void addListener(final AnimatorListener listener) {
        Animator a = (Animator)this.mNativeAnimator.get();
        if(a != null) {
            if(listener == null) {
                a.addListener((android.animation.Animator.AnimatorListener)null);
            } else {
                a.addListener(new android.animation.Animator.AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                        listener.onAnimationStart();
                    }

                    public void onAnimationEnd(Animator animation) {
                        listener.onAnimationEnd();
                    }

                    public void onAnimationCancel(Animator animation) {
                        listener.onAnimationCancel();
                    }

                    public void onAnimationRepeat(Animator animation) {
                        listener.onAnimationRepeat();
                    }
                });
            }
        }
    }

    public boolean isRunning() {
        Animator a = (Animator)this.mNativeAnimator.get();
        return a != null && a.isRunning();
    }
}

