package model;

/**
 * This class is an item/line for a ListView. It makes it possible to put both BankInterestRequest and BankInterestReply object in one item in a ListView.
 */
public class ListViewLine {
	
	private TreatmentCostsRequest treatmentCostsRequest;
	private TreatmentCostsReply treatmentCostsReply;
	private HospitalCostsRequest hospitalCostsRequest;
	private HospitalCostsReply hospitalCostsReply;

	public TreatmentCostsRequest getTreatmentCostsRequest() {
		return treatmentCostsRequest;
	}

	public void setTreatmentCostsRequest(TreatmentCostsRequest treatmentCostsRequest) {
		this.treatmentCostsRequest = treatmentCostsRequest;
	}

	public TreatmentCostsReply getTreatmentCostsReply() {
		return treatmentCostsReply;
	}

	public void setTreatmentCostsReply(TreatmentCostsReply treatmentCostsReply) {
		this.treatmentCostsReply = treatmentCostsReply;
	}

	public HospitalCostsRequest getHospitalCostsRequest() {
		return hospitalCostsRequest;
	}

	public void setHospitalCostsRequest(HospitalCostsRequest hospitalCostsRequest) {
		this.hospitalCostsRequest = hospitalCostsRequest;
	}

	public HospitalCostsReply getHospitalCostsReply() {
		return hospitalCostsReply;
	}

	public void setHospitalCostsReply(HospitalCostsReply hospitalCostsReply) {
		this.hospitalCostsReply = hospitalCostsReply;
	}


	
	public ListViewLine(TreatmentCostsRequest treatmentCostsRequest) {
		setTreatmentCostsRequest(treatmentCostsRequest);
		setHospitalCostsReply(null);
		setTreatmentCostsReply(null);
		setHospitalCostsRequest(null);
	}

	public ListViewLine(HospitalCostsReply bankInterestReply) {
		setHospitalCostsRequest(null);
		setHospitalCostsReply(bankInterestReply);
		setTreatmentCostsReply(null);
		setTreatmentCostsRequest(null);

	}
	


	@Override
	public String toString() {
	   return treatmentCostsRequest.toString() + "  --->  " + ((hospitalCostsReply !=null)? hospitalCostsReply.toString():"waiting for bank reply...");
	}



}
