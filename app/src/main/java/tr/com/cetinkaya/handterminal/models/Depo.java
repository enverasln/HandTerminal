package tr.com.cetinkaya.handterminal.models;

public class Depo {
    private int dep_no;
    private String dep_adi;

    public Depo() {

    }
    public Depo(int depo_no, String depo_adi) {
        this.dep_no = depo_no;
        this.dep_adi = depo_adi;
    }

    public int getDep_no() {
        return dep_no;
    }

    public void setDep_no(int dep_no) {
        this.dep_no = dep_no;
    }

    public String getDep_adi() {
        return dep_adi;
    }

    public void setDep_adi(String dep_adi) {
        this.dep_adi = dep_adi;
    }
}
