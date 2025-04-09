package lk.joblk.Joblk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcceptCourseDocumentDto {
    private  int id ;
    private int CourseDocumentId;
    private String username;
    private String qualifications;
    private int age;
    private String gender;
    private String applyDate;
    private String userEmail;
    private int number;
    private String address;
    private int courseId;
    private String userId;
    private String courseTitle;

    public AcceptCourseDocumentDto(int courseId, String courseTitle, int courseDocumentId, String userEmail, String userId, String address, String applyDate, int age, String gender, int id, String username, int number, String qualifications) {
    this.courseId = courseId;
    this.courseTitle = courseTitle;
    this.userId = userId;
    this.address = address;
    this.applyDate = applyDate;
    this.age = age;
    this.gender = gender;
    this.id = id;
    this.username = username;
    this.number = number;
    this.qualifications = qualifications;
    this.CourseDocumentId = courseDocumentId;
    this.userEmail = userEmail;
    

    }
}
