package lk.joblk.Joblk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "AcceptCourseDocumentTable")
public class AcceptCourseDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id ;
    private int courseDocumentId;
    private String username;
    private  String qualifications ;
    private  int age  ;
    private  String gender;
    private  String applyDate  ;
    private  String UserEmail ;
    private  int number ;
    private  String address ;
    private  int courseId ;
    private  String userId   ;
    private  String courseTitle ;


}
