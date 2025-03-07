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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
