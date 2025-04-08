package lk.joblk.Joblk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "course_table")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseLocation;
    private String courseQualification;
    private String courseContent;
    private String courseStartDate;
    private String imgPath;
    private String dateUpload ;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    public Course(int courseId, String courseTitle, String courseDescription, String courseQualification, String courseStartDate, String courseContent, String imgPath, String courseLocation, String dateUpload) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseQualification = courseQualification;
        this.courseContent = courseContent;
        this.imgPath = imgPath;
        this.courseLocation = courseLocation;
        this.courseStartDate = courseStartDate;
        this.dateUpload = dateUpload;

    }


}
