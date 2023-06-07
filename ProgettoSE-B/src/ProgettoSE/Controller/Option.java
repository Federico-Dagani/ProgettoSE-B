package ProgettoSE.Controller;

public class Option {
    final private String voce;

    final private Handler handler;

    public Option(String voce, Handler handler) {
        this.voce = voce;
        this.handler = handler;
    }

    public String getVoce() {
        return voce;
    }

    public Handler getAction() {
        return handler;
    }
}
