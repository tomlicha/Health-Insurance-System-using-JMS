package model;


import com.owlike.genson.Genson;

public class TreatmentSerializer {

    Genson genson = new Genson();

    public String RequestToString(TreatmentCostsRequest treatmentCostsRequest){

        return genson.serialize(treatmentCostsRequest);
    }

    public TreatmentCostsRequest requestFromString(String str){

        return genson.deserialize(str,TreatmentCostsRequest.class);
    }

    public String replyToString(TreatmentCostsReply treatmentCostsReply){

        return genson.serialize(treatmentCostsReply);
    }

    public TreatmentCostsReply replyFromString(String str){
        return genson.deserialize(str,TreatmentCostsReply.class);
    }
}
