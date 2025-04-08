package lk.joblk.Joblk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name =  "Accept_Job_document")
public class AcceptJobDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
