package lk.joblk.Joblk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name =  "Course_document")
public class CourseDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_doc_id")
    private int id;
    @Column(nullable = false)
    private String username;
    private String qualifications;
    private int age;
    private String gender;
    private String imagePath;
    private String cvPath;
    private String applyDate;
    private String userEmail;
    private int number;
    private String address;
    private int courseId;
    private String courseTitle;
    private String userid;








}
