package lk.joblk.Joblk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobDetailsDto {
    private int jobId;
    private String jobTitle;
    private String jobDescription;
    private String qualifications;
    private String jobClosingDate;
    private  String imgPath;

}
