package com.example.springboot.onlinejudge.submitCode.entity;

public class SubmissionStatus {
    private long submissionId;

    private String status;

    private String message;

    private String  sampleTestCaseResponse;

    public SubmissionStatus(long submissionId, String status, String message, String  sampleTestCaseResponse) {
        this.submissionId = submissionId;
        this.status = status;
        this.message = message;
        this.sampleTestCaseResponse = sampleTestCaseResponse;
    }

    public SubmissionStatus() {

    }

    public long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(long submissionId) {
        this.submissionId = submissionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSampleTestCaseResponse() {
        return sampleTestCaseResponse;
    }

    public void setSampleTestCaseResponse(String sampleTestCaseResponse) {
        this.sampleTestCaseResponse = sampleTestCaseResponse;
    }

    @Override
    public String toString() {
        return "SubmitResponse{" +
                "submissionId=" + submissionId +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", sampleTestCaseResponse=" + sampleTestCaseResponse +
                '}';
    }
}
