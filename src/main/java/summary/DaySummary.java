package summary;

import java.time.LocalDate;

public class DaySummary {
    private LocalDate day;
    private int HAPPINESS;
    private int ANGER;
    private int SADNESS;
    private int FEAR;
    private int DISGUST;
    private int UNCLASSIFIED;

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public int getHAPPINESS() {
        return HAPPINESS;
    }

    public void setHAPPINESS(int HAPPINESS) {
        this.HAPPINESS = HAPPINESS;
    }

    public int getANGER() {
        return ANGER;
    }

    public void setANGER(int ANGER) {
        this.ANGER = ANGER;
    }

    public int getSADNESS() {
        return SADNESS;
    }

    public void setSADNESS(int SADNESS) {
        this.SADNESS = SADNESS;
    }

    public int getFEAR() {
        return FEAR;
    }

    public void setFEAR(int FEAR) {
        this.FEAR = FEAR;
    }

    public int getDISGUST() {
        return DISGUST;
    }

    public void setDISGUST(int DISGUST) {
        this.DISGUST = DISGUST;
    }

    public int getUNCLASSIFIED() {
        return UNCLASSIFIED;
    }

    public void setUNCLASSIFIED(int UNCLASSIFIED) {
        this.UNCLASSIFIED = UNCLASSIFIED;
    }
}
