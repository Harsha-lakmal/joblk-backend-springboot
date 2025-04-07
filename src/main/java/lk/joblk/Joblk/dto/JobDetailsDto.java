package lk.joblk.Joblk.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data

public class JobDetailsDto {
    private int jobId;
    private String jobTitle;
    private String jobDescription;
    private String qualifications;
    private String jobClosingDate;
    private  String imgPath;
    private String dateUpload ;
    private  String userId ;

    public JobDetailsDto(int jobId, String jobDescription, String jobClosingDate, String dateUpload, String imgPath, String qualifications, String jobTitle, String userId) {
        this.jobId = jobId;
        this.jobDescription = jobDescription;
        this.jobClosingDate = jobClosingDate;
        this.dateUpload = dateUpload;
        this.imgPath = imgPath;
        this.qualifications = qualifications;
        this.jobTitle = jobTitle;
        this.userId = userId;

    }

    public JobDetailsDto(int jobId, String jobTitle, String jobDescription, String qualifications, String jobClosingDate, String imgPath, String dateUpload) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.qualifications = qualifications;
        this.jobClosingDate = jobClosingDate;
        this.imgPath = imgPath;
        this.dateUpload = dateUpload;
    }
}
