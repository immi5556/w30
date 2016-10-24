package within30.com.sidemenu.animation;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */
import android.view.animation.Interpolator;

public abstract class SupportAnimator {
    public SupportAnimator() {
    }

    public abstract boolean isNativeAnimator();

    public abstract Object get();

    public abstract void start();

    public abstract void setDuration(int var1);

    public abstract void setInterpolator(Interpolator var1);

    public abstract void addListener(SupportAnimator.AnimatorListener var1);

    public abstract boolean isRunning();

    public interface AnimatorListener {
        void onAnimationStart();

        void onAnimationEnd();

        void onAnimationCancel();

        void onAnimationRepeat();
    }
}

