import java.util.Comparator;

public class Attainment implements Comparable<Attainment> {
    private final String courseCode;
    private final String studentNumber;
    private final int grade;

    Attainment(String courseCode, String studentNumber, int grade) {
        this.courseCode = courseCode;
        this.studentNumber = studentNumber;
        this.grade = grade;
    }

    public String getCourseCode() {
        return courseCode;
    }
    public String getStudentNumber() {
        return studentNumber;
    }
    public int getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", courseCode, studentNumber, grade);
    }

    public static final Comparator<Attainment> CODE_STUDENT_CMP = new Comparator<Attainment>() {
        @Override
        public int compare(Attainment a1, Attainment a2) {
            int cmp = a1.getCourseCode().compareTo(a2.getCourseCode());
            if(cmp == 0) {
                cmp = a1.getStudentNumber().compareTo(a2.getStudentNumber());
            }
            return cmp;
        }
    };

    public static final Comparator<Attainment> CODE_GRADE_CMP = new Comparator<Attainment>() {
        @Override
        public int compare(Attainment a1, Attainment a2) {
            int cmp = a1.getCourseCode().compareTo(a2.getCourseCode());
            if(cmp == 0) {
                cmp = Integer.compare(a2.getGrade(), a1.getGrade());
            }
            return cmp;
        }
    };

    @Override
    public int compareTo(Attainment other) {
        int cmp = studentNumber.compareTo(other.studentNumber);
        if(cmp == 0) {
            cmp = courseCode.compareTo(other.courseCode);
        }
        return cmp;
    }
}
