package lk.joblk.Joblk.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_document")
public class JobDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_doc_id")
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
    private int jobId;

    private  String JobTitle ;
    private  String userid  ;






    public JobDocument(Integer id, String username, String qualifications, int number, String gender, String imagePath, String cvPath, String applyDate, String userEmail, int age, String address) {
        this.id = id;
        this.username = username;
        this.qualifications = qualifications;
        this.number = number;
        this.gender = gender;
        this.imagePath = imagePath;
        this.cvPath = cvPath;
        this.applyDate = applyDate;
        this.userEmail = userEmail;
        this.age = age;
        this.address = address;
    }


    public void setJobTitle(String jobTitle) {
        this.JobTitle = jobTitle;
    }
}
