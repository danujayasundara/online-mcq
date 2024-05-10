package com.example.demo.dto;

import java.util.List;

public class ExamDtoRes {
	
	private long examId;
	private String examName;
    private int duration;
    private String dateTime;
    
    //private QuesDtoRes[] question;
    private List<QuesDtoRes> question;
    
	public long getExamId() {
		return examId;
	}
	public void setExamId(long examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	/*public QuesDtoRes[] getQuestion() {
		return question;
	}
	public void setQuestion(QuesDtoRes[] question) {
		this.question = question;
	}*/
	public List<QuesDtoRes> getQuestion() {
		return question;
	}
	public void setQuestion(List<QuesDtoRes> question) {
		this.question = question;
	}
	@Override
	public String toString() {
	    return "ExamDtoRes{" +
	            "examId=" + examId +
	            ", examName='" + examName + '\'' +
	            ", duration=" + duration +
	            ", dateTime=" + dateTime +
	            '}';
	}

}
