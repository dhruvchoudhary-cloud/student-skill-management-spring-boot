package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        if (repository.existsById(student.getStudentId()))
            throw new IllegalArgumentException("A student with this ID already exists");
        if (repository.existsByEmailIgnoreCase(student.getEmail()))
            throw new IllegalArgumentException("A student with this email already exists");
        return repository.save(student);
    }

    public List<Student> getAllStudents() { return repository.findAll(); }

    public Student getStudent(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + id));
    }

    @Transactional
    public Student updateStudent(String id, Student updates) {
        Student existing = getStudent(id);
        if (!existing.getEmail().equalsIgnoreCase(updates.getEmail())
                && repository.existsByEmailIgnoreCase(updates.getEmail()))
            throw new IllegalArgumentException("Email is already used by another student");
        existing.setProgrammingScore(updates.getProgrammingScore());
        existing.setMathematicsScore(updates.getMathematicsScore());
        existing.setTechnicalSkills(updates.getTechnicalSkills());
        existing.setEmail(updates.getEmail());
        return repository.save(existing);
    }

    public void deleteStudent(String id) {
        Student student = getStudent(id);
        repository.delete(student);
    }

    public List<Student> getMeritList() {
        List<Student> students = new ArrayList<>(repository.findAll());
        students.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        return students;
    }

    public List<Student> search(String id, String email, String course) {
        if (id != null && !id.isBlank()) return List.of(getStudent(id));
        if (email != null && !email.isBlank())
            return repository.findByEmailIgnoreCase(email).map(List::of).orElse(List.of());
        if (course != null && !course.isBlank()) return repository.findByCourseNameIgnoreCase(course);
        return getAllStudents();
    }

    public Map<String, List<String>> studentsWithSameSkills() {
        Map<Set<String>, List<String>> grouped = new HashMap<>();
        for (Student s : repository.findAll()) {
            Set<String> normalized = s.getTechnicalSkills().stream()
                    .map(x -> x.trim().toLowerCase()).collect(Collectors.toCollection(TreeSet::new));
            grouped.computeIfAbsent(normalized, k -> new ArrayList<>()).add(s.getStudentId());
        }
        Map<String, List<String>> result = new LinkedHashMap<>();
        grouped.forEach((skills, ids) -> {
            if (ids.size() > 1) result.put(String.join(", ", skills), ids);
        });
        return result;
    }

    public List<Student> programmingNeedsImprovementMathAtLeast75() {
        return repository.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50 && s.getMathematicsScore() >= 75)
                .collect(Collectors.toList());
    }
}
