package within30.com.sidemenu.animation;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */

import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;

import java.lang.ref.WeakReference;

final class SupportAnimatorPreL extends SupportAnimator {
    WeakReference<Animator> mSupportFramework;

    SupportAnimatorPreL(Animator animator) {
        this.mSupportFramework = new WeakReference(animator);
    }

    public boolean isNativeAnimator() {
        return false;
    }

    public Object get() {
        return this.mSupportFramework.get();
    }

    public void start() {
        Animator a = (Animator)this.mSupportFramework.get();
        if(a != null) {
            a.start();
        }

    }

    public void setDuration(int duration) {
        Animator a = (Animator)this.mSupportFramework.get();
        if(a != null) {
            a.setDuration((long)duration);
        }

    }

    public void setInterpolator(Interpolator value) {
        Animator a = (Animator)this.mSupportFramework.get();
        if(a != null) {
            a.setInterpolator(value);
        }

    }

    public void addListener(final AnimatorListener listener) {
        Animator a = (Animator)this.mSupportFramework.get();
        if(a != null) {
            if(listener == null) {
                a.addListener((com.nineoldandroids.animation.Animator.AnimatorListener)null);
            } else {
                a.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
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
        Animator a = (Animator)this.mSupportFramework.get();
        return a != null && a.isRunning();
    }
}

