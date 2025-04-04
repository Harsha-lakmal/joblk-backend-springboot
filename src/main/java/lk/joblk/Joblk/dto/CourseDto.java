package lk.joblk.Joblk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CourseDto {
    private int courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseLocation;
    private String courseQualification;
    private String courseContent;
    private String courseStartDate;
    private String imgPath ;
    private String dateUpload ;
    private String userId;  // Add userId field




    public CourseDto(int courseId, String courseTitle, String courseTitle1, String courseTitle2, String courseTitle3) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseTitle1;
        this.courseLocation = courseTitle2;
        this.courseQualification = courseTitle3;

    }

    // Constructor
    public CourseDto(int courseId, String courseTitle, String courseDescription, String courseLocation,String courseQualification, String courseContent, String courseStartDate, String imgPath, String dateUpload, String userId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseLocation = courseLocation;
        this.courseQualification = courseQualification;
        this.courseContent = courseContent;
        this.courseStartDate = courseStartDate;
        this.imgPath = imgPath;
        this.dateUpload = dateUpload;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    }
