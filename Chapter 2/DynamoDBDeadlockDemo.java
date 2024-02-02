public class DynamoDBDeadlockDemo {
    private static final Object Item1Lock = new Object();
    private static final Object Item2Lock = new Object();

    public static void main(String[] args) {
        Thread lambdaFunction1 = new Thread(() -> {
            synchronized (Item1Lock) {
                System.out.println("Lambda Function 1 locked Item 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                System.out.println("Lambda Function 1 waiting to lock Item 2");
                synchronized (Item2Lock) {
                    System.out.println("Lambda Function 1 locked Item 1 & 2");
                }
            }
        });
        Thread lambdaFunction2 = new Thread(() -> {
            synchronized (Item2Lock) {
                System.out.println("Lambda Function 2 locked Item 2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                System.out.println("Lambda Function 2 waiting to lock Item 1");
                synchronized (Item1Lock) {
                    System.out.println("Lambda Function 2 locked Item 1 & 2");
                }
            }
        });
        lambdaFunction1.start();
        lambdaFunction2.start();
    }
}
