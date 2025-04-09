package lk.joblk.Joblk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcceptJobDocumentDto {
    private  int id ;
    private int jobDocumentId;
    private String username;
    private  String qualifications ;
    private  int age  ;
    private  String gender;
    private  String applyDate  ;
    private  String userEmail ;
    private  int number ;
    private  String address ;
    private  int jobId ;
    private  String userId   ;
    private  String jobTitle ;

    public AcceptJobDocumentDto(int jobId, String jobTitle, int jobDocumentId, String userId, String address, String applyDate, int jobId1, int age, String gender, int id, String username, int number, String qualifications) {
    this.jobDocumentId = jobDocumentId;
    this.username = username;
    this.qualifications = qualifications;
    this.jobId = jobId;
    this.userId = userId;
    this.address = address;
    this.applyDate = applyDate;
    this.age = age;
    this.gender = gender;
    this.id = id;

    this.number = number;
    this.jobTitle = jobTitle;




    }

    public AcceptJobDocumentDto(int jobId, String jobTitle, int jobDocumentId, String userEmail, String userId, String address, String applyDate, int age, String gender, int id, String username, int number, String qualifications) {
    this.jobDocumentId = jobDocumentId;
    this.userEmail = userEmail;
    this.userId = userId;
    this.address = address;
    this.applyDate = applyDate;
    this.age = age;
    this.gender = gender;
    this.id = id;
    this.number = number;
    this.jobTitle = jobTitle;
    this.jobId = jobId;
    this.username = username;
    this.qualifications = qualifications;
    }
}
