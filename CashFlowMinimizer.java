import java.util.*;


public class CashFlowMinimizer {



    static class Bank {
        String name;
        int netAmount;
        TreeSet<String> types = new TreeSet<>();

        Bank(String name) {
            this.name = name;
        }
    }


    static class Transaction {
        int amount = 0;
        String type = "";
    }

    static int getMinIndex(Bank[] netAmounts) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int i = 0; i < netAmounts.length; i++) {
            if (netAmounts[i].netAmount == 0) continue;
            if (netAmounts[i].netAmount < min) {
                min = netAmounts[i].netAmount;
                minIndex = i;
            }
        }
        return minIndex;
    }


    static int getSimpleMaxIndex(Bank[] netAmounts) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
                (a, b) -> netAmounts[b].netAmount - netAmounts[a].netAmount);

        for (int i = 0; i < netAmounts.length; i++) {
            if (netAmounts[i].netAmount != 0) {
                maxHeap.offer(i);
            }
        }
        return maxHeap.isEmpty() ? -1 : maxHeap.peek();
    }


    static Object[] getMaxIndex(Bank[] netAmounts, int minIndex) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
                (a, b) -> netAmounts[b].netAmount - netAmounts[a].netAmount);

        for (int i = 0; i < netAmounts.length; i++) {
            if (netAmounts[i].netAmount > 0) {
                maxHeap.offer(i);
            }
        }

        while (!maxHeap.isEmpty()) {
            int i = maxHeap.poll();

            TreeSet<String> intersection = new TreeSet<>(netAmounts[minIndex].types);
            intersection.retainAll(netAmounts[i].types);

            if (!intersection.isEmpty()) {
                return new Object[] { i, intersection.first() };
            }
        }

        return new Object[] { -1, "" };
    }


    static void minimizeCashFlow(int numBanks, Bank[] input, int[][] graph) {

        Bank[] netAmounts = new Bank[numBanks];
        for (int b = 0; b < numBanks; b++) {
            netAmounts[b] = new Bank(input[b].name);
            netAmounts[b].types = new TreeSet<>(input[b].types);

            int amount = 0;

            for (int i = 0; i < numBanks; i++) {
                amount += graph[i][b];
            }

            for (int j = 0; j < numBanks; j++) {
                amount -= graph[b][j];
            }
            netAmounts[b].netAmount = amount;
        }

        Transaction[][] ansGraph = new Transaction[numBanks][numBanks];
        for (int i = 0; i < numBanks; i++) {
            for (int j = 0; j < numBanks; j++) {
                ansGraph[i][j] = new Transaction();
            }
        }

        int numZeroNetAmounts = 0;
        for (int i = 0; i < numBanks; i++) {
            if (netAmounts[i].netAmount == 0) numZeroNetAmounts++;
        }

        while (numZeroNetAmounts != numBanks) {
            int minIndex = getMinIndex(netAmounts);
            Object[] maxAns = getMaxIndex(netAmounts, minIndex);
            int maxIndex = (int) maxAns[0];

            if (maxIndex == -1) {

                ansGraph[minIndex][0].amount += Math.abs(netAmounts[minIndex].netAmount);
                ansGraph[minIndex][0].type = input[minIndex].types.first();

                int simpleMaxIndex = getSimpleMaxIndex(netAmounts);
                ansGraph[0][simpleMaxIndex].amount += Math.abs(netAmounts[minIndex].netAmount);
                ansGraph[0][simpleMaxIndex].type = input[simpleMaxIndex].types.first();

                netAmounts[simpleMaxIndex].netAmount += netAmounts[minIndex].netAmount;
                netAmounts[minIndex].netAmount = 0;

                numZeroNetAmounts++; 
                if (netAmounts[simpleMaxIndex].netAmount == 0) numZeroNetAmounts++;

            } else {
                int transactionAmount = Math.min(
                        Math.abs(netAmounts[minIndex].netAmount),
                        netAmounts[maxIndex].netAmount);

                ansGraph[minIndex][maxIndex].amount += transactionAmount;
                ansGraph[minIndex][maxIndex].type = (String) maxAns[1];

                netAmounts[minIndex].netAmount += transactionAmount;
                netAmounts[maxIndex].netAmount -= transactionAmount;

                if (netAmounts[minIndex].netAmount == 0) numZeroNetAmounts++;
                if (netAmounts[maxIndex].netAmount == 0) numZeroNetAmounts++;
            }
        }

        printAns(ansGraph, numBanks, input);
    }

    static void printAns(Transaction[][] ansGraph, int numBanks, Bank[] input) {
        System.out.println("\nThe transactions for minimum cash flow are as follows :\n");

        for (int i = 0; i < numBanks; i++) {
            for (int j = 0; j < numBanks; j++) {
                if (i == j) continue;

                if (ansGraph[i][j].amount != 0 && ansGraph[j][i].amount != 0) {
                    if (ansGraph[i][j].amount == ansGraph[j][i].amount) {
                        ansGraph[i][j].amount = 0;
                        ansGraph[j][i].amount = 0;
                    } else if (ansGraph[i][j].amount > ansGraph[j][i].amount) {
                        ansGraph[i][j].amount -= ansGraph[j][i].amount;
                        ansGraph[j][i].amount = 0;

                        System.out.println(input[i].name + " pays Rs " + ansGraph[i][j].amount
                                + " to " + input[j].name + " via " + ansGraph[i][j].type);
                    } else {
                        ansGraph[j][i].amount -= ansGraph[i][j].amount;
                        ansGraph[i][j].amount = 0;

                        System.out.println(input[j].name + " pays Rs " + ansGraph[j][i].amount
                                + " to " + input[i].name + " via " + ansGraph[j][i].type);
                    }
                } else if (ansGraph[i][j].amount != 0) {
                    System.out.println(input[i].name + " pays Rs " + ansGraph[i][j].amount
                            + " to " + input[j].name + " via " + ansGraph[i][j].type);
                } else if (ansGraph[j][i].amount != 0) {
                    System.out.println(input[j].name + " pays Rs " + ansGraph[j][i].amount
                            + " to " + input[i].name + " via " + ansGraph[j][i].type);
                }

                ansGraph[i][j].amount = 0;
                ansGraph[j][i].amount = 0;
            }
        }
        System.out.println();
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\t\t\t\t********************* Welcome to CASH FLOW MINIMIZER SYSTEM ***********************\n\n");
        System.out.println("This system minimizes the number of transactions among multiple banks in the different "
                + "corners of the world that use different modes of payment. There is one world bank (with all "
                + "payment modes) to act as an intermediary between banks that have no common mode of payment.\n");

        System.out.println("Enter the number of banks participating in the transactions.");
        int numBanks = sc.nextInt();

        Bank[] input = new Bank[numBanks];
        Map<String, Integer> indexOf = new HashMap<>(); 

        System.out.println("Enter the details of the banks and transactions as stated:");
        System.out.println("Bank name, number of payment modes it has, and the payment modes.");
        System.out.println("Bank name and payment modes should not contain spaces");

        for (int i = 0; i < numBanks; i++) {
            if (i == 0) {
                System.out.print("World Bank : ");
            } else {
                System.out.print("Bank " + i + " : ");
            }

            String name = sc.next();
            input[i] = new Bank(name);
            indexOf.put(name, i);

            int numTypes = sc.nextInt();
            for (int t = 0; t < numTypes; t++) {
                input[i].types.add(sc.next());
            }
        }

        System.out.println("Enter number of transactions.");
        int numTransactions = sc.nextInt();

        int[][] graph = new int[numBanks][numBanks];

        System.out.println("Enter the details of each transaction as stated: Debtor Bank, Creditor Bank, and amount");
        System.out.println("The transactions can be in any order");

        for (int i = 0; i < numTransactions; i++) {
            System.out.print(i + " th transaction : ");
            String s1 = sc.next();
            String s2 = sc.next();
            int amount = sc.nextInt();

            graph[indexOf.get(s1)][indexOf.get(s2)] = amount;
        }

        minimizeCashFlow(numBanks, input, graph);

        sc.close();
    }
}

/*
Sample input (World Bank must be entered first, index 0):

5
World_Bank 2 Google_Pay PayTM
Bank_B 1 Google_Pay
Bank_C 1 Google_Pay
Bank_D 1 PayTM
Bank_E 1 PayTM
4
Bank_B World_Bank 300
Bank_C World_Bank 700
Bank_D Bank_B 500
Bank_E Bank_B 500

--------------------

6
B 3 1 2 3
C 2 1 2
D 1 2
E 2 1 3
F 1 3
G 2 2 3
9
G B 30
G D 10
F B 10
F C 30
F D 10
F E 10
B C 40
C D 20
D E 50
*/
