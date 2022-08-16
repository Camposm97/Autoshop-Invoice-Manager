package model.tps;

import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;

public class TPS {
    private List<Transaction> transactions;
    private int numTransactions;
    private int mostRecentTransaction;

    public TPS() {
        this.transactions = new LinkedList<>();
        this.numTransactions = 0;
        this.mostRecentTransaction = -1;
    }

    public boolean hasTransactionToRedo() {
        return (mostRecentTransaction + 1) < numTransactions;
    }

    public boolean hasTransactionToUndo() {
        return mostRecentTransaction >= 0;
    }

    public void addTransaction(Transaction t) {
        if ((mostRecentTransaction < 0) || (mostRecentTransaction < (transactions.size() - 1))) {
            for (int i = transactions.size() - 1; i > mostRecentTransaction; i--) {
                transactions.remove(i);
            }
            numTransactions = mostRecentTransaction + 2;
        } else {
            numTransactions++;
        }

        // ADD THE TRANSACTION
        this.transactions.add(mostRecentTransaction+1, t);

        // AND EXECUTE IT
        doTransaction();
    }

    public void doTransaction() {
        if (this.hasTransactionToRedo()) {
            Transaction transaction = transactions.get(mostRecentTransaction + 1);
            transaction.doTransaction();
            this.mostRecentTransaction++;
        }
    }

    /**
     * This function gets the most recently executed transaction on the
     * TPS stack and undoes it, moving the TPS counter accordingly.
     */
    public void undoTransaction() {
        if (this.hasTransactionToUndo()) {
            Transaction transaction = transactions.get(mostRecentTransaction);
            transaction.undoTransaction();
            this.mostRecentTransaction--;
        }
    }

    @Override
    public String toString() {
        String text = "--Number of Transactions: " + this.numTransactions + "\n";
        text += "--Current Index on Stack: " + this.mostRecentTransaction + "\n";
        text += "--Current Transaction Stack:\n";
        for (int i = 0; i <= this.mostRecentTransaction; i++) {
            Transaction jT = this.transactions.get(i);
            text += "----" + jT.toString() + "\n";
        }
        return text;
    }

    public interface Transaction {
        void doTransaction();
        void undoTransaction();
    }
}
