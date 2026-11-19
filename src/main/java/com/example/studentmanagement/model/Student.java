package com.example.studentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false, unique = true)
    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String studentName;

    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false)
    private String courseName;

    @NotNull @DecimalMin("0.0") @DecimalMax("100.0")
    private Double programmingScore;

    @NotNull @DecimalMin("0.0") @DecimalMax("100.0")
    private Double mathematicsScore;

    @ElementCollection
    @CollectionTable(name = "student_skills",
            joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill_name")
    private List<String> technicalSkills = new ArrayList<>();

    public Student() { }

    public Student(String studentId, String studentName, String email, String courseName,
                   Double programmingScore, Double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);
        setTechnicalSkills(technicalSkills);
    }

    public double getAverageScore() {
        return (programmingScore + mathematicsScore) / 2.0;
    }

    public String getPerformanceCategory() {
        double avg = getAverageScore();
        if (avg >= 85) return "Excellent";
        if (avg >= 70) return "Good";
        if (avg >= 50) return "Satisfactory";
        return "Needs Improvement";
    }

    public String getStrongerSubject() {
        int result = Double.compare(programmingScore, mathematicsScore);
        if (result > 0) return "Programming";
        if (result < 0) return "Mathematics";
        return "Both subjects are equal";
    }

    private void validateScore(Double score) {
        if (score == null || score < 0 || score > 100)
            throw new IllegalArgumentException("Scores must be between 0 and 100");
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(Double score) { validateScore(score); this.programmingScore = score; }
    public Double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(Double score) { validateScore(score); this.mathematicsScore = score; }
    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> skills) {
        this.technicalSkills = skills == null ? new ArrayList<>() : new ArrayList<>(skills);
    }

    @Override
    public String toString() {
        return "Student{studentId='" + studentId + "', studentName='" + studentName
                + "', email='" + email + "', courseName='" + courseName
                + "', average=" + (programmingScore == null || mathematicsScore == null ? "N/A" : getAverageScore())
                + ", skills=" + technicalSkills + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student other)) return false;
        return Objects.equals(studentId, other.studentId);
    }

    @Override
    public int hashCode() { return Objects.hash(studentId); }
}
