import java.util.ArrayList;
import java.util.Scanner;

class User {
    private String userId;
    private String userPin;
    private double balance;
    private ArrayList<String> transactionHistory;

    public User(String userId, String userPin, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: $" + amount);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        transactionHistory.add("Withdrawn: $" + amount);
        return true;
    }

    public boolean transfer(User recipient, double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        recipient.deposit(amount);
        transactionHistory.add("Transferred: $" + amount + " to " + recipient.getUserId());
        return true;
    }

    public void displayTransactionHistory() {
        System.out.println("Transaction History:");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions available.");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
}

public class ATMSystem {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Sample users
        User user1 = new User("user123", "1234", 1000.0);
        User user2 = new User("user456", "5678", 500.0);

        System.out.println("Welcome to the ATM System");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter User PIN: ");
        String userPin = scanner.nextLine();

        User currentUser = authenticate(userId, userPin, user1, user2);

        if (currentUser == null) {
            System.out.println("Authentication Failed. Exiting...");
            return;
        }

        System.out.println("Login Successful!");
        displayMenu(currentUser, user1, user2);
    }

    private static User authenticate(String userId, String userPin, User... users) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getUserPin().equals(userPin)) {
                return user;
            }
        }
        return null;
    }

    private static void displayMenu(User currentUser, User user1, User user2) {
        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. View Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    currentUser.displayTransactionHistory();
                    break;
                case 2:
                    handleWithdrawal(currentUser);
                    break;
                case 3:
                    handleDeposit(currentUser);
                    break;
                case 4:
                    handleTransfer(currentUser, user1, user2);
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleWithdrawal(User currentUser) {
        System.out.print("Enter amount to withdraw: $");
        double amount = scanner.nextDouble();
        if (currentUser.withdraw(amount)) {
            System.out.println("Withdrawal successful. Remaining balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Insufficient balance. Withdrawal failed.");
        }
    }

    private static void handleDeposit(User currentUser) {
        System.out.print("Enter amount to deposit: $");
        double amount = scanner.nextDouble();
        currentUser.deposit(amount);
        System.out.println("Deposit successful. Current balance: $" + currentUser.getBalance());
    }

    private static void handleTransfer(User currentUser, User user1, User user2) {
        System.out.print("Enter recipient User ID: ");
        scanner.nextLine(); // Consume the newline
        String recipientId = scanner.nextLine();
        System.out.print("Enter amount to transfer: $");
        double amount = scanner.nextDouble();

        User recipient = null;
        if (user1.getUserId().equals(recipientId)) {
            recipient = user1;
        } else if (user2.getUserId().equals(recipientId)) {
            recipient = user2;
        }

        if (recipient == null) {
            System.out.println("Recipient not found. Transfer failed.");
        } else if (currentUser.transfer(recipient, amount)) {
            System.out.println("Transfer successful. Remaining balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Insufficient balance. Transfer failed.");
        }
    }
}
