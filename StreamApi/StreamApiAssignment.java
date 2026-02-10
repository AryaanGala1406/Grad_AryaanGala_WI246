import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Emp {
        String name;
        int age;
        String gender;
        int salary;
        String designation;
        String department;

        public Emp(String n, int a, String g, int s, String d, String dept) {
                name = n;
                age = a;
                gender = g;
                salary = s;
                designation = d;
                department = dept;
        }

        public String toString() {
                return "(" + name + ", " + age + ", " + gender + ", " + salary + ", " + designation + ", " + department
                                + ")";
        }
}

public class StreamApiAssignment {
        public static void main(String[] args) {

                List<Emp> empList = List.of(
                                new Emp("Aryaan", 21, "Male", 50000, "Developer", "IT"),
                                new Emp("Rohit", 25, "Male", 60000, "Tester", "QA"),
                                new Emp("Neha", 23, "Female", 55000, "Designer", "UI/UX"),
                                new Emp("Amit", 28, "Male", 70000, "Manager", "IT"),
                                new Emp("Pooja", 26, "Female", 65000, "HR Manager", "HR"),
                                new Emp("Sahil", 24, "Male", 48000, "Developer", "IT"),
                                new Emp("Sneha", 22, "Female", 45000, "Intern", "IT"),
                                new Emp("Rahul", 27, "Male", 68000, "Analyst", "Finance"),
                                new Emp("Isha", 23, "Female", 52000, "Developer", "IT"),
                                new Emp("Vikram", 31, "Male", 90000, "Senior Manager", "Operations"),
                                new Emp("Anjali", 25, "Female", 58000, "QA Engineer", "QA"),
                                new Emp("Nikhil", 28, "Male", 72000, "DevOps Engineer", "IT"),
                                new Emp("Riya", 24, "Female", 50000, "UI Designer", "UI/UX"),
                                new Emp("Aditya", 26, "Male", 67000, "Backend Developer", "IT"),
                                new Emp("Karan", 29, "Male", 75000, "Architect", "IT"),
                                new Emp("Meera", 30, "Female", 82000, "Manager", "Finance"),
                                new Emp("Suresh", 35, "Male", 95000, "Director", "Operations"),
                                new Emp("Naina", 27, "Female", 64000, "HR Executive", "HR"),
                                new Emp("Arjun", 32, "Male", 88000, "Project Manager", "IT"),
                                new Emp("Kavya", 29, "Female", 71000, "Business Analyst", "Finance"));

                // * Find the highest salary paid employee
                Emp highestPaid = empList.stream()
                                .max(Comparator.comparingInt(e -> e.salary))
                                .get();

                System.out.println("Highest Salary Employee: " + highestPaid);

                int highestSalary = empList.stream()
                                .max(Comparator.comparingInt(e -> e.salary))
                                .map(e -> e.salary)
                                .orElse(0);

                System.out.println("Highest Salary: " + highestSalary);
                System.out.println();

                // * Find how many male & female employees working in company (numbers)
                Map<Boolean, Long> genderPartition = empList.stream()
                                .collect(Collectors.partitioningBy(
                                                e -> e.gender.equalsIgnoreCase("Male"),
                                                Collectors.counting()));

                System.out.println("Male count: " + genderPartition.get(true));
                System.out.println("Female count: " + genderPartition.get(false));

                // * Total expense for the company department wise
                Map<String, Integer> m2 = empList.stream()
                                .collect(Collectors.groupingBy(e -> e.department,
                                                Collectors.summingInt(e -> e.salary)));
                System.out.println("Department wise company expense are: ");
                m2.forEach((department, sum) -> System.out.println(department + " = " + sum));
                System.out.println();

                // * Who is the top 5 senior employees in the company
                // Top 5 senior employees by age
                List<Emp> top5ByAge = empList.stream()
                                .sorted((a, b) -> Integer.compare(b.age, a.age)) // desc by age
                                .limit(5)
                                .collect(Collectors.toList());

                System.out.println(top5ByAge);
                System.out.println("Top 5 senior employees in the company are: ");
                top5ByAge.forEach(System.out::println);
                System.out.println();

                Predicate<Emp> p1 = (e) -> e.designation.equalsIgnoreCase("Manager");

                // * Find only the names who all are managers
                System.out.println("Names of Managers are: ");
                empList.stream()
                                .filter(p1)
                                .map(e -> e.name)
                                .forEach(System.out::println);
                System.out.println();

                // * Hike the salary by 20% for everyone except manager
                System.out.println("Updated Salaries (except Managers):");
                empList.stream()
                                .filter(p1.negate())
                                .forEach(e -> {
                                        int newSalary = (int) (e.salary * 1.20);
                                        System.out.println("(" + e.department + ")" + e.name + " -> " + newSalary);
                                });
                System.out.println();

                // * Find the total number of employees
                System.out.println("Total number of employees are " + empList.size());
                System.out.println();

        }
}
