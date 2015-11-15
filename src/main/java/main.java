import java.sql.*;

/**
 * Created by barcode on 2/11/15.
 */
public class main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String Query = "WITH answered_instances AS(\n" +
                "WITH anonymous_instances_answers AS (\n" +
                "WITH anonymous_instances AS(\n" +
                "SELECT \n" +
                "an.consecutive,\n" +
                "an.creationDate,\n" +
                "an.survey.`$oid` AS `survey`,\n" +
                "an.programLocation.`$oid` AS `location`,\n" +
                "FLATTEN(an.solution) AS `solution`\n" +
                "FROM mongo.GIPLatest.anonsurveyinstances an\n" +
                "WHERE an.`deleted` IS NOT TRUE AND an.`status` = 'complete'\n" +
                ")\n" +
                "SELECT \n" +
                "sv.description AS `survey`,\n" +
                "an.creationDate,\n" +
                "an.consecutive,\n" +
                "lc.location.`$oid` AS `location`,\n" +
                "an.solution.section.`$oid` AS `section`,\n" +
                "FLATTEN(an.solution.answers) AS `answer`\n" +
                "FROM anonymous_instances an,\n" +
                "mongo.GIPLatest.surveys sv,\n" +
                "mongo.GIPLatest.programlocations lc\n" +
                "WHERE lc.`_id`.`$oid` = an.location AND\n" +
                "sv.`_id`.`$oid` = an.survey\n" +
                ")\n" +
                "SELECT\n" +
                "aia.survey,\n" +
                "aia.creationDate,\n" +
                "aia.consecutive,\n" +
                "lst.`value` AS `location`,\n" +
                "ans.normalizedAnswer AS `answer`,\n" +
                "section.title AS `section`,\n" +
                "ans.question.`$oid` AS `question`,\n" +
                "qsorder.`order` AS `questionOrder`\n" +
                "FROM anonymous_instances_answers aia,\n" +
                "mongo.GIPLatest.`lists` lst,\n" +
                "mongo.GIPLatest.`answers` ans,\n" +
                "mongo.GIPLatest.`surveysections` section,\n" +
                "mongo.GIPLatest.`questionsectionorders` qsorder\n" +
                "WHERE \n" +
                "section.`_id`.`$oid` = aia.section AND\n" +
                "ans.`_id`.`$oid` = aia.answer.`$oid` AND\n" +
                "aia.location = lst.`_id`.`$oid` AND\n" +
                "qsorder.`question`.`$oid` = ans.question.`$oid` AND\n" +
                "qsorder.`section`.`$oid` = aia.section\n" +
                ")\n" +
                "SELECT\n" +
                "ai.survey,\n" +
                "ai.creationDate,\n" +
                "ai.consecutive,\n" +
                "ai.section,\n" +
                "ai.`location`,\n" +
                "ai.answer,\n" +
                "ai.questionOrder,\n" +
                "question.`title` AS `question`,\n" +
                "question.`type` AS `question_type`, \n" +
                "question.`allowedValues` AS `question_allowedValues`\n" +
                "FROM answered_instances ai,\n" +
                "mongo.GIPLatest.surveyquestions question\n" +
                "WHERE\n" +
                "ai.question = question.`_id`.`$oid`";
        Class.forName("org.apache.drill.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:drill:zk=localhost:2181/drill/drillbits1;schema=mongo");
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(Query);
        System.out.println("Read done, writing docs now");
        while (rs.next()) {
            Instance ins = new Instance(
                    rs.getString(1),
                    rs.getString(5),
                    rs.getString(4),
                    rs.getString(6),
                    rs.getString(8),
                    rs.getString(7),
                    rs.getInt(3),
                    rs.getTimestamp(2),
                    rs.getString(9),
                    rs.getString(10)
            );
            saveRecord(ins);
        }
        rs.close();
    }

    public static void saveRecord(Instance ins) throws SQLException {
        // create a mysql database connection
        String myDriver = "com.mysql.jdbc.Driver";
        String myUrl = "jdbc:mysql://localhost/SSO";
        Connection conn = null;

        try {
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "1235s2gip-");

            // the mysql insert statement
            String query = " insert into Driller (survey, creationDate, consecutive, section, location, answer, questionOrder,question, question_type, question_allowedValues)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, ins.getSurvey());
            preparedStmt.setDate(2, new java.sql.Date(ins.getCreationDate().getTime()));
            preparedStmt.setInt(3, ins.getConsecutive());
            preparedStmt.setString(4, ins.getSection());
            preparedStmt.setString(5, ins.getLocation());
            preparedStmt.setString(6, ins.getAnswer());
            preparedStmt.setString(7, ins.getQuestionOrder());
            preparedStmt.setString(8, ins.getQuestion());
            preparedStmt.setString(9, ins.getQuestion_type());
            preparedStmt.setString(10, ins.getQuestion_allowedValues());

            // execute the preparedstatement
            preparedStmt.execute();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            if(conn != null){
                conn.close();
            }
        }
    }
}
