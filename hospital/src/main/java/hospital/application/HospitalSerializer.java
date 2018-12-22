package hospital.application;

import com.owlike.genson.Genson;
import hospital.model.HospitalCostsReply;
import hospital.model.HospitalCostsRequest;

public class HospitalSerializer {

    Genson genson = new Genson();

    public String RequestToString(HospitalCostsRequest hospitalCostsRequest)
    {
        return genson.serialize(hospitalCostsRequest);

    }

    public HospitalCostsRequest requestFromString(String str){
        return genson.deserialize(str,HospitalCostsRequest.class);

    }

    public String replyToString(HospitalCostsReply hospitalCostsReply){
        return genson.serialize(hospitalCostsReply);

    }

    public HospitalCostsReply replyFromString(String str){
        return genson.deserialize(str,HospitalCostsReply.class);
    }
}
