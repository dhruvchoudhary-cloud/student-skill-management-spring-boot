package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@Valid @RequestBody Student student) {
        return service.addStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() { return service.getAllStudents(); }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable String id) { return service.getStudent(id); }

    @GetMapping("/search")
    public List<Student> search(@RequestParam(required = false) String id,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String course) {
        return service.search(id, email, course);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable String id, @Valid @RequestBody Student updates) {
        return service.updateStudent(id, updates);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable String id) { service.deleteStudent(id); }

    @GetMapping("/merit-list")
    public List<Student> meritList() { return service.getMeritList(); }

    @GetMapping("/same-skills")
    public Map<String, List<String>> sameSkills() { return service.studentsWithSameSkills(); }

    @GetMapping("/programming-support")
    public List<Student> programmingSupport() {
        return service.programmingNeedsImprovementMathAtLeast75();
    }
}
