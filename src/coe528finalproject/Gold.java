package coe528finalproject;

/**
 *
 * @author Samuel & Sanjay
 */
public class Gold extends Status {

    @Override
    public void buy(Customer c, double sum) {

        String customerPoints = c.getPoints(); // Get customer points
        int customerPointsInt = Integer.parseInt(customerPoints);
        int availablePoint = customerPointsInt / 100;
        int leftover = 0;
        if (customerPointsInt > availablePoint * 100) {
            leftover = customerPointsInt - availablePoint * 100;
        }
        System.out.println(sum);
        // Calculating point redemption and new transaction cost
        if (availablePoint <= sum) {
            customerPointsInt = 0;
            sum = sum - availablePoint;
            System.out.println(sum);

        } else {
            customerPointsInt = (int) ((availablePoint - sum) * 100);
            sum = 0;
        }

        c.updateTransactionCost(sum);
        c.setPoints(Integer.toString(customerPointsInt + leftover + ((int)sum*10)));
    }

    @Override
    public String toString() {
        return "Gold";
    }
}
