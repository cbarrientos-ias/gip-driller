import java.sql.Timestamp;

/**
 * Created by barcode on 2/11/15.
 */
public class Instance {
    String survey,
            location,
            section,
            answer,
            question,
            questionOrder,
            question_type,
            question_allowedValues;
    Integer consecutive;
    Timestamp creationDate;

    public Instance(String survey, String location, String section, String answer, String question, String questionOrder, Integer consecutive, Timestamp creationDate, String question_type, String question_allowedValues) {
        this.survey = survey;
        this.location = location;
        this.section = section;
        this.answer = answer;
        this.question = question;
        this.questionOrder = questionOrder;
        this.question_type = question_type;
        this.question_allowedValues = question_allowedValues;
        this.consecutive = consecutive;
        this.creationDate = creationDate;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(String questionOrder) {
        this.questionOrder = questionOrder;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion_allowedValues() {
        return question_allowedValues;
    }

    public void setQuestion_allowedValues(String question_allowedValues) {
        this.question_allowedValues = question_allowedValues;
    }

    public Integer getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(Integer consecutive) {
        this.consecutive = consecutive;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}