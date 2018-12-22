package model;

import java.util.ArrayList;
import java.util.List;

public class HospitalReplyAggregate {

    private int numberRequestSent;
    List<HospitalCostsReply> listBankReply = new ArrayList<>();

    public void addReply(HospitalCostsReply hospitalCostsReply){
        listBankReply.add(hospitalCostsReply);
    }

    public int getNumberRequestSent() {
        return numberRequestSent;
    }

    public void setNumberRequestSent(int numberRequestSent) {
        this.numberRequestSent = numberRequestSent;
    }

    public List<HospitalCostsReply> getListBankReply() {
        return listBankReply;
    }

    public void setListBankReply(List<HospitalCostsReply> listHospitalReply) {
        this.listBankReply = listHospitalReply;
    }

    public boolean isFinished(){
        if (numberRequestSent == listBankReply.size()) return true;
        else return false;
    }

    public HospitalCostsReply getBest(){
        HospitalCostsReply minInterest = listBankReply.get(0);
        for(int i=1;i<listBankReply.size();i++){

            if (listBankReply.get(i).getPrice() < minInterest.getPrice()){
                minInterest = listBankReply.get(i);
            }
        }
        return minInterest;
    }
}
