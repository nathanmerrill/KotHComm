package KoTHComm.loader;

public class Submission {
    private final String code, submissionName;
    private final boolean isJava;
    public Submission(String code, boolean isJava, String submissionName){
        this.code = code;
        this.isJava = isJava;
        this.submissionName = submissionName;
    }
}
