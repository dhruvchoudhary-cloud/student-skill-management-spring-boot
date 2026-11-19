# Student Profile and Skill Development Management System

This Spring Boot project implements the three questions in the CS336B MSE-II paper.

## Requirements
- JDK 17+
- IntelliJ IDEA
- MySQL Server
- Maven (IntelliJ can use the Maven wrapper/imported Maven)

## 1. Create the database
Open MySQL Workbench or MySQL command line and run:
```sql
CREATE DATABASE student_management;
```

## 2. Configure the password
Open `src/main/resources/application.properties` and replace `YOUR_MYSQL_PASSWORD` with your MySQL root password. If your MySQL username is not `root`, change that too.

## 3. Open in IntelliJ
1. Extract the ZIP.
2. IntelliJ -> File -> Open -> select the extracted `student-skill-management` folder.
3. Open `pom.xml` as a Maven project / click Load Maven Changes.
4. Set Project SDK to JDK 17 or later: File -> Project Structure -> Project SDK.
5. Wait for dependencies to download.
6. Run `StudentManagementApplication.java` using the green Run icon.
7. Console should show that Tomcat started on port 8080. Hibernate creates the tables automatically.

## 4. Test APIs
Use Postman or IntelliJ's HTTP Client. Base URL: `http://localhost:8080/api/students`

### Add a student (POST /api/students)
Header: `Content-Type: application/json`
```json
{
  "studentId": "STU001",
  "studentName": "Aisha Sharma",
  "email": "aisha@example.com",
  "courseName": "B.Tech CSE",
  "programmingScore": 92,
  "mathematicsScore": 86,
  "technicalSkills": ["Java", "SQL", "Spring Boot"]
}
```
Add another student:
```json
{
  "studentId": "STU002",
  "studentName": "Rahul Verma",
  "email": "rahul@example.com",
  "courseName": "B.Tech CSE",
  "programmingScore": 45,
  "mathematicsScore": 80,
  "technicalSkills": ["Python", "SQL"]
}
```
Add a third student:
```json
{
  "studentId": "STU003",
  "studentName": "Neha Singh",
  "email": "neha@example.com",
  "courseName": "B.Tech IT",
  "programmingScore": 78,
  "mathematicsScore": 82,
  "technicalSkills": ["Java", "SQL", "Spring Boot"]
}
```

### Other endpoints
| Method | URL | Purpose |
|---|---|---|
| POST | `/api/students` | Add student |
| GET | `/api/students` | Retrieve all |
| GET | `/api/students/STU001` | Find by ID |
| GET | `/api/students/search?email=aisha@example.com` | Search by email |
| GET | `/api/students/search?course=B.Tech%20CSE` | Search by course |
| PUT | `/api/students/STU001` | Update scores and skills |
| DELETE | `/api/students/STU001` | Delete |
| GET | `/api/students/merit-list` | Descending average |
| GET | `/api/students/same-skills` | Students sharing same complete skill set |
| GET | `/api/students/programming-support` | Programming < 50 and Maths >= 75 |

### Update example (PUT /api/students/STU001)
The body must include email, scores and skills:
```json
{
  "studentId": "STU001",
  "studentName": "Aisha Sharma",
  "email": "aisha@example.com",
  "courseName": "B.Tech CSE",
  "programmingScore": 95,
  "mathematicsScore": 90,
  "technicalSkills": ["Java", "SQL", "Spring Boot", "React"]
}
```

## 5. Q1 explanation
`Student` is an entity/class with private fields (encapsulation), a no-argument constructor for JPA, a parameterized constructor, getters/setters, and `toString`, `equals`, and `hashCode`. It also calculates average, category and stronger subject.

## 6. Q2 explanation
- Average = (Programming + Mathematics) / 2.
- Category uses the exact thresholds in the question.
- Stronger subject compares the two marks.
- Search uses repository methods.
- Merit list uses a Java `ArrayList` and `Comparator` sorting descending.
- Same skills groups students by a normalized, sorted `Set` of skills using a `HashMap`.
- Programming support filters programming < 50 and maths >= 75 using Stream API.
- Student ID is the primary key; email is unique; Java validation and setters enforce scores 0..100.
- Raw SQL examples are in `database/q2_queries.sql`.

## 7. Viva: Java Collections used
- `List<String>`: stores a student's technical skills.
- `ArrayList<Student>`: holds students while creating the merit list.
- `Comparator`: sorts by average score descending.
- `HashMap<Set<String>, List<String>>`: groups students with identical skill sets.
- `HashSet` / `TreeSet`: represents a unique normalized skill set for comparison.
- Stream API: filters students meeting the programming support condition.

## Important note
The API's `/same-skills` endpoint means students with the same complete set of skills, ignoring order and letter case. If your teacher means students who share at least one skill, the grouping logic would need a different interpretation.
