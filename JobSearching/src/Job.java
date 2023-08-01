import java.sql.*;

public class Job extends BaseEntity {
    private int jobId;
    private int employerId;
    private String title;
    private String description;
    private String requirements;
    private String location;

    public Job(int jobId, int employerId, String title, String description, String requirements, String location) {
        this.jobId = jobId;
        this.employerId = employerId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.location = location;
    }
    
    public Job()
    {
    	
    }

    public int getJobId() {
        return jobId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getLocation() {
        return location;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public static Job buildJob(int employerId, String title, String description, String requirements, String location) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Job (EmployerID, Title, Description, Requirements, Location) VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, employerId);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, requirements);
            statement.setString(5, location);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Failed to create job. No rows affected.");
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int jobId = generatedKeys.getInt(1);
                    return new Job(jobId, employerId, title, description, requirements, location);
                } else {
                    System.out.println("Failed to create job. No ID obtained.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to create job. Error: " + e.getMessage());
            return null;
        }
    }

    public void updateJob(int employerId, String title, String description, String requirements, String location) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Job SET EmployerID = ?, Title = ?, Description = ?, Requirements = ?, Location = ? WHERE JobID = ?")) {

            statement.setInt(1, employerId);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, requirements);
            statement.setString(5, location);
            statement.setInt(6, jobId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Failed to update job. No rows affected.");
            } else {
                this.employerId = employerId;
                this.title = title;
                this.description = description;
                this.requirements = requirements;
                this.location = location;
                System.out.println("Job updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update job. Error: " + e.getMessage());
        }
    }

    public static void deleteJob(int jobId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Job WHERE JobID = ?")) {

            statement.setInt(1, jobId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Failed to delete job. No rows affected.");
            } else {
                System.out.println("Job deleted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete job. Error: " + e.getMessage());
        }
    }

    public static Job getJobById(int jobId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Job WHERE JobID = ?")) {

            statement.setInt(1, jobId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int employerId = resultSet.getInt("EmployerID");
                    String title = resultSet.getString("Title");
                    String description = resultSet.getString("Description");
                    String requirements = resultSet.getString("Requirements");
                    String location = resultSet.getString("Location");

                    return new Job(jobId, employerId, title, description, requirements, location);
                } else {
                    System.out.println("Job with JobID " + jobId + " not found.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch job information. Error: " + e.getMessage());
            return null;
        }
    }
    @Override
    public int getNumberOfRecords() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Job")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch job count. Error: " + e.getMessage());
        }
        return 0; // Return 0 in case of an error or no records found
    }
    
    public static String getJobWithEmployerName(int jobId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT  e.Name AS EmployerName " +
                     "FROM Job j " +
                     "JOIN User e ON j.EmployerID = e.UserId " +
                     "WHERE j.JobID = ?")) {

            statement.setInt(1, jobId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String employerName = resultSet.getString("EmployerName");
                    return employerName;
                } else {
                    System.out.println("Job with JobID " + jobId + " not found.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch job information. Error: " + e.getMessage());
            return null;
        }
    }
}
