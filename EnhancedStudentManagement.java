import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    int id;
    String name;
    String course;
    HashMap<String, Integer> subjects = new HashMap<>();
    double gpa;

    Student(int id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    void calculateGPA() {
        if (subjects.size() == 0) {
            gpa = 0.0;
            return;
        }
        double total = 0;
        for (int marks : subjects.values()) {
            total += marks;
        }
        this.gpa = total / subjects.size() / 10.0;
    }

    public String toString() {
        return id + " | " + name + " | " + course + " | GPA: " + String.format("%.2f", gpa);
    }

    String toCSV() {
        return id + "," + name + "," + course + "," + gpa;
    }
}

public class StudentManagement {
    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        loadFromFile();

        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Delete Student by ID");
            System.out.println("5. Sort Students by Name");
            System.out.println("6. Export to CSV");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> searchStudent();
                case 4 -> deleteStudent();
                case 5 -> sortStudents();
                case 6 -> exportToCSV();
                case 7 -> {
                    saveToFile();
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void addStudent() {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Course: ");
        String course = sc.nextLine();

        Student s = new Student(id, name, course);

        System.out.print("Enter number of subjects: ");
        int n = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.print("Subject name: ");
            String sub = sc.nextLine();
            System.out.print("Marks (out of 100): ");
            int marks = sc.nextInt();
            sc.nextLine();
            s.subjects.put(sub, marks);
        }

        s.calculateGPA();
        students.add(s);
        System.out.println("Student added successfully!");
    }

    static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    static void searchStudent() {
        System.out.print("Enter ID to search: ");
        int id = sc.nextInt();
        for (Student s : students) {
            if (s.id == id) {
                System.out.println("Student found: " + s);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    static void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();
        boolean removed = students.removeIf(s -> s.id == id);
        System.out.println(removed ? "Student deleted." : "ID not found.");
    }

    static void sortStudents() {
        students.sort(Comparator.comparing(s -> s.name));
        System.out.println("Students sorted by name.");
    }

    static void exportToCSV() {
        try (PrintWriter pw = new PrintWriter("students_export.csv")) {
            pw.println("ID,Name,Course,GPA");
            for (Student s : students) {
                pw.println(s.toCSV());
            }
            System.out.println("Exported to students_export.csv");
        } catch (IOException e) {
            System.out.println("Error exporting to CSV.");
        }
    }

    static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(students);
        } catch (IOException e) {
            System.out.println("Failed to save data.");
        }
    }

    @SuppressWarnings("unchecked")
    static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) in.readObject();
        } catch (Exception e) {
            System.out.println("Failed to load previous data: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}
