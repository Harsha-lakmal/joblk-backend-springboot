package lk.joblk.Joblk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
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

    public CourseDto(int courseId) {
        this.courseId = courseId;
    }

    public CourseDto(int courseId, String courseTitle, String courseTitle1, String courseTitle2, String courseTitle3) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseTitle1;
        this.courseLocation = courseTitle2;
        this.courseQualification = courseTitle3;

    }
}
