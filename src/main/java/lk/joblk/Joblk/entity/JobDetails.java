package lk.joblk.Joblk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "job_details_table")
public class JobDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;

    private String jobTitle;
    private String jobDescription;
    private String qualifications;
    private String jobClosingDate;
    private String imgPath;
    private String dateUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToMany(mappedBy = "appliedJobs")
//    private List<JobDocument> applicants;
//
//    @OneToMany(mappedBy = "jobDetails", cascade = CascadeType.ALL)
//    private List<JobDocument> jobDocuments = new ArrayList<> ();


    public JobDetails(int jobId, String jobDescription, String jobTitle, String qualifications, String jobClosingDate, String imgPath, String dateUpload) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.qualifications = qualifications;
        this.jobClosingDate = jobClosingDate;
        this.imgPath = imgPath;
        this.dateUpload = dateUpload;
        this.user = new User();
    }
}
