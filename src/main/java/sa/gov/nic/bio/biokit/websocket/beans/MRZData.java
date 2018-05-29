package sa.gov.nic.bio.biokit.websocket.beans;

public class MRZData
{
	private String MRZ;
	private String DocNo;
	private String DocType;
	private String Lastname;
	private String Firstname;
	private String Gender;
	private String Issuer;
	private String Nationality;
	private String DOB;
	private String DOE;
	private String OptionalData1;
	private String OptionalData2;
	
	public String getMRZ(){return MRZ;}
	public void setMRZ(String MRZ){this.MRZ = MRZ;}
	
	public String getDocNo(){return DocNo;}
	public void setDocNo(String docNo){ DocNo = docNo;}
	
	public String getDocType(){return DocType;}
	public void setDocType(String docType){ DocType = docType;}
	
	public String getLastname(){return Lastname;}
	public void setLastname(String lastname){ Lastname = lastname;}
	
	public String getFirstname(){return Firstname;}
	public void setFirstname(String firstname){ Firstname = firstname;}
	
	public String getGender(){return Gender;}
	public void setGender(String gender){ Gender = gender;}
	
	public String getIssuer(){return Issuer;}
	public void setIssuer(String issuer){ Issuer = issuer;}
	
	public String getNationality(){return Nationality;}
	public void setNationality(String nationality){ Nationality = nationality;}
	
	public String getDOB(){return DOB;}
	public void setDOB(String DOB){this.DOB = DOB;}
	
	public String getDOE(){return DOE;}
	public void setDOE(String DOE){this.DOE = DOE;}
	
	public String getOptionalData1(){return OptionalData1;}
	public void setOptionalData1(String optionalData1){ OptionalData1 = optionalData1;}
	
	public String getOptionalData2(){return OptionalData2;}
	public void setOptionalData2(String optionalData2){ OptionalData2 = optionalData2;}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		MRZData mrzData = (MRZData) o;
		
		if(MRZ != null ? !MRZ.equals(mrzData.MRZ) : mrzData.MRZ != null) return false;
		if(DocNo != null ? !DocNo.equals(mrzData.DocNo) : mrzData.DocNo != null) return false;
		if(DocType != null ? !DocType.equals(mrzData.DocType) : mrzData.DocType != null) return false;
		if(Lastname != null ? !Lastname.equals(mrzData.Lastname) : mrzData.Lastname != null) return false;
		if(Firstname != null ? !Firstname.equals(mrzData.Firstname) : mrzData.Firstname != null) return false;
		if(Gender != null ? !Gender.equals(mrzData.Gender) : mrzData.Gender != null) return false;
		if(Issuer != null ? !Issuer.equals(mrzData.Issuer) : mrzData.Issuer != null) return false;
		if(Nationality != null ? !Nationality.equals(mrzData.Nationality) : mrzData.Nationality != null) return false;
		if(DOB != null ? !DOB.equals(mrzData.DOB) : mrzData.DOB != null) return false;
		if(DOE != null ? !DOE.equals(mrzData.DOE) : mrzData.DOE != null) return false;
		if(OptionalData1 != null ? !OptionalData1.equals(mrzData.OptionalData1) : mrzData.OptionalData1 != null)
			return false;
		return OptionalData2 != null ? OptionalData2.equals(mrzData.OptionalData2) : mrzData.OptionalData2 == null;
	}
	
	@Override
	public int hashCode()
	{
		int result = MRZ != null ? MRZ.hashCode() : 0;
		result = 31 * result + (DocNo != null ? DocNo.hashCode() : 0);
		result = 31 * result + (DocType != null ? DocType.hashCode() : 0);
		result = 31 * result + (Lastname != null ? Lastname.hashCode() : 0);
		result = 31 * result + (Firstname != null ? Firstname.hashCode() : 0);
		result = 31 * result + (Gender != null ? Gender.hashCode() : 0);
		result = 31 * result + (Issuer != null ? Issuer.hashCode() : 0);
		result = 31 * result + (Nationality != null ? Nationality.hashCode() : 0);
		result = 31 * result + (DOB != null ? DOB.hashCode() : 0);
		result = 31 * result + (DOE != null ? DOE.hashCode() : 0);
		result = 31 * result + (OptionalData1 != null ? OptionalData1.hashCode() : 0);
		result = 31 * result + (OptionalData2 != null ? OptionalData2.hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString()
	{
		return "MRZData{" + "MRZ='" + MRZ + '\'' + ", DocNo='" + DocNo + '\'' + ", DocType='" + DocType + '\'' +
			   ", Lastname='" + Lastname + '\'' + ", Firstname='" + Firstname + '\'' + ", Gender='" + Gender + '\'' +
			   ", Issuer='" + Issuer + '\'' + ", Nationality='" + Nationality + '\'' + ", DOB='" + DOB + '\'' +
			   ", DOE='" + DOE + '\'' + ", OptionalData1='" + OptionalData1 + '\'' + ", OptionalData2='" +
			   OptionalData2 + '\'' + '}';
	}
}