package within30.com.sidemenu.animation;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import java.lang.ref.WeakReference;

public interface RevealAnimator {
    void setClipOutlines(boolean var1);

    void setCenter(float var1, float var2);

    void setTarget(View var1);

    void setRevealRadius(float var1);

    float getRevealRadius();

    void invalidate(Rect var1);

    public static class RevealFinishedJellyBeanMr1 extends ViewAnimationUtils.SimpleAnimationListener {
        WeakReference<RevealAnimator> mReference;
        volatile Rect mInvalidateBounds;
        int mLayerType;

        @TargetApi(11)
        RevealFinishedJellyBeanMr1(RevealAnimator target, Rect bounds) {
            this.mReference = new WeakReference(target);
            this.mInvalidateBounds = bounds;
            this.mLayerType = ((View)target).getLayerType();
        }

        @TargetApi(11)
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            ((View)this.mReference.get()).setLayerType(2, (Paint)null);
        }

        @TargetApi(11)
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            ((View)this.mReference.get()).setLayerType(this.mLayerType, (Paint)null);
            RevealAnimator target = (RevealAnimator)this.mReference.get();
            if(target != null) {
                target.setClipOutlines(false);
                target.setCenter(0.0F, 0.0F);
                target.setTarget((View)null);
                target.invalidate(this.mInvalidateBounds);
            }
        }
    }

    public static class RevealFinishedIceCreamSandwich extends ViewAnimationUtils.SimpleAnimationListener {
        WeakReference<RevealAnimator> mReference;
        volatile Rect mInvalidateBounds;
        int mLayerType;

        @TargetApi(11)
        RevealFinishedIceCreamSandwich(RevealAnimator target, Rect bounds) {
            this.mReference = new WeakReference(target);
            this.mInvalidateBounds = bounds;
            this.mLayerType = ((View)target).getLayerType();
        }

        @TargetApi(11)
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            ((View)this.mReference.get()).setLayerType(1, (Paint)null);
        }

        @TargetApi(11)
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            ((View)this.mReference.get()).setLayerType(this.mLayerType, (Paint)null);
            RevealAnimator target = (RevealAnimator)this.mReference.get();
            if(target != null) {
                target.setClipOutlines(false);
                target.setCenter(0.0F, 0.0F);
                target.setTarget((View)null);
                target.invalidate(this.mInvalidateBounds);
            }
        }
    }

    public static class RevealFinishedGingerbread extends ViewAnimationUtils.SimpleAnimationListener {
        WeakReference<RevealAnimator> mReference;
        volatile Rect mInvalidateBounds;

        RevealFinishedGingerbread(RevealAnimator target, Rect bounds) {
            this.mReference = new WeakReference(target);
            this.mInvalidateBounds = bounds;
        }

        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            RevealAnimator target = (RevealAnimator)this.mReference.get();
            if(target != null) {
                target.setClipOutlines(false);
                target.setCenter(0.0F, 0.0F);
                target.setTarget((View)null);
                target.invalidate(this.mInvalidateBounds);
            }
        }
    }
}

