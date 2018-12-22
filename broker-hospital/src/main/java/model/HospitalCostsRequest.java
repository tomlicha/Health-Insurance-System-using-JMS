package model;

public class HospitalCostsRequest {

    private int ssn; // social security number
    private int age;
    private String treatmentCode;
    private int transportDistance;

    public int getTransportDistance() {
        return transportDistance;
    }

    public void setTransportDistance(int transportDistance) {
        this.transportDistance = transportDistance;
    }

    public HospitalCostsRequest() {
        super();
    }

    public HospitalCostsRequest(int ssn, String treatmentCode, int age, int transportDistance) {
        super();
        this.ssn = ssn;
        this.treatmentCode = treatmentCode;
        this.age = age;
        this.transportDistance = transportDistance;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTreatmentCode() {
        return treatmentCode;
    }

    public void setTreatmentCode(String treatmentCode) {
        this.treatmentCode = treatmentCode;
    }

    @Override
    public String toString() {
        return "["+ssn + "]-[" +age+"]-["+ treatmentCode+"]";
    }

}
