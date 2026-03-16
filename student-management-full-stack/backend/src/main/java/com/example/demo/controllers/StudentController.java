package com.example.demo.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Student;
import com.example.demo.enums.Gender;
import com.example.demo.repo.StudentRepo;

@RestController
@CrossOrigin("http://localhost:4200")
public class StudentController {

    @Autowired
    StudentRepo repo;

    // GET /students
    @GetMapping("/students")
    public List<Student> getStudents() {
        return repo.findAll();
    }

    // GET /students/{regNo}
    @GetMapping("/students/{regNo}")
    public Student getStudentByRegNo(@PathVariable Integer regNo) {
        return repo.findById(regNo).orElse(null);
    }

    // POST /students
    @PostMapping("/students")
    public Student insertStudent(@RequestBody Student s) {
        return repo.save(s);
    }

    // PUT /students/{regNo}
    @PutMapping("/students/{regNo}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer regNo, @RequestBody Student s) {

        if (!repo.existsById(regNo))
            return ResponseEntity.notFound().build();

        s.setRegNo(regNo);

        return ResponseEntity.ok(repo.save(s));
    }

    // PATCH /students/{regNo}
    @PatchMapping("/students/{regNo}")
    public Student patchStudent(@PathVariable Integer regNo, @RequestBody Student updated) {

        Student student = repo.findById(regNo).orElse(null);

        if (student == null)
            return null;

        if (updated.getName() != null)
            student.setName(updated.getName());

        if (updated.getSchool() != null)
            student.setSchool(updated.getSchool());

        if (updated.getGender() != null)
            student.setGender(updated.getGender());

        if (updated.getStandard() != 0)
            student.setStandard(updated.getStandard());

        if (updated.getPercentage() != 0)
            student.setPercentage(updated.getPercentage());

        return repo.save(student);
    }

    // DELETE /students/{regNo}
    @DeleteMapping("/students/{regNo}")
    public void removeStudent(@PathVariable Integer regNo) {
        repo.deleteById(regNo);
    }

    // GET /students/school?name=KV
    @GetMapping("/students/school")
    public List<Student> getStudentsBySchool(@RequestParam("name") String school) {

        return repo.findAll()
                .stream()
                .filter(s -> s.getSchool().equalsIgnoreCase(school))
                .toList();
    }

    // GET /students/school/count?name=DPS
    @GetMapping("/students/school/count")
    public Map<String, Long> getNoOfStudentsBySchool(@RequestParam("name") String school) {

        long count = repo.findAll()
                .stream()
                .filter(s -> s.getSchool().equalsIgnoreCase(school))
                .count();

        return Map.of("count", count);
    }

    // GET /students/school/standard/count?class=5
    @GetMapping("/students/school/standard/count")
    public Map<String, Long> getNoOfStudentsInStandard(@RequestParam("class") int standard) {

        long count = repo.findAll()
                .stream()
                .filter(s -> s.getStandard() == standard)
                .count();

        return Map.of("count", count);
    }

    // GET /students/result?pass=true
    @GetMapping("/students/result")
    public List<Student> getStudentByResult(@RequestParam("pass") boolean pass) {

        if (pass) {
            return repo.findAll()
                    .stream()
                    .filter(s -> s.getPercentage() >= 40)
                    .sorted((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()))
                    .toList();
        } else {
            return repo.findAll()
                    .stream()
                    .filter(s -> s.getPercentage() < 40)
                    .sorted((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()))
                    .toList();
        }
    }

    // GET /students/strength?gender=MALE&standard=5
    @GetMapping("/students/strength")
    public Map<String, Long> getNoOfStudentsByGenderAndStandard(
            @RequestParam("gender") String gender,
            @RequestParam("standard") int standard) {

        Gender g = Gender.valueOf(gender.toUpperCase());

        long count = repo.findAll()
                .stream()
                .filter(s -> s.getGender() == g)
                .filter(s -> s.getStandard() == standard)
                .count();

        return Map.of("count", count);
    }
}