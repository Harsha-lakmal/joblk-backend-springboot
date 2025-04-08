package lk.joblk.Joblk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CourseDocumentDto {
        private  Integer id;
        private String username;
        private  String qualifications ;
        private  int age  ;
        private  String gender;
        private  String imagePath ;
        private  String cvPath ;
        private  String applyDate  ;
        private  String UserEmail ;
        private  int number ;
        private  String address ;
        private  int courseId ;
        private  String userId   ;
        private  String courseTitle ;

    public CourseDocumentDto(int id, String username, String qualifications, int number, String gender, String imagePath, String cvPath, String applyDate, String userEmail, int age, String address) {

        this.id = id;
        this.username = username;
        this.qualifications = qualifications;
        this.age = age;
        this.gender = gender;
        this.imagePath = imagePath;
        this.cvPath = cvPath;
        this.applyDate = applyDate;
        this.UserEmail = userEmail;
        this.number = number;
        this.address = address;


    }

    public CourseDocumentDto(int courseId, String username, String qualifications, int age, String gender, String imagePath, String cvPath, String applyDate, int id, int number, String userEmail, String address, String userid, String courseTitle) {
    this.courseId = courseId;
    this.username = username;
    this.qualifications = qualifications;
    this.age = age;
    this.gender = gender;
    this.imagePath = imagePath;
    this.cvPath = cvPath;
    this.applyDate = applyDate;
    this.UserEmail = userEmail;
    this.number = number;
    this.userId = userid;
    this.courseTitle = courseTitle;
    this.id = id ;
    this.address = address;

    }


    public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


}
