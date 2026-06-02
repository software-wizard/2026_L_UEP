package pl.psi.ai;

/** Represents a decision to pass / do nothing. */
public class PassAction implements Action {
    // stateless
    public static final PassAction INSTANCE = new PassAction();
    private PassAction() {}
}

