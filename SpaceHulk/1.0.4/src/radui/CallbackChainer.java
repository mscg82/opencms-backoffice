package radui;

/**
 *
 * @author Giuseppe Miscione
 */
public class CallbackChainer implements Callback {

    private Callback callbacks[];

    public CallbackChainer(Callback callback1, Callback callback2) {
        this.callbacks = new Callback[] {callback1, callback2};
    }

    public CallbackChainer(Callback callbacks[]) {
        this.callbacks = callbacks;
    }

    public void perform() {
        for(int i = 0, l = callbacks.length; i < l; i++) {
            if(callbacks[i] != null) {
                callbacks[i].perform();
            }
        }
    }

}
