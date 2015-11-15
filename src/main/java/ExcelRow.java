import java.util.Date;

/**
 * Created by barcode on 4/11/15.
 */
public class ExcelRow implements Cloneable {
    String section,
            location,
            question,
            answer,
            questionOrder,
            question_type;
    Double relation;
    Integer frequency;
    Date creationDate;


    public ExcelRow(String section, String location, String question, String answer, String questionOrder, Integer frequency, Date creationDate, String question_type, Double relation) {
        this.section = section;
        this.location = location;
        this.question = question;
        this.answer = answer;
        this.questionOrder = questionOrder;
        this.frequency = frequency;
        this.creationDate = creationDate;
        this.question_type = question_type;
        this.relation = relation;
    }

    protected ExcelRow clone() {
        try {
            return (ExcelRow) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(" Cloning not allowed. ");
            return this;
        }
    }


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(String questionOrder) {
        this.questionOrder = questionOrder;
    }

    public Integer getFrequency() {
        return frequency;
    }



    public ExcelRow() {
    }

    public String getQuestion_type() {

        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    @Override
    public String toString() {
        return "ExcelRow{" +
                "section='" + section + '\'' +
                ", location='" + location + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", questionOrder='" + questionOrder + '\'' +
                ", question_type='" + question_type + '\'' +
                ", relation=" + relation +
                ", frequency=" + frequency +
                ", creationDate=" + creationDate +
                '}';
    }

    public Double getRelation() {
        return relation;
    }

    public void setRelation(Double relation) {
        this.relation = relation;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
