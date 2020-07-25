package presentation.demo.models.viewmodels;

public class UserControlViewModel {

    private String firstName;
    private String lastName;
    private String username;
    private String docName;
    private String nurseName;
    private String nurseNum;
    private String practice;

    public UserControlViewModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getNurseNum() {
        return nurseNum;
    }

    public void setNurseNum(String nurseNum) {
        this.nurseNum = nurseNum;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }
}
