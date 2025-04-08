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
    private  String UserEmail ;
    private  int number ;
    private  String address ;
    private  int jobId ;
    private  String userId   ;
    private  String jobTitle ;
}
