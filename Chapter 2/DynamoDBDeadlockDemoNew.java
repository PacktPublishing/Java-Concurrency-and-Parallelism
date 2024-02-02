public class DynamoDBDeadlockDemoNew {
    private static final Object Item1Lock = new Object();
    private static final Object Item2Lock = new Object();

    public static void main(String[] args) {
        Thread lambdaFunction1 = new Thread(() -> {
            synchronized (Item1Lock) {
                System.out.println("Lambda Function 1 locked Item 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Properly handle InterruptedException
                }
                synchronized (Item2Lock) {
                    System.out.println("Lambda Function 1 locked Item 1 & 2");
                }
            }
        }); // Corrected: Closing bracket for lambdaFunction1 thread definition

        // Thread representing Lambda Function 2
        Thread lambdaFunction2 = new Thread(() -> {
            // First, attempt to lock Item1
            synchronized (Item1Lock) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Properly handle InterruptedException
                }
                // Then, attempt to lock Item2
                synchronized (Item2Lock) {
                    System.out.println("Lambda Function 2 locked Item 1 & 2");
                }
            }
        });

        lambdaFunction1.start();
        lambdaFunction2.start();
    }
}
