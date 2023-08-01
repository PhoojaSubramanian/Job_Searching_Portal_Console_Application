import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Application extends BaseEntity {
    public enum Status {
        APPLIED,
        IN_REVIEW,
        REJECTED,
        ACCEPTED
    }

    private int applicationId;
    private int userId;
    private int jobId;
    private Status status;

    public Application(int applicationId, int userId, int jobId, Status status) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
    }
    public Application()
    {
    	
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getJobId() {
        return jobId;
    }

    public Status getStatus() {
        return status;
    }
    

    public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static Application buildApplication(int userId, int jobId, Status status) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Application (UserID, JobID, Status) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);
            statement.setInt(2, jobId);
            statement.setString(3, status.toString().toLowerCase());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Failed to create application. No rows affected.");
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int applicationId = generatedKeys.getInt(1);
                    return new Application(applicationId, userId, jobId, status);
                } else {
                    System.out.println("Failed to create application. No ID obtained.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to create application. Error: " + e.getMessage());
            return null;
        }
    }

    public void updateApplicationStatus(Status status) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Application SET Status = ? WHERE ApplicationID = ?")) {

            statement.setString(1, status.toString().toLowerCase());
            statement.setInt(2, applicationId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Failed to update application status. No rows affected.");
            } else {
                this.status = status;
                System.out.println("Application status updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update application status. Error: " + e.getMessage());
        }
    }

    public static void deleteApplication(int applicationId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Application WHERE ApplicationID = ?")) {

            statement.setInt(1, applicationId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Failed to delete application. No rows affected.");
            } else {
                System.out.println("Application deleted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete application. Error: " + e.getMessage());
        }
    }

    public static Application getApplicationById(int applicationId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Application WHERE ApplicationID = ?")) {

            statement.setInt(1, applicationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("UserID");
                    int jobId = resultSet.getInt("JobID");
                    String statusString = resultSet.getString("Status");
                    Status status = Status.valueOf(statusString.toUpperCase());

                    return new Application(applicationId, userId, jobId, status);
                } else {
                    System.out.println("Application with ApplicationID " + applicationId + " not found.");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch application information. Error: " + e.getMessage());
            return null;
        }
    }
    @Override
    public int getNumberOfRecords() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "root", "root");
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Application")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch application count. Error: " + e.getMessage());
        }
        return 0; 
    }
    
    public static List<User> getUsersForJob(int jobId) {
        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM User WHERE UserID IN (SELECT UserID FROM Application WHERE JobID = ?)")) {

            statement.setInt(1, jobId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("UserID");
                    String name = resultSet.getString("Name");
                    String email = resultSet.getString("Email");
                    String password = resultSet.getString("Password");
                    String roleString = resultSet.getString("Role");
                    User.Role role = User.Role.valueOf(roleString.toUpperCase()); // Resolve Role here

                    User user = new User(userId, name, email, password, role);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch users for JobID " + jobId + ". Error: " + e.getMessage());
        }

        return users;
    }
}
