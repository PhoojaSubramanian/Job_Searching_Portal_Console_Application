import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Job Search Console!");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Create User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. View User Information");
            System.out.println("5. Create Job");
            System.out.println("6. Update Job");
            System.out.println("7. Delete Job");
            System.out.println("8. View Job Information");
            System.out.println("9. Create Application");
            System.out.println("10. Update Application Status");
            System.out.println("11. Delete Application");
            System.out.println("12. View Application Information");
            System.out.println("13. View Records count");
            System.out.println("14. View Applicants for a Job");
            System.out.println("15. View Employer for a Job");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    createUser(scanner);
                    break;
                case 2:
                    updateUser(scanner);
                    break;
                case 3:
                    deleteUser(scanner);
                    break;
                case 4:
                    viewUserInformation(scanner);
                    break;
                case 5:
                    createJob(scanner);
                    break;
                case 6:
                    updateJob(scanner);
                    break;
                case 7:
                    deleteJob(scanner);
                    break;
                case 8:
                    viewJobInformation(scanner);
                    break;
                case 9:
                    createApplication(scanner);
                    break;
                case 10:
                    updateApplicationStatus(scanner);
                    break;
                case 11:
                    deleteApplication(scanner);
                    break;
                case 12:
                    viewApplicationInformation(scanner);
                    break;
                case 13:
                	viewRecordCounts();
                    break;
                case 14:
                    viewUsersForJob(scanner);
                    break;
                case 15:
                	viewJobsEmployer(scanner);
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void viewRecordCounts() {

        User user = new User();
        Job job = new Job();
        Application application = new Application();


        int userCount = user.getNumberOfRecords();
        int jobCount = job.getNumberOfRecords();
        int applicationCount = application.getNumberOfRecords();


        System.out.println("User Count: " + userCount);
        System.out.println("Job Count: " + jobCount);
        System.out.println("Application Count: " + applicationCount);
    }

    public static void viewUsersForJob(Scanner scanner) {
        System.out.println("Enter the JobID to view users for:");
        int jobId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        List<User> usersForJob = Application.getUsersForJob(jobId);
        if (usersForJob != null && !usersForJob.isEmpty()) {
            System.out.println("Users who have applied for JobID " + jobId + ":");
            for (User user : usersForJob) {
                System.out.println("User ID: " + user.getUserId() + ", Name: " + user.getName() + ", Email: " + user.getEmail());
            }
        } else {
            System.out.println("No users found for JobID " + jobId + ".");
        }
    }

    public static void createUser(Scanner scanner) {
        System.out.println("Enter the user's name:");
        String name = scanner.nextLine();

        System.out.println("Enter the user's email:");
        String email = scanner.nextLine();

        System.out.println("Enter the user's password:");
        String password = scanner.nextLine();

        System.out.println("Select the user's role (JOB_SEEKER or EMPLOYER):");
        String roleString = scanner.nextLine();
        User.Role role = User.Role.valueOf(roleString.toUpperCase());

        User user = User.buildUser(name, email, password, role);
        if (user != null) {
            System.out.println("User created successfully.");
            System.out.println("User ID: " + user.getUserId());
        }
    }

    public static void updateUser(Scanner scanner) {
        System.out.println("Enter the UserID of the user you want to update:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        User userToUpdate = User.getUserById(userId);
        if (userToUpdate == null) {
            System.out.println("User with UserID " + userId + " not found.");
            return;
        }

        System.out.println("Enter the updated name:");
        String name = scanner.nextLine();

        System.out.println("Enter the updated email:");
        String email = scanner.nextLine();

        System.out.println("Enter the updated password:");
        String password = scanner.nextLine();

        System.out.println("Select the updated role (JOB_SEEKER or EMPLOYER):");
        String roleString = scanner.nextLine();
        User.Role role = User.Role.valueOf(roleString.toUpperCase());

        userToUpdate.updateUser(name, email, password, role);
    }

    public static void deleteUser(Scanner scanner) {
        System.out.println("Enter the UserID of the user you want to delete:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        User.deleteUser(userId);
    }

    public static void viewUserInformation(Scanner scanner) {
        System.out.println("Enter the UserID of the user you want to view:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        User user = User.getUserById(userId);
        if (user == null) {
            System.out.println("User with UserID " + userId + " not found.");
            return;
        }

        System.out.println("User Information:");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
    }
    

    public static void createJob(Scanner scanner) {
        System.out.println("Enter the employer ID for the job:");
        int employerId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Enter the job title:");
        String title = scanner.nextLine();

        System.out.println("Enter the job description:");
        String description = scanner.nextLine();

        System.out.println("Enter the job requirements:");
        String requirements = scanner.nextLine();

        System.out.println("Enter the job location:");
        String location = scanner.nextLine();

        Job job = Job.buildJob(employerId, title, description, requirements, location);
        if (job != null) {
            System.out.println("Job created successfully.");
            System.out.println("Job ID: " + job.getJobId());
        }
    }

    public static void deleteJob(Scanner scanner) {
        System.out.println("Enter the JobID of the job you want to delete:");
        int jobId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Job.deleteJob(jobId);
    }

    public static void updateJob(Scanner scanner) {
        System.out.println("Enter the JobID of the job you want to update:");
        int jobId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Job jobToUpdate = Job.getJobById(jobId);
        if (jobToUpdate == null) {
            System.out.println("Job with JobID " + jobId + " not found.");
            return;
        }

        System.out.println("Enter the updated employer ID for the job:");
        int employerId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Enter the updated job title:");
        String title = scanner.nextLine();

        System.out.println("Enter the updated job description:");
        String description = scanner.nextLine();

        System.out.println("Enter the updated job requirements:");
        String requirements = scanner.nextLine();

        System.out.println("Enter the updated job location:");
        String location = scanner.nextLine();

        jobToUpdate.updateJob(employerId, title, description, requirements, location);
    }

    public static void viewJobInformation(Scanner scanner) {
        System.out.println("Enter the JobID of the job you want to view:");
        int jobId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Job job = Job.getJobById(jobId);
        if (job == null) {
            System.out.println("Job with JobID " + jobId + " not found.");
            return;
        }

        System.out.println("Job Information:");
        System.out.println("Job ID: " + job.getJobId());
        System.out.println("Employer ID: " + job.getEmployerId());
        System.out.println("Title: " + job.getTitle());
        System.out.println("Description: " + job.getDescription());
        System.out.println("Requirements: " + job.getRequirements());
        System.out.println("Location: " + job.getLocation());
    }

    public static void createApplication(Scanner scanner) {
        System.out.println("Enter the User ID for the application:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Enter the Job ID for the application:");
        int jobId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Select the application status (APPLIED, IN_REVIEW, REJECTED, or ACCEPTED):");
        String statusString = scanner.nextLine();
        Application.Status status = Application.Status.valueOf(statusString.toUpperCase());

        Application application = Application.buildApplication(userId, jobId, status);
        if (application != null) {
            System.out.println("Application created successfully.");
            System.out.println("Application ID: " + application.getApplicationId());
        }  
    }

    public static void deleteApplication(Scanner scanner) {
        System.out.println("Enter the ApplicationID of the application you want to delete:");
        int applicationId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Application.deleteApplication(applicationId);
    }

    public static void updateApplicationStatus(Scanner scanner) {
        System.out.println("Enter the ApplicationID of the application you want to update:");
        int applicationId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Application applicationToUpdate = Application.getApplicationById(applicationId);
        if (applicationToUpdate == null) {
            System.out.println("Application with ApplicationID " + applicationId + " not found.");
            return;
        }

        System.out.println("Select the updated application status (APPLIED, IN_REVIEW, REJECTED, or ACCEPTED):");
        String statusString = scanner.nextLine();
        Application.Status status = Application.Status.valueOf(statusString.toUpperCase());

        applicationToUpdate.updateApplicationStatus(status);
    }

    public static void viewApplicationInformation(Scanner scanner) {
        System.out.println("Enter the ApplicationID of the application you want to view:");
        int applicationId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Application application = Application.getApplicationById(applicationId);
        if (application == null) {
            System.out.println("Application with ApplicationID " + applicationId + " not found.");
            return;
        }

        System.out.println("Application Information:");
        System.out.println("Application ID: " + application.getApplicationId());
        System.out.println("User ID: " + application.getUserId());
        System.out.println("Job ID: " + application.getJobId());
        System.out.println("Status: " + application.getStatus());
    }
    
    public static void viewJobsEmployer(Scanner scanner) {
        System.out.println("Enter the JobID of the job you want to view the employer:");
        int jobId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String emp = Job.getJobWithEmployerName(jobId);
        if (emp == null) {
            return;
        }

        System.out.println("Job Information:");
        System.out.println("Employer Name: " + emp);

    }

}